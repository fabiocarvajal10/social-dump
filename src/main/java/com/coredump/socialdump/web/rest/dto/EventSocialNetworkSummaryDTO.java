package com.coredump.socialdump.web.rest.dto;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Entity;

/**
 * Objeto de transmisión de datos de resúmen de evento.
 * @author Esteban
 */
public class EventSocialNetworkSummaryDTO implements Serializable {

  /**
  * Id del evento.
  */
  private long eventId;

  /**
  * Id de la red social.
  */
  private int socialNetworkId;

  /**
   * Nombre de la red social.
   */
  private String socialNetworkName;

  /**
  * Total de posts obtenidos.
  */
  private long total;

  /**
   * Momento en que fue generado.
   */
  private Timestamp generatedAt;

  /**
  * Recibe todos los parámetros necesarios para inicializar en un estado
  * válido el objeto.
  * @param eventId Id del evento
  * @param socialNetworkId Id de la red social
  * @param socialNetworkName Nombre de la red social.
  * @param total total de posts obtenidos
  */
  public EventSocialNetworkSummaryDTO(long eventId, int socialNetworkId,
                                      String socialNetworkName, long total) {
    this.eventId = eventId;
    this.socialNetworkId = socialNetworkId;
    this.socialNetworkName = socialNetworkName;
    this.total = total;
    this.generatedAt = new Timestamp(System.currentTimeMillis());
  }

  /**
  * Constructor por defecto. Usado para asignar manualmente los atributos de la
  * instancia.
  */
  public EventSocialNetworkSummaryDTO() {
    this(0, 0, "", 0);
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

  public long getEventId() {
    return eventId;
  }

  public void setTotal(long total) {
    this.total = total;
  }

  public Timestamp getGeneratedAt() {
    return generatedAt;
  }

  public void setGeneratedAt(Timestamp generatedAt) {
    this.generatedAt = generatedAt;
  }

  public String getSocialNetworkName() {
    return socialNetworkName;
  }

  public void setSocialNetworkName(String socialNetworkName) {
    this.socialNetworkName = socialNetworkName;
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
