package com.coredump.socialdump.web.websocket;

import com.coredump.socialdump.domain.SocialNetworkPost;
import com.coredump.socialdump.repository.SocialNetworkPostRepository;

import com.coredump.socialdump.web.websocket.dto.RemovePostDTO;
import com.coredump.socialdump.web.websocket.dto.SocialNetworkPostSocketDTO;
import com.coredump.socialdump.web.websocket.mapper.SocialNetworkPostSocketMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.core.MessageSendingOperations;


import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.broker.BrokerAvailabilityEvent;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;


import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;


/**
 * Created by fabio on 7/18/15.
 */


@Service
@Controller
public class EventPublicationService implements ApplicationListener<BrokerAvailabilityEvent> {

  private static final Logger log = LoggerFactory.getLogger(EventPublicationService.class);

  @Inject
  private final MessageSendingOperations<String> messagingTemplate;

  private AtomicBoolean brokerAvailable = new AtomicBoolean();

  @Inject
  SocialNetworkPostSocketMapper postSocketMapper;

  @Inject
  SocialNetworkPostRepository socialNetworkPostRepository;

  @Inject
  public EventPublicationService(MessageSendingOperations<String> messagingTemplate) {
    this.messagingTemplate = messagingTemplate;
  }

  /**
  *
  * */
  public void showPost(List<SocialNetworkPost> postList) throws Exception {
    long eventId = 0;
    if (postList.size() > 0) {
      eventId = postList.get(0).getEventByEventId().getId();
    }

    String destination = "/topic/eventPublications/" + eventId;
    List<SocialNetworkPostSocketDTO> dtoList = postList
          .stream()
          .map(postSocketMapper::SocialNetworkPostToSocialNetworkPostDTO)
          .collect(Collectors.toList());
    this.messagingTemplate.convertAndSend(destination, dtoList);
  }

  @MessageMapping("/removePost")
  public void removePost(RemovePostDTO removePostDTO) {
    log.debug("Post Id to remove " + removePostDTO.getId());
    String destination = "/topic/blockPost/" + removePostDTO.getEventId();
    //checking if post exists
    if ( removePostDTO.getId() != null
          && socialNetworkPostRepository.findOne(removePostDTO.getId()) != null) {
      //remove from database
      socialNetworkPostRepository.delete(removePostDTO.getId());
      //send update to clients

      this.messagingTemplate.convertAndSend(destination, removePostDTO);
    }
  }



  @RequestMapping("/start")
  public String start() {
    return "start";
  }

  @Override
  public void onApplicationEvent(BrokerAvailabilityEvent event) {
    this.brokerAvailable.set(event.isBrokerAvailable());
  }
}
