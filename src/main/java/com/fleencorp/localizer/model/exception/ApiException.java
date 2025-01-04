package com.fleencorp.localizer.model.exception;

import java.util.HashMap;
import java.util.Map;

public class ApiException extends RuntimeException {

  protected String message = "";
  protected Object[] params = new Object[] {};

  public ApiException(final Object...params) {
    super();
    this.params = params;
  }

  public ApiException() {
    super();
  }

  public String getMessageCode() {
    return "";
  }

  public Object[] getParams() {
    return params;
  }

  public Map<String, Object> getDetails() {
    return new HashMap<>();
  }

  @Override
  public String getMessage() {
    return message;
  }

  public void setMessage(final String message) {
    this.message = message;
  }
}
