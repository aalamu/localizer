package com.fleencorp.localizer;

import com.fleencorp.localizer.model.exception.ApiException;
import com.fleencorp.localizer.model.response.ApiResponse;
import com.fleencorp.localizer.model.response.ErrorResponse;
import com.fleencorp.localizer.service.adapter.ErrorLocalizerAdapter;
import com.fleencorp.localizer.service.adapter.LocalizerAdapter;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.StaticMessageSource;

import java.util.Locale;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class LocalizerAdapterTest {

  private LocalizerAdapter localizerAdapter;
  private ErrorLocalizerAdapter errorLocalizerAdapter;

  @BeforeEach
  void setUp() {
    final StaticMessageSource messageSource = new StaticMessageSource();

    // Define messages in message sources
    messageSource.addMessage("test.key", Locale.US, "Test Message");
    messageSource.addMessage("response.key", Locale.US, "Response Message");
    messageSource.addMessage("response2.key", Locale.US, "Response Message {0}");
    messageSource.addMessage("error.key", Locale.US, "Error Message");

    // Initialize the LocalizerAdapter
    localizerAdapter = new LocalizerAdapter(messageSource);
    errorLocalizerAdapter = new ErrorLocalizerAdapter(messageSource);
  }

  @Test
  @DisplayName("Get a message with a locale")
  void testGetMessageWithLocale() {
    Locale locale = Locale.US;
    String key = "test.key";
    String expectedMessage = "Test Message";

    String actualMessage = localizerAdapter.getMessage(key, locale);

    assertEquals(expectedMessage, actualMessage);
  }

  @Test
  @DisplayName("Get a message without locale")
  void testGetMessageWithoutLocale() {
    LocaleContextHolder.setLocale(Locale.US);
    String key = "test.key";
    String expectedMessage = "Test Message";

    String actualMessage = localizerAdapter.getMessage(key);

    assertEquals(expectedMessage, actualMessage);
  }

  @Test
  @DisplayName("Get a response message with code")
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
  @DisplayName("Get a response message with code and parameters")
  void testOfResponseWithMessageCodeAndParams() {
    ApiResponse response = new ApiResponse() {

      @Override
      public String getMessageCode() {
        return "response2.key";
      }

      @Override
      public Object[] getParams() {
        return new Object[] { "Two" };
      }
    };

    ApiResponse result = localizerAdapter.of(response, "response2.key");

    assertNotNull(result);
    assertEquals("Response Message Two", result.getMessage());
  }

  @Test
  @DisplayName("Get Error Response with status")
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
    ErrorResponse result = errorLocalizerAdapter.withStatus(ex, Response.Status.BAD_REQUEST);

    assertNotNull(result);
    assertEquals("Error Message", result.getMessage());
    assertEquals(status, result.getStatus());
  }

  @Test
  @DisplayName("Get exception with message code and parameters")
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

    ApiException result = errorLocalizerAdapter.of(ex);

    assertNotNull(result);
    assertEquals("Error Message", result.getMessage());
  }

  @Test
  @DisplayName("Get message with a response supplier")
  void testOfWithSupplier() {
    Supplier<ApiResponse> responseSupplier = () -> new ApiResponse() {

      @Override
      public String getMessageCode() {
        return "response.key";
      }
    };

    Supplier<ApiResponse> localizedSupplier = localizerAdapter.of(responseSupplier);
    ApiResponse localizedResponse = localizedSupplier.get();

    assertNotNull(localizedResponse);
    assertEquals("Response Message", localizedResponse.getMessage());
  }

}

