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

package io.gatling.charts.stats

final case class Ranges(lowerBound: Int, higherBound: Int, lowCount: Int, middleCount: Int, highCount: Int, koCount: Int) {

  private val totalCount = lowCount + middleCount + highCount + koCount
  private def percentage(count: Int): Double = if (totalCount == 0) 0 else count.toDouble / totalCount * 100
  def lowPercentage: Double = percentage(lowCount)
  def middlePercentage: Double = percentage(middleCount)
  def highPercentage: Double = percentage(highCount)
  def koPercentage: Double = percentage(koCount)
}
