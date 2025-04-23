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

package io.gatling.charts.template

import io.gatling.charts.FileNamingConventions
import io.gatling.charts.component.RequestStatistics
import io.gatling.charts.report.GroupContainer
import io.gatling.charts.util.JsHelper._

private[charts] final class MenuTreeTemplate(rootContainer: GroupContainer) {
  private def renderNode(request: RequestStatistics, path: String): String =
    s"""name: "${escapeJsIllegalChars(request.name)}",
path: "${escapeJsIllegalChars(request.path)}",
pathFormatted: "$path""""

  private def renderSubGroups(group: GroupContainer): Iterable[String] =
    group.groups.values.map { subGroup =>
      s""""${subGroup.name.toGroupFileName}": {
          ${renderGroup(subGroup)}
     }"""
    }

  private def renderSubRequests(group: GroupContainer): Iterable[String] =
    group.requests.values.map { request =>
      s""""${request.name.toRequestFileName}": {
        type: "REQUEST",
        ${renderNode(request.stats, request.stats.path.toRequestFileName)}
    }"""
    }

  private def renderGroup(group: GroupContainer): String =
    s"""type: "GROUP",
${renderNode(group.stats, group.stats.path.toGroupFileName)},
contents: {
${(renderSubGroups(group) ++ renderSubRequests(group)).mkString(",")}
}
"""

  def getOutput: String =
    s"""var stats = {
    ${renderGroup(rootContainer)}
}
"""
}
