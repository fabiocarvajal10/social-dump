package com.coredump.socialdump.web.rest.dto;

import com.coredump.socialdump.domain.util.CustomDateTimeDeserializer;
import com.coredump.socialdump.domain.util.CustomDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.joda.time.DateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashMap;

/**
 * Objeto de transmisión de datos de resúmen de evento.
 * @author Esteban
 */
public class EventSummaryDTO {

  /**
  * Objeto de transmisión de datos de resúmen red social por evento.
  * Corresponde al resúmen en un moment de la cantidad de posts de una
  * red social en un evento.
  */
  public class SocialNetworkSummaryDTO {

    /**
    * Id del evento.
    */
    private long eventId;

    /**
    * Id de la red social.
    */
    private int socialNetworkId;

    /**
    * Total de posts obtenidos
    */
    private long total;

    /**
    * Recibe todos los parámetros necesarios para inicializar en un estado
    * válido el objeto.
    * @param eventId Id del evento
    * @param socialNetworkId Id de la red social
    * @param total total de posts obtenidos
    */
    public SocialNetworkSummaryDTO(long eventId, int socialNetworkId,
                                   long total) {
      this.eventId = eventId;
      this.socialNetworkId = socialNetworkId;
      this.total = total;
    }

    /**
    * Constructor por defecto. Usado para asignar manualmente los atributos de la
    * instancia.
    */
    public SocialNetworkSummaryDTO() {
      this(0, 0, 0);
    }

    public void setSocialNetworkId(int socialNetworkId) {
      this.socialNetworkId = socialNetworkId;
    }

    public int getSocialNetworkId() {
      return socialNetworkId;
    }

    public void setEventId(long eventId) {
      this.eventId = eventId;
    }

    public int getEventId() {
      return eventId;
    }

    public void setTotal(long total) {
      this.total = total;
    }

    public long getTotal() {
      return total;
    }

    @Override
    public String toString() {
      return "SocialNetworkSummaryDTO{"
              + "generatedAt='" + generatedAt + '}';
    }
  }


  /**
  * Id del evento
  */
  private long eventId;

  /**
  * Momento en que fue creado
  */
  @NotNull
  @JsonSerialize(using = CustomDateTimeSerializer.class)
  @JsonDeserialize(using = CustomDateTimeDeserializer.class)
  private DateTime generatedAt;

  /**
  * Resúmen de red social por evento
  */
  private HashMap<Integer, SocialNetworkSummaryDTO>
    socialNetworkSummaries;

  /**
  * Inicializa en un estado válido el resúmen de un evento.
  * @param eventId Id del evento
  * @param socialNetworkSummaries resúmen de las redes sociales
  * @param total total de posts contando todas las redes sociales.
  */
  public EventSummaryDTO(long eventId,
    HashMap<Integer, SocialNetworkSummaryDTO> socialNetworkSummaries,
    long total) {
    this.eventId = eventId;
    this.socialNetworkSummaries = socialNetworkSummaries;
    this.total = total;
  }

  /**
  * Constructor por defecto. Usado para asignar manualmente los atributos de la
  * instancia.
  */
  public EventSummaryDTO() {
    this(0, new HashMap<Integer, SocialNetworkSummaryDTO>(), 0);
  }

  /**
  * Asegura que el total sea acorde a los datos del HashMap.
  * @param eventId Id del evento
  * @param socialNetworkSummaries resúmen de las redes sociales
  */
  public EventSummaryDTO(long eventId,
    SocialNetworkSummaryDTO> socialNetworkSummaries) {
    this(0, new HashMap<Integer, SocialNetworkSummaryDTO>(), 0);
    long total = 0;
    for(long socialNetworkTotal : socialNetworkSummaries.values()) {
      total += socialNetworkTotal;
    }
  }



  public Long getEventId() {
    return eventId;
  }

  public void setEventId(Long eventId) {
    this.eventId = eventId;
  }

  public DateTime getGeneratedAt() {
    return activatedAt;
  }

  public void setGeneratedAt(DateTime generatedAt) {
    this.generatedAt = generatedAt;
  }

  public HashMap<Integer, SocialNetworkSummaryDTO> getSocialNetworkSummaries() {
    return socialNetworkSummaries;
  }

  public void setSocialNetworkSummaries(
    HashMap<Integer, SocialNetworkSummaryDTO> socialNetworkSummaries) {
    this.socialNetworkSummaries = socialNetworkSummaries;
  }

  @Override
  public String toString() {
    return "EventSummaryDTO{"
            + "generatedAt='" + generatedAt + '}';
  }


}
