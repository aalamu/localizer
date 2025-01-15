# Localizer

_"A comprehensive Java library for internationalizing your APIs and applications with ease."_

Localizer is a Java library that simplifies internationalization for your Java applications. It allows seamless resolution of localized messages from `MessageSource` instances or beans, supporting multiple locales for general, response, and error messages. With deep integration into Spring Boot and other frameworks (like Vaadin, Quarkus, Jakarta), Localizer enhances the user experience by offering an easy way to localize API responses and handle errors gracefully.

**Features**
- Retrieve localized messages from `MessageSource` beans.
- Support multiple locales for general, response, and error messages.
- Simplified error handling with localized messages.
- Seamless integration with custom `ApiResponse` and `ApiException` classes for APIs.
- Easily localize exceptions and API responses.


## Installation

Add the following Maven dependency to your project `pom.xml` file:

```xml
<dependency>
  <groupId>com.fleencorp.i18n</groupId>
  <artifactId>localizer</artifactId>
  <version>2.0.0</version>
</dependency>
```

If you're using Gradle, include the following in your `build.gradle` file:

```groovy
implementation 'com.fleencorp.i18n:localizer:2.0.0'
```

**Usage**

Here’s how to use Localizer in a Spring Boot application. While this example uses Spring Boot, the same approach applies to frameworks like Vaadin, Quarkus, Jakarta, and even standalone Java applications.

1. **Bean Configuration**  
   First, configure the base name and encoding for your message resource files:

```properties
# application.properties
spring.messages.message.base-name=classpath:i18n/messages
spring.messages.encoding=UTF-8
```

Then, ensure `LocalizerAdapter` is configured as a bean in your application. Below is an example configuration class:

```java
@Configuration
public class MessageSourceConfiguration {

  // Customizable 
  private ReloadableResourceBundleMessageSource baseMessageSource() {
    final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
    messageSource.setDefaultLocale(Locale.US);
    messageSource.setUseCodeAsDefaultMessage(false);
    messageSource.setFallbackToSystemLocale(false);
    messageSource.setAlwaysUseMessageFormat(true);
    return messageSource;
  }

  @Bean
  @Primary
  public MessageSource messageSource(
      @Value("${spring.messages.message.base-name}") final String baseName,
      @Value("${spring.messages.encoding}") final String encoding) {
    final ReloadableResourceBundleMessageSource messageSource = baseMessageSource();
    messageSource.setBasenames(baseName);
    messageSource.setDefaultEncoding(encoding);
    return messageSource;
  }

  @Bean
  public Localizer localizer(final MesssageSource messsageSource) {
    return new LocalizerAdapter(messsageSource);
  }

  @Bean
  public ErrorLocalizer errorLocalizer(final MesssageSource messsageSource) {
    return new ErrorLocalizerAdapter(messsageSource);
  }
}
```

2. **Creating Message Source Files**
   
    Define your localized messages in messages.properties files. For example:

```properties
# messages.properties
add.country=Country added successfully
reset.password.code.expired=Reset password code has expired
```

Create your message files with the following pattern messages[_LOCALE].properties. For example:

- messages.properties: Default messages.
- messages_en_US.properties: Messages for the en_US locale.
- messages_fr.properties: Messages for the fr locale.

Add additional locale-specific files such as `messages_fr.properties`, `messages_de.properties` and `messages_es.properties` for other translations.

3. **Localizing API Responses**

    Localize your API responses by overriding the getMessageCode method in your ApiResponse class:

```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddCountryResponse extends ApiResponse {

  @Override
  public String getMessageCode() {
    return "add.country";
  }
}
```

Use `Localizer` with `AddCountryResponse` in `CountryServiceImpl`

<details>
    <summary>Click to see more</summary>

```
  public class CountryServiceImpl implements CountryService {

    private final Localizer localizer;
    private final CountryRepository countryRepository;

    public CountryServiceImpl(
      final Localizer localizer,
      final CountryRepository countryRepository) {
    this.localizer = localizer;
    this.countryRepository = countryRepository;
  }

  @Override
  public AddCountryResponse addCountry(final AddCountryDto countryDto) {
    final Country country = countryDto.toCountry();

    countryRepository.save(country);
    final CountryResponse countryResponse = CountryMapper.toCountryResponse(country);

    localizer.of(countryResponse);
  }
}
```

</details>

4. **Handling Exceptions**
    
    Localize your exceptions by overriding the getMessageCode method in custom ApiException classes:

How to use ApiException with `<T extends ApiException> ErrorResponse withStatus(T ex, Response.Status status)` and `<T extends ApiException> T of(T ex);`

```java
public class DisabledAccountException extends ApiException {

  @Override
  public String getMessageCode() {
    return "disabled.account";
  }
}
```

5. **How to localize Exceptions with `<T extends ApiException> ErrorResponse withStatus(T ex, Response.Status status);`**

```java 
@RestControllerAdvice
public class RestExceptionHandler {

   private final ErrorLocalizer localizer;
   
   public RestExceptionHandler(final ErrorLocalizer localizer) {
     this.localizer = localizer;
   }

   @ExceptionHandler(value = {
     DisabledAccountException.class
   })
   @ResponseStatus(value = BAD_REQUEST)
   public ErrorResponse handleBadRequest(final DisabledAccountException ex) {
      return localizer.withStatus(ex, Response.Status.BAD_REQUEST);
   }
}
```


6. **Advance Usage**


```java
public class SignInResponse extends ApiResponse {
  
   @Override
   public String getMessageCode() {
      return "sign.in";
   }

   @JsonIgnore
   public String getPreVerificationMessageCode() {
      return "sign.in.pre.verification";
   }

   @JsonIgnore
   public String getMfaAuthenticatorMessageCode() {
      return "sign.in.mfa.authenticator";
   }

   @JsonIgnore
   public String getMfaEmailOrPhoneMessageCode() {
      return MfaType.isEmail(getMfaType()) ? "sign.in.mfa.email" : "sign.in.mfa.phone";
   }

   @JsonIgnore
   public String getMfaMessageCode() {
      return MfaType.isAuthenticator(getMfaType()) ? getMfaAuthenticatorMessageCode() : getMfaEmailOrPhoneMessageCode();
   }

   @Override
   public Object[] getParams() {
      final String verificationType = MfaType.isPhone(getMfaType()) ? phoneNumber.toString() : emailAddress.toString();
      return new Object[] { verificationType };
   }
}
```

```java
/**
 * Signs in a user with the provided credentials.
 *
 * <p>This method authenticates the user using the provided email address and password.
 * It validates the user's profile status to ensure it is not disabled or banned.
 * Depending on the user's profile status and MFA settings, it handles the sign-in process
 * accordingly by initializing authentication, creating tokens, and updating the sign-in response.</p>
 *
 * @param signInDto the DTO containing user's sign-in credentials
 * @return SignInResponse containing authentication details and user information
 * @throws InvalidAuthenticationException if authentication fails for the provided credentials
 * @throws DisabledAccountException       if the user's profile status is disabled
 * @throws BannedAccountException         if the user's profile status is banned
 */
public SignInResponse signIn(final SignInDto signInDto) {
   final String emailAddress = signInDto.getEmailAddress();
   final String password = signInDto.getPassword();
   final Authentication authentication = authenticateCredentials(emailAddress, password);
   final AuthenticatedUser user = (AuthenticatedUser) authentication.getPrincipal();

   setUserTimezoneAfterAuthentication(user);
   validateProfileIsNotDisabledOrBanned(user.getProfileStatus());

   final SignInResponse signInResponse = userMapper.toSignInResponse(user);

   if (isProfileInactiveAndUserYetToBeVerified(user)) {
      return processSignInForProfileYetToBeVerified(signInResponse, user);
   }

   if (isMfaEnabledAndMfaTypeSet(user)) {
      return processSignInForProfileWithMfaEnabled(signInResponse, user);
   }

   return processSignInForProfileThatIsVerified(signInResponse, user, authentication);
}

/**
 * Processes the sign-in for a user whose profile is yet to be verified.
 *
 * <p>This method handles the sign-in process for a user whose profile has incomplete verification details
 * such as email or phone. The user may not yet have completed all necessary verification steps.
 * The result is returned as a localized response with a message code indicating the pre-verification status.</p>
 *
 * @param signInResponse the sign-in response object to be updated based on the user's verification status
 * @param user the authenticated {@link VerifiedUser} whose profile is yet to be verified
 * @return a {@link SignInResponse} containing the result of the sign-in process, with a
 *         localization message for pre-verification
 */
protected SignInResponse processSignInForProfileYetToBeVerified(final SignInResponse signInResponse, final FleenUser user) {
   handleProfileYetToBeVerified(signInResponse, user);
   return localizer.of(signInResponse, signInResponse.getPreVerificationMessageCode());
}

/**
 * Processes the sign-in for a user with Multi-Factor Authentication (MFA) enabled.
 *
 * <p>This method handles the sign-in process for a user whose profile has MFA or 2FA enabled. It ensures
 * that the necessary MFA steps are carried out before allowing the user to complete the sign-in.
 * The result is returned as a localized response, with an appropriate message code for MFA.</p>
 *
 * @param signInResponse the sign-in response object to be updated based on the user's MFA settings
 * @param user the authenticated {@link VerifiedUser} whose profile has MFA enabled
 * @return a {@link SignInResponse} containing the final result of the sign-in process, with the
 *         appropriate localization message for MFA
 */
protected SignInResponse processSignInForProfileWithMfaEnabled(final SignInResponse signInResponse, final FleenUser user) {
   handleProfileWithMfaEnabled(signInResponse, user);
   return localizer.of(signInResponse, signInResponse.getMfaMessageCode());
}

/**
 * Processes the sign-in for a user with a verified profile.
 *
 * <p>This method handles the final steps of the sign-in process for a user whose profile has been verified. It
 * ensures that the user's profile is fully authenticated and their access is granted, with any necessary
 * post-authentication actions being applied. The result of the sign-in process is then returned as a
 * localized response.</p>
 *
 * @param signInResponse the sign-in response object to be updated based on the verified profile
 * @param user the authenticated {@link VerifiedUser} whose profile is verified
 * @param authentication the {@link Authentication} object containing the authenticated user's details
 * @return a {@link SignInResponse} containing the final result of the sign-in process, with any
 *         appropriate localization messages
 */
protected SignInResponse processSignInForProfileThatIsVerified(final SignInResponse signInResponse, final FleenUser user, final Authentication authentication) {
   handleProfileThatIsVerified(signInResponse, user, authentication);
   return localizer.of(signInResponse);
}
```