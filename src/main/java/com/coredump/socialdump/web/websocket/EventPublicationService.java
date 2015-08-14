package com.coredump.socialdump.web.websocket;

import com.coredump.socialdump.domain.SocialNetworkPost;
import com.coredump.socialdump.repository.SocialNetworkPostRepository;

import com.coredump.socialdump.web.websocket.dto.SocialNetworkPostSocketDTO;
import com.coredump.socialdump.web.websocket.mapper.SocialNetworkPostSocketMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.core.MessageSendingOperations;


import org.springframework.messaging.simp.broker.BrokerAvailabilityEvent;
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
    String destination = "/topic/eventPublications";
    List<SocialNetworkPostSocketDTO> dtoList = postList
          .stream()
          .map(postSocketMapper::SocialNetworkPostToSocialNetworkPostDTO)
          .collect(Collectors.toList());
    this.messagingTemplate.convertAndSend(destination, dtoList);
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
