package com.fleencorp.localizer;

import com.fleencorp.localizer.model.response.ErrorResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ErrorResponseTest {

  @Test
  @DisplayName("An error is stamped with the moment it was built, an instant to the microsecond")
  void the_timestamp_is_an_instant_to_the_microsecond() {
    final Instant before = Instant.now().minusSeconds(1);

    final ErrorResponse error = ErrorResponse.of();

    final Instant stamped = error.getTimestamp();
    assertNotNull(stamped);
    assertFalse(stamped.isBefore(before));
    assertTrue(stamped.isBefore(Instant.now().plusSeconds(1)));
    assertEquals(0, stamped.getNano() % 1_000, "sub-microsecond digits: " + stamped);
  }

}
