package com.coredump.socialdump.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by fabio on 09/07/15.
 */
@Entity
public class OrganizationMember implements Serializable {
  private long id;
  private Timestamp startDate;
  private Timestamp endDate;
  private Organization organization;
  private OrganizationRole organizationRole;
  private GenericStatus genericStatus;
  private User user;

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
  public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      OrganizationMember that = (OrganizationMember) o;

      if (id != that.id) return false;
      if (startDate != null ? !startDate.equals(that.startDate) : that.startDate != null) return false;
      if (endDate != null ? !endDate.equals(that.endDate) : that.endDate != null) return false;

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
  public Organization getorganization() {
      return organization;
  }

  public void setorganization(Organization organization) {
      this.organization = organization;
  }

  @ManyToOne
  @JoinColumn(name = "roleId", referencedColumnName = "id", nullable = false)
  public OrganizationRole getorganizationRole() {
      return organizationRole;
  }

  public void setorganizationRole(OrganizationRole organizationRole) {
      this.organizationRole = organizationRole;
  }

  @ManyToOne
  @JoinColumn(name = "statusId", referencedColumnName = "id", nullable = false)
  public GenericStatus getgenericStatus() {
      return genericStatus;
  }

  public void setgenericStatus(GenericStatus genericStatus) {
      this.genericStatus = genericStatus;
  }

  @ManyToOne
  @JoinColumn(name = "userId", referencedColumnName = "id", nullable = false)
  public User getuser() {
      return user;
  }

  public void setuser(User user) {
      this.user = user;
  }
}
