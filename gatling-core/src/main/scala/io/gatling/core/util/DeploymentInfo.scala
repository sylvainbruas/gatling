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

package io.gatling.core.util

import java.util.UUID

object DeploymentInfo {
  val runId: Option[UUID] = None
  val locationName: Option[String] = None
  val numberOfLoadGeneratorsInLocation: Int = 1
  val indexOfLoadGeneratorInLocation: Int = 0
  val numberOfLoadGeneratorsInRun: Int = 1
  val indexOfLoadGeneratorInRun: Int = 0
}
