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

package io.gatling.charts.component

import io.gatling.charts.stats.Ranges
import io.gatling.core.config.IndicatorsConfiguration
import io.gatling.shared.util.NumberHelper._

private[charts] final class Stats[T: Numeric](val name: String, val total: T, val success: T, val failure: T)

private[charts] final class RequestStatistics(
    val name: String,
    val path: String,
    val numberOfRequestsStatistics: Stats[Long],
    val minResponseTimeStatistics: Stats[Int],
    val maxResponseTimeStatistics: Stats[Int],
    val meanResponseTimeStatistics: Stats[Int],
    val stdDeviationStatistics: Stats[Int],
    val percentiles1: Stats[Int],
    val percentiles2: Stats[Int],
    val percentiles3: Stats[Int],
    val percentiles4: Stats[Int],
    val ranges: Ranges,
    val meanNumberOfRequestsPerSecondStatistics: Stats[Double]
)

private[charts] final class DetailsStatsTableComponent(stats: RequestStatistics, configuration: IndicatorsConfiguration) extends Component {

  import stats._

  override def html: String = s"""
                        <div class="infos">
                            <div class="infos-in">
	                        <div class="infos-title">Stats</div>
                                <div class="info">
                                    <h2 class="first">Executions</h2>
                                    <table>
                                        <thead>
                                            <tr><th></th><th>Total</th><th>OK</th><th>KO</th></tr>
                                        </thead>
                                        <tbody>
                                            <tr>
                                                <td class="title">Total count</td>
                                                <td class="total">${style(numberOfRequestsStatistics.total)}</td>
                                                <td class="ok">${style(numberOfRequestsStatistics.success)}</td>
                                                <td class="ko">${style(numberOfRequestsStatistics.failure)}</td>
                                            </tr>
                                            <tr>
                                                <td class="title">Mean count/s</abbr></td>
                                                <td class="total">${style(meanNumberOfRequestsPerSecondStatistics.total)}</td>
                                                <td class="ok">${style(meanNumberOfRequestsPerSecondStatistics.success)}</td>
                                                <td class="ko">${style(meanNumberOfRequestsPerSecondStatistics.failure)}</td>
                                            </tr>
                                        </tbody>
                                    </table>
                                    <h2 class="second">Response Time (ms)</h2>
                                    <table>
                                        <thead>
                                            <tr>
                                                <th></th>
                                                <th>Total</th>
                                                <th>OK</th>
                                                <th>KO</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr>
                                                <td class="title">Min</td>
                                                <td class="total">${style(minResponseTimeStatistics.total)}</td>
                                                <td class="ok">${style(minResponseTimeStatistics.success)}</td>
                                                <td class="ko">${style(minResponseTimeStatistics.failure)}</td>
                                            </tr>
                                            <tr>
                                                <td class="title">${configuration.percentile1.toRank} percentile</td>
                                                <td class="total">${style(percentiles1.total)}</td>
                                                <td class="ok">${style(percentiles1.success)}</td>
                                                <td class="ko">${style(percentiles1.failure)}</td>
                                            </tr>
                                            <tr>
                                                <td class="title">${configuration.percentile2.toRank} percentile</td>
                                                <td class="total">${style(percentiles2.total)}</td>
                                                <td class="ok">${style(percentiles2.success)}</td>
                                                <td class="ko">${style(percentiles2.failure)}</td>
                                            </tr>
                                            <tr>
                                                <td class="title">${configuration.percentile3.toRank} percentile</td>
                                                <td class="total">${style(percentiles3.total)}</td>
                                                <td class="ok">${style(percentiles3.success)}</td>
                                                <td class="ko">${style(percentiles3.failure)}</td>
                                            </tr>
                                            <tr>
                                                <td class="title">${configuration.percentile4.toRank} percentile</td>
                                                <td class="total">${style(percentiles4.total)}</td>
                                                <td class="ok">${style(percentiles4.success)}</td>
                                                <td class="ko">${style(percentiles4.failure)}</td>
                                            </tr>
                                            <tr>
                                                <td class="title">Max</td>
                                                <td class="total">${style(maxResponseTimeStatistics.total)}</td>
                                                <td class="ok">${style(maxResponseTimeStatistics.success)}</td>
                                                <td class="ko">${style(maxResponseTimeStatistics.failure)}</td>
                                            </tr>
                                            <tr>
                                                <td class="title">Mean</td>
                                                <td class="total">${style(meanNumberOfRequestsPerSecondStatistics.total)}</td>
                                                <td class="ok">${style(meanNumberOfRequestsPerSecondStatistics.success)}</td>
                                                <td class="ko">${style(meanNumberOfRequestsPerSecondStatistics.failure)}</td>
                                            </tr>
                                            <tr>
                                                <td class="title">Standard Deviation</td>
                                                <td class="total">${style(stdDeviationStatistics.total)}</td>
                                                <td class="ok">${style(stdDeviationStatistics.success)}</td>
                                                <td class="ko">${style(stdDeviationStatistics.failure)}</td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
"""

  override def js = ""

  override def jsFiles: Seq[String] = Nil
}
