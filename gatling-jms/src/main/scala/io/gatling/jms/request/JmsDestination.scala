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

package io.gatling.jms.request

import io.gatling.core.session.Expression

sealed trait JmsDestination

object JmsDestination {
  final case class Queue(name: Expression[String]) extends JmsDestination
  final case class Topic(name: Expression[String]) extends JmsDestination
  case object TemporaryQueue extends JmsDestination
  case object TemporaryTopic extends JmsDestination
}
