/*
 * Copyright 2011-2025 GatlingCorp (https://gatling.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.gatling.charts.report

import java.nio.charset.Charset
import java.nio.file.Path
import java.time.ZoneId

import io.gatling.charts.component.{ ComponentLibrary, RequestStatistics, Stats }
import io.gatling.charts.config.ChartsFiles
import io.gatling.charts.stats.{ GeneralStats, Group, GroupStatsPath, LogFileData, RequestPath, RequestStatsPath }
import io.gatling.charts.template.ConsoleTemplate
import io.gatling.commons.stats.{ KO, OK, Status }
import io.gatling.core.config.ReportsConfiguration
import io.gatling.shared.util.NumberHelper.RichDouble
import io.gatling.shared.util.ScanHelper

import com.typesafe.scalalogging.StrictLogging

private[gatling] final class ReportsGenerator(
    zoneId: ZoneId,
    charset: Charset,
    resultsDirectory: Path,
    reportsConfiguration: ReportsConfiguration
) extends StrictLogging {
  def generateFor(reportsGenerationInputs: ReportsGenerationInputs): Path = {
    val chartsFiles = new ChartsFiles(reportsGenerationInputs.reportFolderName, resultsDirectory)

    def hasAtLeastOneRequestReported: Boolean =
      reportsGenerationInputs.logFileData.statsPaths.exists(_.isInstanceOf[RequestStatsPath])

    def copyAssets(): Unit = {
      ScanHelper.deepCopyPackageContent(ChartsFiles.GatlingAssetsStylePackage, chartsFiles.styleDirectory)
      ScanHelper.deepCopyPackageContent(ChartsFiles.GatlingAssetsJsPackage, chartsFiles.jsDirectory)
    }

    if (!hasAtLeastOneRequestReported) {
      throw new UnsupportedOperationException("There were no requests sent during the simulation, reports won't be generated")
    }

    val rootContainer = computeRootContainer(reportsGenerationInputs.logFileData, reportsConfiguration)

    val reportGenerators =
      List(
        new AllSessionsReportGenerator(
          reportsGenerationInputs.logFileData,
          chartsFiles,
          ComponentLibrary.Instance,
          charset
        ),
        new GlobalReportGenerator(
          reportsGenerationInputs.logFileData,
          reportsGenerationInputs.assertionResults,
          rootContainer,
          chartsFiles,
          ComponentLibrary.Instance,
          zoneId,
          charset,
          reportsConfiguration
        ),
        new RequestDetailsReportGenerator(
          reportsGenerationInputs.logFileData,
          rootContainer,
          chartsFiles,
          ComponentLibrary.Instance,
          charset,
          reportsConfiguration
        ),
        new GroupDetailsReportGenerator(
          reportsGenerationInputs.logFileData,
          rootContainer,
          chartsFiles,
          ComponentLibrary.Instance,
          charset,
          reportsConfiguration
        ),
        new MenuTreeGenerator(
          rootContainer,
          chartsFiles,
          charset
        )
      )

    copyAssets()
    reportGenerators.foreach(_.generate())

    println(new ConsoleTemplate(rootContainer.stats, reportsGenerationInputs.logFileData.errors(None, None)).getOutput)

    chartsFiles.globalFile
  }

  private def percentiles(rank: Double, title: Double => String, total: GeneralStats, ok: GeneralStats, ko: GeneralStats) =
    new Stats(title(rank) + " (ms)", total.percentile(rank), ok.percentile(rank), ko.percentile(rank))

  private def computeRootContainer(logFileData: LogFileData, configuration: ReportsConfiguration): GroupContainer = {

    def computeRequestStats(name: String, requestName: Option[String], group: Option[Group]): RequestStatistics = {
      val total = logFileData.requestGeneralStats(requestName, group, None)
      val ok = logFileData.requestGeneralStats(requestName, group, Some(OK))
      val ko = logFileData.requestGeneralStats(requestName, group, Some(KO))

      val percentilesTitle = (rank: Double) => s"response time ${rank.toRank} percentile"

      val path = requestName match {
        case Some(n) => RequestPath.path(n, group)
        case _       => group.map(RequestPath.path).getOrElse("")
      }

      new RequestStatistics(
        name,
        path,
        numberOfRequestsStatistics = new Stats("request count", total.count, ok.count, ko.count),
        minResponseTimeStatistics = new Stats("min response time (ms)", total.min, ok.min, ko.min),
        maxResponseTimeStatistics = new Stats("max response time (ms)", total.max, ok.max, ko.max),
        meanResponseTimeStatistics = new Stats("mean response time (ms)", total.mean, ok.mean, ko.mean),
        stdDeviationStatistics = new Stats("response time std deviation (ms)", total.stdDev, ok.stdDev, ko.stdDev),
        percentiles1 = percentiles(configuration.indicators.percentile1, percentilesTitle, total, ok, ko),
        percentiles2 = percentiles(configuration.indicators.percentile2, percentilesTitle, total, ok, ko),
        percentiles3 = percentiles(configuration.indicators.percentile3, percentilesTitle, total, ok, ko),
        percentiles4 = percentiles(configuration.indicators.percentile4, percentilesTitle, total, ok, ko),
        ranges = logFileData.numberOfRequestInResponseTimeRanges(requestName, group),
        meanNumberOfRequestsPerSecondStatistics = new Stats("mean throughput (rps)", total.meanRequestsPerSec, ok.meanRequestsPerSec, ko.meanRequestsPerSec)
      )
    }

    def computeGroupStats(group: Group): RequestStatistics = {
      def groupStatsFunction: (Group, Option[Status]) => GeneralStats =
        if (configuration.useGroupDurationMetric) {
          logger.debug("Use group duration stats.")
          logFileData.groupDurationGeneralStats
        } else {
          logger.debug("Use group cumulated response time stats.")
          logFileData.groupCumulatedResponseTimeGeneralStats
        }

      val total = groupStatsFunction(group, None)
      val ok = groupStatsFunction(group, Some(OK))
      val ko = groupStatsFunction(group, Some(KO))

      new RequestStatistics(
        group.name,
        path = RequestPath.path(group),
        numberOfRequestsStatistics = new Stats("numberOfRequests", total.count, ok.count, ko.count),
        minResponseTimeStatistics = new Stats("minResponseTime", total.min, ok.min, ko.min),
        maxResponseTimeStatistics = new Stats("maxResponseTime", total.max, ok.max, ko.max),
        meanResponseTimeStatistics = new Stats("meanResponseTime", total.mean, ok.mean, ko.mean),
        stdDeviationStatistics = new Stats("stdDeviation", total.stdDev, ok.stdDev, ko.stdDev),
        percentiles1 = percentiles(configuration.indicators.percentile1, _ => "percentiles1", total, ok, ko),
        percentiles2 = percentiles(configuration.indicators.percentile2, _ => "percentiles2", total, ok, ko),
        percentiles3 = percentiles(configuration.indicators.percentile3, _ => "percentiles3", total, ok, ko),
        percentiles4 = percentiles(configuration.indicators.percentile4, _ => "percentiles4", total, ok, ko),
        ranges = logFileData.numberOfRequestInResponseTimeRanges(None, Some(group)),
        meanNumberOfRequestsPerSecondStatistics =
          new Stats("meanNumberOfRequestsPerSecond", total.meanRequestsPerSec, ok.meanRequestsPerSec, ko.meanRequestsPerSec)
      )
    }

    val rootContainer = GroupContainer.root(computeRequestStats("All Requests", None, None))

    val statsPaths = logFileData.statsPaths

    val groupsByHierarchy: Map[List[String], Group] = statsPaths
      .collect {
        case GroupStatsPath(group)            => group
        case RequestStatsPath(_, Some(group)) => group
      }
      .map(group => group.hierarchy.reverse -> group)
      .toMap

    val seenGroups = collection.mutable.HashSet.empty[List[String]]

    @SuppressWarnings(Array("org.wartremover.warts.Recursion"))
    def addGroupsRec(hierarchy: List[String]): Unit =
      if (!seenGroups.contains(hierarchy)) {
        seenGroups += hierarchy

        hierarchy match {
          case _ :: tail if tail.nonEmpty => addGroupsRec(tail)
          case _                          =>
        }

        val group = groupsByHierarchy(hierarchy)
        val stats = computeGroupStats(group)
        rootContainer.addGroup(group, stats)
      }

    val requestStatsPaths = statsPaths.collect { case path: RequestStatsPath => path }
    requestStatsPaths.foreach { case RequestStatsPath(request, group) =>
      group.foreach { group =>
        addGroupsRec(group.hierarchy.reverse)
      }
      val stats = computeRequestStats(request, Some(request), group)
      rootContainer.addRequest(group, request, stats)
    }

    rootContainer
  }
}
