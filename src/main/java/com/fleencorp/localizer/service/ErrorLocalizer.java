package com.fleencorp.localizer.service;

import com.fleencorp.localizer.model.exception.ApiException;
import com.fleencorp.localizer.model.response.ErrorResponse;
import jakarta.ws.rs.core.Response;

public interface ErrorLocalizer extends Localizer {

  <T extends ApiException> T of(T ex);

  <T extends ApiException> ErrorResponse withStatus(T ex, Response.Status status);
}
