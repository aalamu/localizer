package com.fleencorp.localizer.model.exception;

import java.util.HashMap;
import java.util.Map;

public class LocalizedException extends RuntimeException {

  protected String message = "";
  protected transient Object[] params = new Object[] {};

  public LocalizedException(final Object...params) {
    super();
    this.params = params;
  }

  public LocalizedException() {
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
