package com.fleencorp.localizer.service.adapter;

import org.springframework.context.MessageSource;

public class DefaultLocalizerAdapter extends LocalizerAdapter implements DefaultLocalizer {

  public DefaultLocalizerAdapter(final MessageSource messageSource) {
    super(messageSource);
  }
}
