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

package io.gatling.javaapi.core.condition;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.gatling.javaapi.core.Session;
import io.gatling.javaapi.core.StructureBuilder;
import io.gatling.javaapi.core.exec.Executable;
import io.gatling.javaapi.core.internal.Executables;
import io.gatling.javaapi.core.internal.condition.ScalaDoIfEqualsOrElse;
import java.util.function.Function;

/**
 * Methods for defining "doIfEqualsOrElse" conditional blocks.
 *
 * <p>Important: instances are immutable so any method doesn't mutate the existing instance but
 * returns a new one.
 *
 * @param <T> the type of {@link StructureBuilder} to attach to and to return
 * @param <W> the type of wrapped Scala instance
 */
public interface DoIfEqualsOrElse<
    T extends StructureBuilder<T, W>, W extends io.gatling.core.structure.StructureBuilder<W>> {

  T make(Function<W, W> f);

  // Gatling EL actual
  /**
   * Execute the "then" block only if the actual value is equal to the expected one, otherwise
   * execute the "else" block.
   *
   * @param actual the actual value expressed as a Gatling Expression Language String
   * @param expected the expected value expressed as a Gatling Expression Language String
   * @return a DSL component for defining the "then" block
   */
  @NonNull
  default Then<T> doIfEqualsOrElse(@NonNull String actual, @NonNull String expected) {
    return new Then<>(ScalaDoIfEqualsOrElse.apply(this, actual, expected));
  }

  /**
   * Execute the "then" block only if the actual value is equal to the expected one, otherwise
   * execute the "else" block.
   *
   * @param actual the actual value expressed as a Gatling Expression Language String
   * @param expected the expected static value
   * @return a DSL component for defining the "then" block
   */
  @NonNull
  default Then<T> doIfEqualsOrElse(@NonNull String actual, @NonNull Object expected) {
    return new Then<>(ScalaDoIfEqualsOrElse.apply(this, actual, expected));
  }

  /**
   * Execute the "then" block only if the actual value is equal to the expected one, otherwise
   * execute the "else" block.
   *
   * @param actual the actual value expressed as a Gatling Expression Language String
   * @param expected the expected value expressed as a function
   * @return a DSL component for defining the "then" block
   */
  @NonNull
  default Then<T> doIfEqualsOrElse(
      @NonNull String actual, @NonNull Function<Session, Object> expected) {
    return new Then<>(ScalaDoIfEqualsOrElse.apply(this, actual, expected));
  }

  // Function actual
  /**
   * Execute the "then" block only if the actual value is equal to the expected one, otherwise
   * execute the "else" block.
   *
   * @param actual the actual value expressed as a function
   * @param expected the expected value expressed as a Gatling Expression Language String
   * @return a DSL component for defining the "then" block
   */
  @NonNull
  default Then<T> doIfEqualsOrElse(
      @NonNull Function<Session, Object> actual, @NonNull String expected) {
    return new Then<>(ScalaDoIfEqualsOrElse.apply(this, actual, expected));
  }

  /**
   * Execute the "then" block only if the actual value is equal to the expected one, otherwise
   * execute the "else" block.
   *
   * @param actual the actual value expressed as a function
   * @param expected the expected static value
   * @return a DSL component for defining the "then" block
   */
  @NonNull
  default Then<T> doIfEqualsOrElse(
      @NonNull Function<Session, Object> actual, @NonNull Object expected) {
    return new Then<>(ScalaDoIfEqualsOrElse.apply(this, actual, expected));
  }

  /**
   * Execute the "then" block only if the actual value is equal to the expected one, otherwise
   * execute the "else" block.
   *
   * @param actual the actual value expressed as a function
   * @param expected the expected value expressed as a function
   * @return a DSL component for defining the "then" block
   */
  @NonNull
  default Then<T> doIfEqualsOrElse(
      @NonNull Function<Session, Object> actual, @NonNull Function<Session, Object> expected) {
    return new Then<>(ScalaDoIfEqualsOrElse.apply(this, actual, expected));
  }

  /**
   * The DSL component for defining the "then" block
   *
   * @param <T> the type of {@link StructureBuilder} to attach to and to return
   */
  final class Then<T extends StructureBuilder<T, ?>> {
    private final ScalaDoIfEqualsOrElse.Then<T, ?> wrapped;

    Then(ScalaDoIfEqualsOrElse.Then<T, ?> wrapped) {
      this.wrapped = wrapped;
    }

    /**
     * Define the chain to be executed when the actual and expected values are equal
     *
     * @param executable the "then "chain
     * @param executables other chains
     * @return the DSL component for defining the "else" block
     */
    @NonNull
    public OrElse<T> then(@NonNull Executable executable, @NonNull Executable... executables) {
      return new OrElse<>(wrapped.then_(Executables.toChainBuilder(executable, executables)));
    }
  }

  /**
   * The DSL component for defining the "else" block
   *
   * @param <T> the type of {@link StructureBuilder} to attach to and to return
   */
  final class OrElse<T extends StructureBuilder<T, ?>> {
    private final ScalaDoIfEqualsOrElse.OrElse<T, ?> wrapped;

    OrElse(ScalaDoIfEqualsOrElse.OrElse<T, ?> wrapped) {
      this.wrapped = wrapped;
    }

    /**
     * Define the chain to be executed when the actual and expected values are not equal
     *
     * @param executable the "then "chain
     * @param executables other chains
     * @return a new {@link StructureBuilder}
     */
    @NonNull
    public T orElse(@NonNull Executable executable, @NonNull Executable... executables) {
      return wrapped.orElse(Executables.toChainBuilder(executable, executables));
    }
  }
}
