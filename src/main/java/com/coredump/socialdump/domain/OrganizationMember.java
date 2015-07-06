package com.coredump.socialdump.domain;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by fabio on 05/07/15.
 */
@Entity
public class OrganizationMember {
    private long id;
    private Timestamp startDate;
    private Timestamp endDate;
    private Organization organizationByOrganizationId;
    private OrganizationRole organizationRoleByRoleId;
    private GenericStatus genericStatusByStatusId;
    private UserSD userByUserId;

    @Id
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "startDate")
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
    public UserSD getUserByUserId() {
        return userByUserId;
    }

    public void setUserByUserId(UserSD userByUserId) {
        this.userByUserId = userByUserId;
    }
}