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

package io.gatling.recorder.config

import java.io.{ File, FileNotFoundException }
import java.nio.file.{ Files, Paths }

import org.scalatest.BeforeAndAfter
import org.scalatest.flatspec.AnyFlatSpecLike
import org.scalatest.matchers.should.Matchers

class RecorderConfigurationSpec extends AnyFlatSpecLike with Matchers with BeforeAndAfter {
  private val nonExistingDir = "nonExistingDir"
  private val existingDir = "existingDir"
  private val fileName = "file"

  private def removeAll(): Unit = {
    new File(existingDir, fileName).delete()
    new File(existingDir).delete()
    new File(fileName).delete()
  }

  private def file2path(file: File) = Paths.get(file.toURI)

  before(removeAll())
  after(removeAll())

  "Recorder Configuration" should "create file if directory exists" in {
    val resFile = RecorderConfiguration.createAndOpen(file2path(new File(fileName)))
    Files.exists(resFile) shouldBe true
  }

  it should "create if parent directory exists" in {
    new File(existingDir).mkdir()
    val testFile = new File(existingDir, fileName)
    val resFile = RecorderConfiguration.createAndOpen(file2path(testFile))
    Files.exists(resFile) shouldBe true
  }

  it should "throw exception if parent directory is missing" in {
    val testFile = new File(nonExistingDir, fileName)
    a[FileNotFoundException] should be thrownBy RecorderConfiguration.createAndOpen(file2path(testFile))
  }
}
