package com.coredump.socialdump.service;

import com.coredump.socialdump.domain.Event;
import com.coredump.socialdump.domain.GenericStatus;
import com.coredump.socialdump.domain.SearchCriteria;
import com.coredump.socialdump.repository.GenericStatusRepository;
import com.coredump.socialdump.repository.SearchCriteriaRepository;
import com.coredump.socialdump.repository.SocialNetworkRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing SearchCriteria.
 */
@Service
@Transactional
public class SearchCriteriaService {

  private final Logger log = LoggerFactory.getLogger(SearchCriteriaService.class);

  @Inject
  private SearchCriteriaRepository searchCriteriaRepository;

  @Inject
  private SocialNetworkRepository socialNetworkRepository;

  @Inject
  private GenericStatusRepository genericStatusRepository;

  public List<SearchCriteria> getSearchCriteriasFromStringList(Event event,
                                                               List<String> searchCriterias) {

    List<SearchCriteria> searchCriteriaList = new ArrayList<>();
    int socialNetworkId = 1;
    for (String searchCriteria : searchCriterias) {
      if (searchCriteria != null || !searchCriteria.isEmpty()) {
        SearchCriteria sc = new SearchCriteria();
        sc.setEventByEventId(event);
        sc.setSearchCriteria(processSc(searchCriteria, socialNetworkId));
        sc.setSocialNetworkBySocialNetworkId(socialNetworkRepository.getOne(socialNetworkId));
        sc.setGenericStatusByStatusId(genericStatusRepository.getOne((short) 1));
        searchCriteriaRepository.save(sc);
        searchCriteriaList.add(sc);
        socialNetworkId++;
      }
    }

    return searchCriteriaList;
  }

  private String processSc(String searchCriteria, int socialNetworkId) {
    String sc = null;
    String localSearchCriteria = searchCriteria;
    localSearchCriteria = localSearchCriteria.replaceAll("\\W", "");
    if (socialNetworkId == 1) {
      if (localSearchCriteria.charAt(0) != '#') {
        sc = new StringBuilder(localSearchCriteria).insert(0, "#").toString();
      }
    } else if (socialNetworkId == 2) {
      //Instagram no acepta # el request
      sc = localSearchCriteria.replaceAll("#", "");
    }

    return sc;
  }

  public void inactivateAll(Event event) {
    GenericStatus genericStatus = genericStatusRepository.findOne((short) 2);
    searchCriteriaRepository.findAllByEventByEventId(event)
      .forEach(sc -> {
        sc.setGenericStatusByStatusId(genericStatus);
        searchCriteriaRepository.save(sc);
      });
  }
}

