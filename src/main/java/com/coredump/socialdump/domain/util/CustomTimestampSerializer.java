package com.coredump.socialdump.domain.util;

/**
 * Created by fabio on 7/26/15.
 */

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class CustomTimestampSerializer extends
      JsonSerializer<Timestamp> {

  private static SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  @Override
  public void serialize(Timestamp value,
                        JsonGenerator generator,
                        SerializerProvider provider)
        throws IOException {
    String dateFormat = formatter.format(value);
    generator.writeString(dateFormat);
  }
}
