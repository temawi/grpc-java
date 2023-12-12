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

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Unit tests for {@link StatusOr}.
 */
@RunWith(JUnit4.class)
public class StatusOrTest {

  StatusOr<String> testResult = StatusOr.fromResult("test");
  StatusOr<String> alsoTestResult = StatusOr.fromResult("test");
  StatusOr<String> anotherResult = StatusOr.fromResult("another result");
  StatusOr<?> internalError = StatusOr.fromStatus(Status.INTERNAL);
  StatusOr<?> alsoInternalError = StatusOr.fromStatus(Status.INTERNAL);
  StatusOr<?> anotherError = StatusOr.fromStatus(Status.CANCELLED);

  @Test
  public void fromStatus() {
    StatusOr<?> statusOr = StatusOr.fromStatus(Status.INTERNAL);
    assertThat(statusOr.getStatus()).isEqualTo(Status.INTERNAL);
  }

  @Test
  public void fromResult() {
    StatusOr<String> statusOr = StatusOr.fromResult("test");
    assertThat(statusOr.getResult()).isEqualTo("test");
  }

  @Test
  public void ok() {
    assertThat(StatusOr.fromResult("test").isOk()).isTrue();
    assertThat(StatusOr.fromStatus(Status.INTERNAL).isOk()).isFalse();
  }

  @Test
  @SuppressWarnings("TruthIncompatibleType")
  public void testEquals() {
    assertThat(testResult).isNotEqualTo("foo");
    assertThat(testResult).isEqualTo(alsoTestResult);
    assertThat(testResult).isNotEqualTo(anotherResult);
    assertThat(testResult).isNotEqualTo(internalError);
    assertThat(internalError).isEqualTo(alsoInternalError);
    assertThat(internalError).isNotEqualTo(anotherError);
    assertThat(internalError).isNotEqualTo(testResult);
  }

  @Test
  public void testHashCode() {
    assertThat(testResult.hashCode()).isNotEqualTo("foo".hashCode());
    assertThat(testResult.hashCode()).isEqualTo(alsoTestResult.hashCode());
    assertThat(testResult.hashCode()).isNotEqualTo(anotherResult.hashCode());
    assertThat(testResult.hashCode()).isNotEqualTo(internalError.hashCode());
    assertThat(internalError.hashCode()).isEqualTo(alsoInternalError.hashCode());
    assertThat(internalError.hashCode()).isNotEqualTo(anotherError.hashCode());
  }

  @Test
  public void testToString() {
    assertThat(testResult.toString()).isEqualTo("StatusOr{result=test}");
    assertThat(internalError.toString()).isEqualTo(
        "StatusOr{status=Status{code=INTERNAL, description=null, cause=null}}");
  }
}