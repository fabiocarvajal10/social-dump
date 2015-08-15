package com.coredump.socialdump.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.*;

/**
 * Created by fabio on 09/07/15.
 */
@Entity
public class OrganizationMember implements Serializable {
  private long id;
  private Timestamp startDate;
  private Timestamp endDate;
  private Organization organizationByOrganizationId;
  private OrganizationRole organizationRoleByRoleId;
  private GenericStatus genericStatusByStatusId;
  private User userByUserId;

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
  @Column(name = "startDate", nullable = false)
  public Timestamp getStartDate() {
    return startDate;
  }

  public void setStartDate(Timestamp startDate) {
    this.startDate = startDate;
  }

  @Basic
  @Column(name = "endDate")
  public Timestamp getEndDate() {
    return endDate;
  }

  public void setEndDate(Timestamp endDate) {
    this.endDate = endDate;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }

    OrganizationMember that = (OrganizationMember) object;

    if (id != that.id) {
      return false;
    }
    if (startDate != null ? !startDate.equals(that.startDate) : that.startDate != null) {
      return false;
    }
    if (endDate != null ? !endDate.equals(that.endDate) : that.endDate != null) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int result = (int) (id ^ (id >>> 32));
    result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
    result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
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

  @ManyToOne
  @JoinColumn(name = "roleId", referencedColumnName = "id", nullable = false)
  public OrganizationRole getOrganizationRoleByRoleId() {
    return organizationRoleByRoleId;
  }

  public void setOrganizationRoleByRoleId(OrganizationRole organizationRoleByRoleId) {
    this.organizationRoleByRoleId = organizationRoleByRoleId;
  }

  @ManyToOne
  @JoinColumn(name = "statusId", referencedColumnName = "id", nullable = false)
  public GenericStatus getGenericStatusByStatusId() {
    return genericStatusByStatusId;
  }

  public void setGenericStatusByStatusId(GenericStatus genericStatusByStatusId) {
    this.genericStatusByStatusId = genericStatusByStatusId;
  }

  @ManyToOne
  @JoinColumn(name = "userId", referencedColumnName = "id", nullable = false)
  public User getUserByUserId() {
    return userByUserId;
  }

  public void setUserByUserId(User userByUserId) {
    this.userByUserId = userByUserId;
  }
}
