package com.coredump.socialdump.web.layout;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by fabio on 7/26/15.
 */

@Controller
@RequestMapping(value = "/layout-service")
public class LayoutResource {

  @RequestMapping(value = "/public-event")
  public String getRentListLayout() {
    return "layouts/public-event";
  }
}
