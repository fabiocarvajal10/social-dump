package com.coredump.socialdump.domain;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 * Created by fabio on 09/07/15.
 */
@Entity
public class MonitorContact implements Serializable {
  private long id;
  private String firstName;
  private String lastName;
  private String email;
  private String phone;
  private Organization organizationByOrganizationId;
  private Collection<TemporalAccess> temporalAccessesById;

  @Id
  @Column(name = "id", columnDefinition = "bigint(15) unsigned", nullable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @Basic
  @Column(name = "firstName", length = 100, nullable = false)
  @Size(max = 100)
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  @Basic
  @Column(name = "lastName", length = 100, nullable = false)
  @Size(max = 100)
  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  @Basic
  @Column(name = "email", length = 255)
  @Size(max = 255)
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Basic
  @Column(name = "phone", length = 80)
  @Size(max = 80)
  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    MonitorContact that = (MonitorContact) o;

    if (id != that.id) return false;
    if (firstName != null ? !firstName.equals(that.firstName) : that.firstName != null) return false;
    if (lastName != null ? !lastName.equals(that.lastName) : that.lastName != null) return false;
    if (email != null ? !email.equals(that.email) : that.email != null) return false;
    if (phone != null ? !phone.equals(that.phone) : that.phone != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = (int) (id ^ (id >>> 32));
    result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
    result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
    result = 31 * result + (email != null ? email.hashCode() : 0);
    result = 31 * result + (phone != null ? phone.hashCode() : 0);
    return result;
  }

  @ManyToOne
  @JoinColumn(name = "organizationId", referencedColumnName = "id", nullable = false)
  public Organization getOrganizationByOrganizationId() {
    return organizationByOrganizationId;
  }

  public void setOrganizationByOrganizationId(Organization organizationByOrganizationId) {
    this.organizationByOrganizationId = organizationByOrganizationId;
  }

  @OneToMany(mappedBy = "monitorContactByMonitorContactId")
  public Collection<TemporalAccess> getTemporalAccessesById() {
    return temporalAccessesById;
  }

  public void setTemporalAccessesById(Collection<TemporalAccess> temporalAccessesById) {
    this.temporalAccessesById = temporalAccessesById;
  }
}
