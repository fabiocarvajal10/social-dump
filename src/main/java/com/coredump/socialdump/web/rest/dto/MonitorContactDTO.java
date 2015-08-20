package com.coredump.socialdump.web.rest.dto;

import java.io.Serializable;

/**
 * Created by Franz on 20/07/2015.
 */
public class MonitorContactDTO implements Serializable {

  private long id;
  private String firstName;
  private String lastName;
  private String email;
  private String phone;
  private Long organizationId;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public Long getOrganizationId() {
    return organizationId;
  }

  public void setOrganizationId(Long organizationId) {
    this.organizationId = organizationId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    MonitorContactDTO that = (MonitorContactDTO) o;

    if (getId() != that.getId()) return false;
    if (!getFirstName().equals(that.getFirstName())) return false;
    if (!getLastName().equals(that.getLastName())) return false;
    if (!getEmail().equals(that.getEmail())) return false;
    if (!getPhone().equals(that.getPhone())) return false;
    return getOrganizationId().equals(that.getOrganizationId());

  }

  @Override
  public int hashCode() {
    int result = (int) (getId() ^ (getId() >>> 32));
    result = 31 * result + getFirstName().hashCode();
    result = 31 * result + getLastName().hashCode();
    result = 31 * result + getEmail().hashCode();
    result = 31 * result + getPhone().hashCode();
    result = 31 * result + getOrganizationId().hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "MonitorContactDTO{" +
      "id=" + id +
      ", firstName='" + firstName + '\'' +
      ", lastName='" + lastName + '\'' +
      ", email='" + email + '\'' +
      ", phone='" + phone + '\'' +
      ", organizationId=" + organizationId +
      '}';
  }
}
