package com.fleencorp.localizer.service;

import com.fleencorp.localizer.model.response.ApiResponse;
import com.fleencorp.localizer.model.exception.ApiException;
import com.fleencorp.localizer.model.response.ErrorResponse;
import jakarta.ws.rs.core.Response;

import java.util.Locale;
import java.util.function.Supplier;

public interface Localizer {

  String getMessage(final String key, final Locale locale, final Object...params);

  String getMessageRes(final String key, final Locale locale, final Object...params);

  String getMessageEx(final String key, final Locale locale, final Object...params);

  String getMessage(final String key, final Object...params);

  String getMessageRes(final String key, final Object...params);

  String getMessageEx(final String key, final Object...params);

  <T extends ApiResponse> T of(final T response);

  <T extends ApiResponse> T of(final T response, final String messageCode);

  String of(final String messageCode);

  <T extends ApiResponse> Supplier<T> of(final Supplier<T> responseSupplier);

  <T extends ApiException> T of(final T ex);

  <T extends ApiException> String getExMessage(final T ex);

  <T extends ApiException> ErrorResponse withStatus(T ex, Response.Status status);
}
