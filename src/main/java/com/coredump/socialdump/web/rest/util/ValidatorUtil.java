package com.coredump.socialdump.web.rest.util;

import org.joda.time.DateTime;

/**
 * Created by fabio on 7/22/15.
 */
public class ValidatorUtil {
  public static boolean isDateLower(DateTime endDate, DateTime startDate) {
    return endDate.isBefore(startDate);
  }
}
