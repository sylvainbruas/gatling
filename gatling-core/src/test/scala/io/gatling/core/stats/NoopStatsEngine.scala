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

package io.gatling.core.stats

import io.gatling.commons.stats.Status
import io.gatling.core.actor.ActorRef
import io.gatling.core.controller.Controller
import io.gatling.core.session.GroupBlock

object NoopStatsEngine extends StatsEngine {
  override private[gatling] def start(): Unit = {}

  override private[gatling] def stop(controller: ActorRef[Controller.Command], exception: Option[Exception]): Unit = {}

  override def logUserStart(scenario: String): Unit = {}

  override def logUserEnd(scenario: String): Unit = {}

  override def logResponse(
      scenario: String,
      groups: List[String],
      requestName: String,
      startTimestamp: Long,
      endTimestamp: Long,
      status: Status,
      responseCode: Option[String],
      message: Option[String]
  ): Unit = {}

  override def logGroupEnd(scenario: String, groupBlock: GroupBlock, exitTimestamp: Long): Unit = {}

  override def logRequestCrash(scenario: String, groups: List[String], requestName: String, error: String): Unit = {}
}
