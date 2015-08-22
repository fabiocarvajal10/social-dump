package com.coredump.socialdump.web.websocket.dto;

/**
 * Created by fabio on 8/9/15.
 */
public class RemovePostDTO {
  private Long id;
  private Long eventId;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getEventId() {
    return eventId;
  }

  public void setEventId(Long eventId) {
    this.eventId = eventId;
  }

}
