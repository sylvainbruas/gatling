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

package io.gatling.http.request.builder

import io.gatling.core.session.Expression

sealed trait HttpParam
final case class SimpleParam(key: Expression[String], value: Expression[Any]) extends HttpParam
final case class MultivaluedParam(key: Expression[String], values: Expression[Seq[Any]]) extends HttpParam
final case class ParamSeq(seq: Expression[Seq[(String, Any)]]) extends HttpParam
final case class ParamMap(map: Expression[Map[String, Any]]) extends HttpParam
