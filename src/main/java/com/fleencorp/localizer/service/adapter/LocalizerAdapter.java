package com.fleencorp.localizer.service.adapter;

import com.fleencorp.localizer.model.response.LocalizedResponse;
import com.fleencorp.localizer.service.Localizer;
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

  public LocalizerAdapter(final MessageSource messageSource) {
    this.messageSource = messageSource;
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
   * Sets the message in the provided {@link LocalizedResponse} object based on its message code.
   * If the response and its message code are not null, the method retrieves the corresponding message
   * from the message source and sets it in the response.
   *
   * @param <T> the type of {@link LocalizedResponse}
   * @param response the {@link LocalizedResponse} object to set the message for
   * @return the updated {@link LocalizedResponse} with the resolved message, or {@code null} if the response or message code is {@code null}
   */
  public <T extends LocalizedResponse> T of(final T response) {
    if (nonNull(response) && nonNull(response.getMessageCode())) {
      final String message = getMessage(response.getMessageCode(), response.getParams());
      response.setMessage(message);
    }
    return response;
  }

  /**
   * Sets the message in the provided {@link LocalizedResponse} object based on the provided message code.
   * If the response and message code are not null, the method retrieves the corresponding message
   * from the message source using the provided message code and the response's parameters,
   * then sets the message in the response.
   *
   * @param <T> the type of {@link LocalizedResponse}
   * @param response the {@link LocalizedResponse} object to set the message for
   * @param messageCode the message code to resolve the message
   * @return the updated {@link LocalizedResponse} with the resolved message, or {@code null} if the response or message code is {@code null}
   */
  public <T extends LocalizedResponse> T of(final T response, final String messageCode) {
    if (nonNull(response) && nonNull(messageCode)) {
      final String message = getMessage(messageCode, response.getParams());
      response.setMessage(message);
    }
    return response;
  }

  /**
   * Retrieves the message for the given message code from the message source.
   * If the message code is not null, the method resolves the corresponding message.
   *
   * @param messageCode the message code to retrieve the message for
   * @return the resolved message as a {@link String}, or {@code null} if the message code is {@code null}
   */
  public String of(final String messageCode) {
    if (nonNull(messageCode)) {
      return getMessage(messageCode);
    }
    return null;
  }

  /**
   * Returns a {@link Supplier} that provides an {@link LocalizedResponse} with its message set, based on its message code.
   * If the supplied {@link LocalizedResponse} is not null and has a valid message code, the method retrieves the corresponding
   * message and sets it in the response. If the response or message code is null, the method returns null.
   *
   * @param <T> the type of {@link LocalizedResponse}
   * @param responseSupplier the {@link Supplier} that provides the {@link LocalizedResponse}
   * @return a {@link Supplier} that provides the {@link LocalizedResponse} with the resolved message, or {@code null} if the response or message code is {@code null}
   */
  public <T extends LocalizedResponse> Supplier<T> of(final Supplier<T> responseSupplier) {
    return () -> {
      if (nonNull(responseSupplier) && nonNull(responseSupplier.get())) {
        final T response = responseSupplier.get();

        if (nonNull(response) && nonNull(response.getMessageCode())) {
          final String message = getMessage(response.getMessageCode());
          response.setMessage(message);
        }

        return response;
      }
      return null;
    };
  }

}
