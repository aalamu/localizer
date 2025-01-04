package com.fleencorp.localizer.service.adapter;

import com.fleencorp.localizer.model.exception.ApiException;
import com.fleencorp.localizer.model.response.ApiResponse;
import com.fleencorp.localizer.model.response.ErrorResponse;
import com.fleencorp.localizer.service.Localizer;
import jakarta.ws.rs.core.Response;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;
import java.util.function.Supplier;

import static java.util.Objects.nonNull;

/**
 * A class that implements the {@link Localizer} interface, serving as an adapter for localization functionality.
 * This class provides a bridge to localize messages or data, depending on the specific implementation of {@link Localizer}.
 * It can be extended or customized to meet the specific needs of the application while conforming to the {@link Localizer} interface.
 *
 * @author Yusuf Àlàmu Musa
 * @version 1.0
 */
public class LocalizerAdapter implements Localizer {

  private final MessageSource messageSource;
  private final MessageSource responseMessageSource;
  private final MessageSource errorMessageSource;

  public LocalizerAdapter(
      final MessageSource messageSource,
      final MessageSource responseMessageSource,
      final MessageSource errorMessageSource) {
    this.messageSource = messageSource;
    this.responseMessageSource = responseMessageSource;
    this.errorMessageSource = errorMessageSource;
  }

  /**
   * Retrieves a message from the message source, resolving it based on the provided key, locale, and optional parameters.
   *
   * @param key the message key to retrieve the message for
   * @param locale the locale to resolve the message for
   * @param params optional parameters to be used within the message if applicable
   * @return the resolved message as a {@link String}
   */
  public String getMessage(final String key, final Locale locale, final Object...params) {
    return messageSource.getMessage(key, params, locale);
  }

  /**
   * Retrieves a response message from the response message source, resolving it based on the provided key, locale, and optional parameters.
   *
   * @param key the message key to retrieve the response message for
   * @param locale the locale to resolve the message for
   * @param params optional parameters to be used within the message if applicable
   * @return the resolved response message as a {@link String}
   */
  public String getMessageRes(final String key, final Locale locale, final Object...params) {
    return responseMessageSource.getMessage(key, params, locale);
  }

  /**
   * Retrieves an error message from the error message source, resolving it based on the provided key, locale, and optional parameters.
   *
   * @param key the message key to retrieve the error message for
   * @param locale the locale to resolve the message for
   * @param params optional parameters to be used within the message if applicable
   * @return the resolved error message as a {@link String}
   */
  public String getMessageEx(final String key, final Locale locale, final Object...params) {
    return errorMessageSource.getMessage(key, params, locale);
  }

  /**
   * Retrieves a message from the message source, resolving it based on the provided key and optional parameters,
   * using the current locale from the {@link LocaleContextHolder}.
   *
   * @param key the message key to retrieve the message for
   * @param params optional parameters to be used within the message if applicable
   * @return the resolved message as a {@link String}
   */
  public String getMessage(final String key, final Object...params) {
    return getMessage(key, LocaleContextHolder.getLocale(), params);
  }

  /**
   * Retrieves a response message from the response message source, resolving it based on the provided key and optional parameters,
   * using the current locale from the {@link LocaleContextHolder}.
   *
   * @param key the message key to retrieve the response message for
   * @param params optional parameters to be used within the message if applicable
   * @return the resolved response message as a {@link String}
   */
  public String getMessageRes(final String key, final Object...params) {
    return getMessageRes(key, LocaleContextHolder.getLocale(), params);
  }

  /**
   * Retrieves an error message from the error message source, resolving it based on the provided key and optional parameters,
   * using the current locale from the {@link LocaleContextHolder}.
   *
   * @param key the message key to retrieve the error message for
   * @param params optional parameters to be used within the message if applicable
   * @return the resolved error message as a {@link String}
   */
  public String getMessageEx(final String key, final Object...params) {
    return getMessageEx(key, LocaleContextHolder.getLocale(), params);
  }

  /**
   * Sets the message in the provided {@link ApiResponse} object based on its message code.
   * If the response and its message code are not null, the method retrieves the corresponding message
   * from the response message source and sets it in the response.
   *
   * @param <T> the type of {@link ApiResponse}
   * @param response the {@link ApiResponse} object to set the message for
   * @return the updated {@link ApiResponse} with the resolved message, or {@code null} if the response or message code is {@code null}
   */
  public <T extends ApiResponse> T of(final T response) {
    if (nonNull(response) && nonNull(response.getMessageCode())) {
      final String message = getMessageRes(response.getMessageCode());
      response.setMessage(message);
      return response;
    }
    return null;
  }

  /**
   * Sets the message in the provided {@link ApiResponse} object based on the provided message code.
   * If the response and message code are not null, the method retrieves the corresponding message
   * from the response message source using the provided message code and the response's parameters,
   * then sets the message in the response.
   *
   * @param <T> the type of {@link ApiResponse}
   * @param response the {@link ApiResponse} object to set the message for
   * @param messageCode the message code to resolve the message
   * @return the updated {@link ApiResponse} with the resolved message, or {@code null} if the response or message code is {@code null}
   */
  public <T extends ApiResponse> T of(final T response, final String messageCode) {
    if (nonNull(response) && nonNull(messageCode)) {
      final String message = getMessageRes(messageCode, response.getParams());
      response.setMessage(message);
      return response;
    }
    return null;
  }

  /**
   * Retrieves the message for the given message code from the response message source.
   * If the message code is not null, the method resolves the corresponding message.
   *
   * @param messageCode the message code to retrieve the message for
   * @return the resolved message as a {@link String}, or {@code null} if the message code is {@code null}
   */
  public String of(final String messageCode) {
    if (nonNull(messageCode)) {
      return getMessageRes(messageCode);
    }
    return null;
  }

  /**
   * Returns a {@link Supplier} that provides an {@link ApiResponse} with its message set, based on its message code.
   * If the supplied {@link ApiResponse} is not null and has a valid message code, the method retrieves the corresponding
   * message and sets it in the response. If the response or message code is null, the method returns null.
   *
   * @param <T> the type of {@link ApiResponse}
   * @param responseSupplier the {@link Supplier} that provides the {@link ApiResponse}
   * @return a {@link Supplier} that provides the {@link ApiResponse} with the resolved message, or {@code null} if the response or message code is {@code null}
   */
  public <T extends ApiResponse> Supplier<T> of(final Supplier<T> responseSupplier) {
    return () -> {
      if (nonNull(responseSupplier) && nonNull(responseSupplier.get())) {
        final T response = responseSupplier.get();

        if (nonNull(response) && nonNull(response.getMessageCode())) {
          final String message = getMessageRes(response.getMessageCode());
          response.setMessage(message);
        }

        return response;
      }
      return null;
    };
  }

  /**
   * Sets the error message in the provided {@link ApiException} object based on its message code.
   * If the exception and its message code are not null, the method retrieves the corresponding error message
   * from the error message source and sets it in the exception.
   *
   * @param <T> the type of {@link ApiException}
   * @param ex the {@link ApiException} object to set the error message for
   * @return the updated {@link ApiException} with the resolved error message, or {@code null} if the exception or message code is {@code null}
   */
  public <T extends ApiException> T of(final T ex) {
    if (nonNull(ex) && nonNull(ex.getMessageCode())) {
      final String message = getMessageEx(ex.getMessageCode(), ex.getParams());
      ex.setMessage(message);
      return ex;
    }
    return null;
  }

  /**
   * Retrieves the error message for the provided {@link ApiException} based on its message code,
   * using the default locale.
   * If the exception and its message code are not null, the method retrieves the corresponding error message
   * from the error message source.
   *
   * @param <T> the type of {@link ApiException}
   * @param ex the {@link ApiException} object to retrieve the error message for
   * @return the resolved error message as a {@link String}, or {@code null} if the exception or message code is {@code null}
   */
  public <T extends ApiException> String getExMessage(final T ex) {
    if (nonNull(ex) && nonNull(ex.getMessageCode())) {
      return getMessageEx(ex.getMessageCode(), Locale.getDefault());
    }
    return null;
  }

  /**
   * Creates an {@link ErrorResponse} based on the provided {@link ApiException} and HTTP status.
   * If the exception and its message code are not null, the method retrieves the corresponding error message
   * and creates an {@link ErrorResponse} with the message, status, and additional details from the exception.
   *
   * @param <T> the type of {@link ApiException}
   * @param ex the {@link ApiException} object to create the error response for
   * @param status the HTTP status to associate with the error response
   * @return an {@link ErrorResponse} with the resolved error message and status, or a default {@link ErrorResponse} if the exception or message code is {@code null}
   */
  @Override
  public <T extends ApiException> ErrorResponse withStatus(final T ex, final Response.Status status) {
    if (nonNull(ex) && nonNull(ex.getMessageCode())) {
      final String message = getMessageEx(ex.getMessageCode(), ex.getParams());
      return ErrorResponse.of(message, status, ex.getDetails());
    }
    return ErrorResponse.of();
  }

}
