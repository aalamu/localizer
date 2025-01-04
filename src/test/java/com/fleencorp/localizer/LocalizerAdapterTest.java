package com.fleencorp.localizer;

import com.fleencorp.localizer.model.exception.ApiException;
import com.fleencorp.localizer.model.response.ApiResponse;
import com.fleencorp.localizer.model.response.ErrorResponse;
import com.fleencorp.localizer.service.adapter.LocalizerAdapter;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.StaticMessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class LocalizerAdapterTest {

  private LocalizerAdapter localizerAdapter;

  @BeforeEach
  void setUp() {
    final StaticMessageSource messageSource = new StaticMessageSource();
    final StaticMessageSource responseMessageSource = new StaticMessageSource();
    final StaticMessageSource errorMessageSource = new StaticMessageSource();

    // Define messages in different message sources
    messageSource.addMessage("test.key", Locale.US, "Test Message");
    responseMessageSource.addMessage("response.key", Locale.US, "Response Message");
    errorMessageSource.addMessage("error.key", Locale.US, "Error Message");

    // Initialize the LocalizerAdapter
    localizerAdapter = new LocalizerAdapter(messageSource, responseMessageSource, errorMessageSource);
  }

  @Test
  void testGetMessageWithLocale() {
    Locale locale = Locale.US;
    String key = "test.key";
    String expectedMessage = "Test Message";

    String actualMessage = localizerAdapter.getMessage(key, locale);

    assertEquals(expectedMessage, actualMessage);
  }

  @Test
  void testGetMessageResWithLocale() {
    Locale locale = Locale.US;
    String key = "response.key";
    String expectedMessage = "Response Message";

    String actualMessage = localizerAdapter.getMessageRes(key, locale);

    assertEquals(expectedMessage, actualMessage);
  }

  @Test
  void testGetMessageExWithLocale() {
    Locale locale = Locale.US;
    String key = "error.key";
    String expectedMessage = "Error Message";

    String actualMessage = localizerAdapter.getMessageEx(key, locale);

    assertEquals(expectedMessage, actualMessage);
  }

  @Test
  void testGetMessageWithoutLocale() {
    LocaleContextHolder.setLocale(Locale.US);
    String key = "test.key";
    String expectedMessage = "Test Message";

    String actualMessage = localizerAdapter.getMessage(key);

    assertEquals(expectedMessage, actualMessage);
  }

  @Test
  void testOfResponseWithMessageCode() {
    ApiResponse response = new ApiResponse() {
      @Override
      public String getMessageCode() {
        return "response.key";
      }
    };

    ApiResponse result = localizerAdapter.of(response);

    assertNotNull(result);
    assertEquals("Response Message", result.getMessage());
  }

  @Test
  void testOfResponseWithMessageCodeAndParams() {
    ApiResponse response = new ApiResponse() {
      @Override
      public String getMessageCode() {
        return "response.key";
      }

      @Override
      public Object[] getParams() {
        return new Object[] {};  // No params in this case
      }
    };

    ApiResponse result = localizerAdapter.of(response, "response.key");

    assertNotNull(result);
    assertEquals("Response Message", result.getMessage());
  }

  @Test
  void testWithStatus() {
    ApiException ex = new ApiException() {
      @Override
      public String getMessageCode() {
        return "error.key";
      }

      @Override
      public Object[] getParams() {
        return new Object[] {};  // No params in this case
      }
    };

    int status = Response.Status.BAD_REQUEST.getStatusCode();
    ErrorResponse result = localizerAdapter.withStatus(ex, Response.Status.BAD_REQUEST);

    assertNotNull(result);
    assertEquals("Error Message", result.getMessage());
    assertEquals(status, result.getStatus());
  }


  @Test
  void testOfExceptionWithMessageCodeAndParams() {
    ApiException ex = new ApiException() {
      @Override
      public String getMessageCode() {
        return "error.key";
      }

      @Override
      public Object[] getParams() {
        return new Object[] {};  // No params in this case
      }
    };

    ApiException result = localizerAdapter.of(ex);

    assertNotNull(result);
    assertEquals("Error Message", result.getMessage());
  }

}

