package com.coredump.socialdump.web.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.datatype.joda.ser.DateTimeSerializer;
import com.fasterxml.jackson.datatype.joda.ser.JacksonJodaFormat;
import org.joda.time.DateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.datetime.joda.DateTimeFormatterFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import javax.inject.Inject;
/**
 * Utility class for testing REST controllers.
 */
@Service
public class TestUtil {

  @Inject
  private UserDetailsService userDetailsService;

  @Inject
  private AuthenticationManager authenticationManager;

  /**
   * MediaType for JSON UTF8
   */
  public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
    MediaType.APPLICATION_JSON.getType(),
    MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

  /**
   * Convert an object to JSON byte array.
   *
   * @param object the object to convert
   * @return the JSON byte array
   * @throws IOException
   */
  public static byte[] convertObjectToJsonBytes(Object object)
    throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    JodaModule module = new JodaModule();
    DateTimeFormatterFactory formatterFactory = new DateTimeFormatterFactory();
    formatterFactory.setIso(DateTimeFormat.ISO.DATE);
    module.addSerializer(DateTime.class, new DateTimeSerializer(
      new JacksonJodaFormat(formatterFactory.createDateTimeFormatter()
        .withZoneUTC())));
    mapper.registerModule(module);
    return mapper.writeValueAsBytes(object);
  }

  /**
   * Logs in a user.
   *
   * @param username
   * @param password
   */
  public static void login(String username, String password) {
    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    securityContext.setAuthentication(
      new UsernamePasswordAuthenticationToken(username, password));
    SecurityContextHolder.setContext(securityContext);
  }


  /**
   * Logs in a user from the database.
   *
   * @param username
   * @param password
   */
  public void loginFromDB(String username, String password) {
    UsernamePasswordAuthenticationToken token =
      new UsernamePasswordAuthenticationToken(username, password);
    Authentication authentication =
      authenticationManager.authenticate(token);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    UserDetails details =
      userDetailsService.loadUserByUsername(username);

  }

}
