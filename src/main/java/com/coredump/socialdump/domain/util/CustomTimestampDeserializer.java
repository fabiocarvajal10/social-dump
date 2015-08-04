package com.coredump.socialdump.domain.util;

/**
 * Created by fabio on 7/26/15.
 */
import java.io.IOException;
import java.sql.Timestamp;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
public class CustomTimestampDeserializer extends JsonDeserializer<Timestamp>{
  @Override
  public Timestamp deserialize(JsonParser jp, DeserializationContext ctxt)
        throws IOException {
    JsonToken token = jp.getCurrentToken();
    if (token == JsonToken.VALUE_STRING) {
      String str = jp.getText().trim();
      return Timestamp.valueOf(str);
    }
    if (token == JsonToken.VALUE_NUMBER_INT) {
      return new Timestamp(jp.getLongValue());
    }
    return null;
  }
}
