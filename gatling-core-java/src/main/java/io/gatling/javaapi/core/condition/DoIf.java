/*
 * Copyright 2011-2024 GatlingCorp (https://gatling.io)
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

package io.gatling.javaapi.core.condition;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.gatling.javaapi.core.Session;
import io.gatling.javaapi.core.StructureBuilder;
import io.gatling.javaapi.core.exec.Executable;
import io.gatling.javaapi.core.internal.Executables;
import io.gatling.javaapi.core.internal.condition.ScalaDoIf;
import java.util.function.Function;

/**
 * Methods for defining "doIf" conditional blocks.
 *
 * <p>Important: instances are immutable so any method doesn't mutate the existing instance but
 * returns a new one.
 *
 * @param <T> the type of {@link StructureBuilder} to attach to and to return
 * @param <W> the type of wrapped Scala instance
 */
public interface DoIf<
    T extends StructureBuilder<T, W>, W extends io.gatling.core.structure.StructureBuilder<W>> {

  T make(Function<W, W> f);

  /**
   * Execute the "then" block only if the condition is true
   *
   * @param condition the condition expressed as a Gatling Expression Language String that must
   *     evaluate to a Boolean
   * @return a DSL component for defining the "then" block
   */
  @NonNull
  default Then<T> doIf(@NonNull String condition) {
    return new Then<>(ScalaDoIf.apply(this, condition));
  }

  /**
   * Execute the "then" block only if the condition is true
   *
   * @param condition the condition expressed as function
   * @return a DSL component for defining the "then" block
   */
  @NonNull
  default Then<T> doIf(@NonNull Function<Session, Boolean> condition) {
    return new Then<>(ScalaDoIf.apply(this, condition));
  }

  /**
   * The DSL component for defining the "then" block
   *
   * @param <T> the type of {@link StructureBuilder} to attach to and to return
   */
  final class Then<T extends StructureBuilder<T, ?>> {
    private final ScalaDoIf.Then<T, ?> wrapped;

    Then(ScalaDoIf.Then<T, ?> wrapped) {
      this.wrapped = wrapped;
    }

    /**
     * Define the chain to be executed when the condition is true
     *
     * @param executable the chain
     * @param executables other chains
     * @return a new {@link StructureBuilder}
     */
    @NonNull
    public T then(@NonNull Executable executable, @NonNull Executable... executables) {
      return wrapped.then_(Executables.toChainBuilder(executable, executables));
    }
  }
}
