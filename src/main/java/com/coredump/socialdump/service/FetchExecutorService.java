package com.coredump.socialdump.service;

import com.coredump.socialdump.domain.Event;
import com.coredump.socialdump.domain.SearchCriteria;

import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * Created by Francisco J. Milanés Sánchez on 13/07/2015.
 */
@Service

public class FetchExecutorService {

  private final Logger log = LoggerFactory.getLogger(FetchExecutorService.class);

  @Inject
  private SocialNetworkBeanFactory socialNetworkFetchFactory;

  private Map<String, FetchableInterface> fetchableMap = new HashMap<>();

  private ScheduledExecutorService scheduledExecutorService;

  /**
   * Agenda la extracción de datos de las redes sociales.
   * @param event evento al que se va a agendar
   */
  public void scheduleFetch(Event event) {
    int searchCriteriaQ = event.getSearchCriteriasById().size();

    scheduledExecutorService = Executors
          .newScheduledThreadPool(searchCriteriaQ);

    List<SearchCriteria> scList = (List<SearchCriteria>) event.getSearchCriteriasById();

    scList.forEach(sc -> {
        try {
          FetchableInterface socialNetworkFetch = socialNetworkFetchFactory
              .getSocialNetworkFetch(sc.getSocialNetworkBySocialNetworkId()
              .getName().toLowerCase());

          log.debug("Search Criteria {}", sc.getSearchCriteria());
          socialNetworkFetch.prepareFetch(sc, event.getPostDelay());
          addSchedule(socialNetworkFetch, event.getStartDate());
          addToMap(event, socialNetworkFetch, sc);
        } catch (Exception e) {
          e.printStackTrace();
        }
      });
  }

  /**
   * Agrega un agendador, para la extracción de datos.
   * @param socialNetworkFetch red social de la que se va a extraer
   * @param startDate fecha en la que se empieza a extraer datos
   */
  private void addSchedule(FetchableInterface socialNetworkFetch, DateTime startDate) {
    log.debug("Scheduled to start in {} mins", getEventStartDelay(startDate));
    scheduledExecutorService
      .schedule(socialNetworkFetch, getEventStartDelay(startDate), TimeUnit.MINUTES);
  }

  /**
   * A&ntilde;ade un nuevo campo al Map fechableMap.
   * @param event evento al que se le va a agregar un criterio de b&uacute;squeda
   * @param socialNetworkFetch red social de la que se van a extraer datos
   * @param searchCriteria criterio que va a utilizar el
   *                       servicio para extraer los posts de una red social
   */
  private void addToMap(Event event, FetchableInterface socialNetworkFetch,
      SearchCriteria searchCriteria) {
    String key = buildKey(event, searchCriteria);
    fetchableMap.put(key, socialNetworkFetch);
  }

  /**
   * Método que detiene la sincronización del eventoa una red social
   * detiene la extracción de datos.
   * @param searchCriteria criterio por el que se va a realizar la búsqueda
   * @return valor booleano indicando el resultado de la stop
   */
  public boolean stopSynchronization(SearchCriteria searchCriteria) {
    String key = buildKey(searchCriteria.getEventByEventId(), searchCriteria);

    if (fetchableMap.containsKey(key) && fetchableMap.get(key) != null) {
      fetchableMap.get(key).kill();
      return true;
    } else {
      return false;
    }

  }

  /**
   * Detiene los hilos encargados de traer los datos de las redes sociales.
   * @param event evento que va detener extracción posts
   */
  public void killAll(Event event) {
    event.getSearchCriteriasById()
      .forEach(sc -> fetchableMap.get(buildKey(event, sc)).kill());
  }

  /**
   * Modifica el tiempo por los que se extraen los posts.
   * @param searchCriteria criterio de búsqueda que se va modificar
   * @param delay indica los segundos de retraso
   * @return delay modificado
   */
  public boolean modifyDelay(SearchCriteria searchCriteria, int delay) {
    String key = buildKey(searchCriteria.getEventByEventId(), searchCriteria);

    if (fetchableMap.containsKey(key) && fetchableMap.get(key) != null) {
      fetchableMap.get(key).setDelay(delay);
      return true;
    } else {
      return false;
    }

  }

  /**
   * Método encargado de retrasar el tiempo en el
   * que se extrae un conjunto de datos de las redes.
   * @param event evento a modificar
   * @param delay segundos de retraso
   */
  public void delayAll(Event event, int delay) {
    event.getSearchCriteriasById()
      .forEach(sc -> fetchableMap.get(buildKey(event, sc)).setDelay(delay));
  }

  /**
   * Método que devuelve la diferencia va a empezar la extracción de datos.
   * @param startDate fecha de inicio
   * @return diferencia entre la fecha de inicio del evento y la actual
   */
  private long getEventStartDelay(DateTime startDate) {
    DateTime now = new DateTime();
    return Minutes.minutesBetween(now, startDate).getMinutes();
  }

  /**
   * Método que crea el key con el que se va a insertar el searchCriteria.
   * @param event evento que maneja los criterios de búsqueda
   * @param searchCriteria criterio de búsqueda a insertar
   * @return un key para identificar el criterio de búsqueda
   */
  private String buildKey(Event event, SearchCriteria searchCriteria) {
    return event.getId().toString()
        + event.getOrganizationByOrganizationId().getId().toString()
        + searchCriteria.getSocialNetworkBySocialNetworkId().getName();
  }
}
