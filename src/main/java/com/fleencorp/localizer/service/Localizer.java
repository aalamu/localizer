package com.fleencorp.localizer.service;

import com.fleencorp.localizer.model.response.LocalizedResponse;

import java.util.Locale;
import java.util.function.Supplier;

public interface Localizer {

  String getMessage(String key, Locale locale, Object...params);

  String getMessage(String key, Object...params);

  <T extends LocalizedResponse> T of(T response);

  <T extends LocalizedResponse> T of(T response, String messageCode);

  <T extends LocalizedResponse> Supplier<T> of(Supplier<T> responseSupplier);

  String of(String messageCode);

}
