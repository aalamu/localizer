package com.fleencorp.localizer.service;

import com.fleencorp.localizer.model.exception.LocalizedException;
import com.fleencorp.localizer.model.response.ErrorResponse;
import jakarta.ws.rs.core.Response;

public interface ErrorLocalizer extends Localizer {

  <T extends LocalizedException> T of(T ex);

  <T extends LocalizedException> ErrorResponse withStatus(T ex, Response.Status status);
}
