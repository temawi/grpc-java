/*
 * Copyright 2023 The gRPC Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.grpc;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * An "abseil-like" generic return type that either contains a {@link Status} if an operation has
 * failed or a {@code result} of some type if successful.
 *
 * <p>Will only ever contain either a {@link Status} or a result, never both or neither.
 */
@ExperimentalApi("") // TODO: Create an issue
public final class StatusOr<T> {

  private final Status status;
  private final T result;

  /**
   * Creates a {@code StatusOr} with a {@link Status} that represents an error condition.
   *
   * <p>As this method should only be used in error situations, the {@code OK Status} code should
   * not be used.
   *
   * @throws IllegalArgumentException if an {@code OK Status} code is provided.
   */
  public static StatusOr<?> fromStatus(Status status) {
    return new StatusOr<>(status);
  }

  /**
   * Creates a {@code StatusOr} with a successfully produced {@code result}.
   */
  public static <T> StatusOr<T> fromResult(T result) {
    return new StatusOr<>(result);
  }

  /**
   * Returns the {@link Status} that specifies why an operation has failed.
   */
  public Status getStatus() {
    return status;
  }

  /**
   * Return the result of a successful operation.
   */
  public T getResult() {
    return result;
  }

  /**
   * A convenience method to see if an operation was successful.
   *
   * @return {@code true} if there is a result.
   */
  public boolean isOk() {
    return result != null;
  }

  private StatusOr(Status status) {
    checkNotNull(status);
    // We use Status to only represent error conditions so OK is not allowed.
    checkArgument(!status.isOk());
    this.status = status;
    this.result = null;
  }

  private StatusOr(T result) {
    this.result = checkNotNull(result);
    this.status = null;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof StatusOr)) {
      return false;
    }
    StatusOr<?> statusOr = (StatusOr<?>) o;
    return Objects.equal(status, statusOr.status) && Objects.equal(result, statusOr.result);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(status, result);
  }

  @Override
  public String toString() {
    if (status != null) {
      return MoreObjects.toStringHelper(this).add("status", status).toString();
    } else if (result != null) {
      return MoreObjects.toStringHelper(this).add("result", result).toString();
    }
    throw new AssertionError("neither status nor result set");
  }


}
