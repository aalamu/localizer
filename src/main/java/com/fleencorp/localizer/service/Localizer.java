package com.fleencorp.localizer.service;

import com.fleencorp.localizer.model.exception.ApiException;
import com.fleencorp.localizer.model.response.ApiResponse;
import com.fleencorp.localizer.model.response.ErrorResponse;
import jakarta.ws.rs.core.Response;

import java.util.Locale;
import java.util.function.Supplier;

public interface Localizer {

  String getMessage(String key, Locale locale, Object...params);

  String getMessage(String key, Object...params);

  <T extends ApiResponse> T of(T response);

  <T extends ApiResponse> T of(T response, String messageCode);

  <T extends ApiResponse> Supplier<T> of(Supplier<T> responseSupplier);

  String of(String messageCode);

}
