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

package io.gatling.javaapi.jms;

import edu.umd.cs.findbugs.annotations.NonNull;
import jakarta.jms.JMSException;
import jakarta.jms.Message;

/**
 * A component in charge of preparing outbound messages and providing the matching strategy with
 * inbound ones.
 */
public interface JmsMessageMatcher {

  /**
   * Prepare the request outbound message and set some correlation id.
   *
   * @param msg the outbound message
   * @throws JMSException
   */
  void prepareRequest(@NonNull Message msg) throws JMSException;

  /**
   * Extract the matchId out of a request outbound message
   *
   * @param msg the message
   * @return the matchId
   * @throws JMSException
   */
  @NonNull
  String requestMatchId(@NonNull Message msg) throws JMSException;

  /**
   * Extract the matchId out of a response inbound message
   *
   * @param msg the message
   * @return the matchId
   * @throws JMSException
   */
  @NonNull
  String responseMatchId(@NonNull Message msg) throws JMSException;
}
