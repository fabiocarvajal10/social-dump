package com.coredump.socialdump.domain;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by fabio on 09/07/15.
 */
@Entity
public class MonitorContact {
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Organization organizationByOrganizationId;
    private Collection<TemporalAccess> temporalAccessesById;

    @Id
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "firstName")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Basic
    @Column(name = "lastName")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "phone")
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
