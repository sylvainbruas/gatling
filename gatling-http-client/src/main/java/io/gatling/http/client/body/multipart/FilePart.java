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

package io.gatling.http.client.body.multipart;

import io.gatling.http.client.Param;
import io.gatling.http.client.body.multipart.impl.FilePartImpl;
import io.gatling.http.client.body.multipart.impl.PartImpl;
import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

public final class FilePart extends FileLikePart<File> {

  public FilePart(
      String name,
      File content,
      Charset charset,
      String transferEncoding,
      String contentId,
      String dispositionType,
      String contentType,
      List<Param> customHeaders,
      String fileName) {
    super(
        name,
        content,
        charset,
        transferEncoding,
        contentId,
        dispositionType,
        contentType,
        customHeaders,
        fileName);
    if (!content.exists()) {
      throw new IllegalArgumentException("File part doesn't exist: " + content.getAbsolutePath());
    } else if (!content.canRead()) {
      throw new IllegalArgumentException("File part can't be read: " + content.getAbsolutePath());
    }
  }

  @Override
  public PartImpl toImpl(byte[] boundary) {
    return new FilePartImpl(this, boundary);
  }
}
