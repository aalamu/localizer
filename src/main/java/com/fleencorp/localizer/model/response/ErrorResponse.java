package com.fleencorp.localizer.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.ws.rs.core.Response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ErrorResponse {

  private static final String DEFAULT_MESSAGE = "An error has occurred";

  private String message;
  private String reason;
  private Object status;
  private LocalDateTime timestamp;
  private List<Map<String, Object>> fieldErrors = new ArrayList<>();
  private Map<String, Object> details = new HashMap<>();

  @JsonProperty("message")
  public String getMessage() {
    return message;
  }

  public void setMessage(final String message) {
    this.message = message;
  }

  @JsonProperty("reason")
  public String getReason() {
    return reason;
  }

  public void setReason(final String reason) {
    this.reason = reason;
  }

  @JsonProperty("status")
  public Object getStatus() {
    return status;
  }

  public void setStatus(final Object status) {
    this.status = status;
  }

  @JsonProperty("timestamp")
  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(final LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public List<Map<String, Object>> getFieldErrors() {
    return fieldErrors;
  }

  public void setFieldErrors(final List<Map<String, Object>> fieldErrors) {
    this.fieldErrors = fieldErrors;
  }

  @JsonProperty("details")
  public Map<String, Object> getDetails() {
    return details;
  }

  public void setDetails(final Map<String, Object> details) {
    this.details = details;
  }

  /**
   * Creates an {@link ErrorResponse} with the specified message and HTTP status.
   *
   * <p>This static method constructs an {@link ErrorResponse} using the provided message and HTTP status,
   * setting the status code, reason phrase, and the current timestamp.</p>
   *
   * @param message the error message to be included in the response.
   * @param status  the HTTP status to be included in the response.
   * @param details other details that might explain the error and an action to do
   * @return a new {@link ErrorResponse} instance with the specified message, status, reason, and timestamp.
   */
  public static ErrorResponse of(final String message, final Response.Status status, final Map<String, Object> details) {
    final ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setMessage(message);
    errorResponse.setReason(status.getReasonPhrase());
    errorResponse.setStatus(status.getStatusCode());
    errorResponse.setTimestamp(LocalDateTime.now());
    errorResponse.setDetails(details);

    return errorResponse;
  }

  /**
   * Creates an {@link ErrorResponse} with the specified message, HTTP status, and field-specific error details.
   *
   * <p>This static method constructs an {@link ErrorResponse} using the provided message, HTTP status, and
   * a list of field-specific error details. The response includes the status code, reason phrase, current timestamp,
   * and sets the field errors and default error type.</p>
   *
   * @param message       the error message to be included in the response.
   * @param status        the HTTP status to be included in the response.
   * @param fieldErrors   a list of field-specific error details to be included in the response.
   * @return a new {@link ErrorResponse} instance with the specified message, status, reason, timestamp, field errors,
   *         and default error type.
   */
  public static ErrorResponse of(final String message, final Response.Status status, final List<Map<String, Object>> fieldErrors) {
    final ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setMessage(message);
    errorResponse.setReason(status.getReasonPhrase());
    errorResponse.setStatus(status.getStatusCode());
    errorResponse.setTimestamp(LocalDateTime.now());
    errorResponse.setFieldErrors(fieldErrors);
    errorResponse.setDetails(null);

    return errorResponse;
  }

  /**
   * Creates a default {@link ErrorResponse} with a default message.
   *
   * <p>This static method constructs an {@link ErrorResponse} with a default error message and the current timestamp.</p>
   *
   * @return a new {@link ErrorResponse} instance with a default message and timestamp.
   */
  public static ErrorResponse of() {
    final ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setMessage(DEFAULT_MESSAGE);
    errorResponse.setTimestamp(LocalDateTime.now());

    return errorResponse;
  }
}
