package com.fleencorp.localizer.model.response;

public abstract class ApiResponse {

  abstract public String getMessageCode();

  public Object[] getParams() {
    return new Object[] {};
  }

  protected String message;

  public String getMessage() {
    return message;
  }

  public void setMessage(final String message) {
    this.message = message;
  }
}
