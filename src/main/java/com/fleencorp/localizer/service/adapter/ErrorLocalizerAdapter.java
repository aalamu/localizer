package com.fleencorp.localizer.service.adapter;

import com.fleencorp.localizer.model.exception.LocalizedException;
import com.fleencorp.localizer.model.response.ErrorResponse;
import com.fleencorp.localizer.service.ErrorLocalizer;
import jakarta.ws.rs.core.Response;
import org.springframework.context.MessageSource;

import static java.util.Objects.nonNull;

/**
 * A class that implements the {@link ErrorLocalizer} interface, serving as an adapter for localization functionality.
 * This class provides a bridge to localize messages or data, depending on the specific implementation of {@link ErrorLocalizer}.
 * It can be extended or customized to meet the specific needs of the application while conforming to the {@link ErrorLocalizer} interface.
 *
 * @author Yusuf Àlàmu Musa
 * @version 1.0
 */
public class ErrorLocalizerAdapter extends LocalizerAdapter implements ErrorLocalizer {

  public ErrorLocalizerAdapter(final MessageSource messageSource) {
    super(messageSource);
  }

  /**
   * Sets the error message in the provided {@link LocalizedException} object based on its message code.
   * If the exception and its message code are not null, the method retrieves the corresponding error message
   * from the error message source and sets it in the exception.
   *
   * @param <T> the type of {@link LocalizedException}
   * @param ex the {@link LocalizedException} object to set the error message for
   * @return the updated {@link LocalizedException} with the resolved error message, or {@code null} if the exception or message code is {@code null}
   */
  public <T extends LocalizedException> T of(final T ex) {
    if (nonNull(ex) && nonNull(ex.getMessageCode())) {
      final String message = getMessage(ex.getMessageCode(), ex.getParams());
      ex.setMessage(message);
    }
    return ex;
  }

  /**
   * Creates an {@link ErrorResponse} based on the provided {@link LocalizedException} and HTTP status.
   * If the exception and its message code are not null, the method retrieves the corresponding error message
   * and creates an {@link ErrorResponse} with the message, status, and additional details from the exception.
   *
   * @param <T> the type of {@link LocalizedException}
   * @param ex the {@link LocalizedException} object to create the error response for
   * @param status the HTTP status to associate with the error response
   * @return an {@link ErrorResponse} with the resolved error message and status, or a default {@link ErrorResponse} if the exception or message code is {@code null}
   */
  @Override
  public <T extends LocalizedException> ErrorResponse withStatus(final T ex, final Response.Status status) {
    if (nonNull(ex) && nonNull(ex.getMessageCode())) {
      final String message = getMessage(ex.getMessageCode(), ex.getParams());
      return ErrorResponse.of(message, status, ex.getDetails());
    }
    return ErrorResponse.of();
  }

  /**
   * Builds an {@link ErrorResponse} using the given message code and status.
   * If the provided message code is not null and resolvable, a localized message is
   * retrieved and included in the response along with any available exception details.
   * If no valid message code is provided, a default empty {@code ErrorResponse} is returned.
   *
   * @param messageCode the message key used to resolve a localized error message
   * @param status the response status to associate with the error
   * @param <T> the type of {@link LocalizedException} associated with the error
   * @return an {@code ErrorResponse} containing the resolved message and details if available,
   *         or a default empty error response if not
   */
  public <T extends LocalizedException> ErrorResponse withStatus(final String messageCode, final Response.Status status) {
    if (nonNull(ex) && nonNull(ex.getMessageCode())) {
      final String message = getMessage(messageCode);
      return ErrorResponse.of(message, status, ex.getDetails());
    }
    return ErrorResponse.of();
  }
}
