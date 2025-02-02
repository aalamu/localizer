package com.fleencorp.localizer.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class ApiResponse {

  protected String message;

  @JsonIgnore
  public abstract String getMessageCode();

  @JsonIgnore
  public Object[] getParams() {
    return new Object[] {};
  }

  @JsonProperty("message")
  public String getMessage() {
    return message;
  }

  public void setMessage(final String message) {
    this.message = message;
  }
}
