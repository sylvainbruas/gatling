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

import scala.annotation.tailrec
import scala.collection.mutable

import io.gatling.charts.component.RequestStatistics
import io.gatling.charts.stats.Group

private[charts] trait Container {
  def name: String
  def stats: RequestStatistics
}

private[charts] final class RequestContainer(
    override val name: String,
    val group: Option[Group],
    override val stats: RequestStatistics
) extends Container

private[charts] object GroupContainer {
  def root(requestStats: RequestStatistics): GroupContainer = GroupContainer(Group.Root, requestStats)

  def apply(group: Group, stats: RequestStatistics): GroupContainer =
    new GroupContainer(group, stats, mutable.LinkedHashMap.empty, mutable.LinkedHashMap.empty)
}

private[charts] final class GroupContainer(
    val group: Group,
    override val stats: RequestStatistics,
    val requests: mutable.Map[String, RequestContainer],
    val groups: mutable.Map[String, GroupContainer]
) extends Container {
  override def name: String = group.name

  private def findGroup(path: List[String]) = {
    @tailrec
    def getGroupRec(g: GroupContainer, path: List[String]): GroupContainer = path match {
      case head :: tail => getGroupRec(g.groups(head), tail)
      case _            => g
    }

    getGroupRec(this, path)
  }

  def addGroup(group: Group, stats: RequestStatistics): Unit = {
    val parentGroup = group.hierarchy.dropRight(1)
    findGroup(parentGroup).groups += (group.name -> GroupContainer(group, stats))
  }

  def addRequest(group: Option[Group], requestName: String, stats: RequestStatistics): Unit = {
    val parentGroup = group.map(_.hierarchy).getOrElse(Nil)
    findGroup(parentGroup).requests += (requestName -> new RequestContainer(requestName, group, stats))
  }
}
