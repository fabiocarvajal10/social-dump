package com.coredump.socialdump.domain;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by fabio on 05/07/15.
 */
@Entity
@Table(name = "User", schema = "", catalog = "socialdump")
public class UserSD {
    private long id;
    private String username;
    private String passwordDigest;
    private Timestamp createdAt;
    private Timestamp activatedAt;
    private String email;
    private Role roleByRoleId;
    private GenericStatus genericStatusByStatusId;

    @Id
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "passwordDigest")
    public String getPasswordDigest() {
        return passwordDigest;
    }

    public void setPasswordDigest(String passwordDigest) {
        this.passwordDigest = passwordDigest;
    }

    @Basic
    @Column(name = "createdAt")
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Basic
    @Column(name = "activatedAt")
    public Timestamp getActivatedAt() {
        return activatedAt;
    }

    public void setActivatedAt(Timestamp activatedAt) {
        this.activatedAt = activatedAt;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserSD userSD = (UserSD) o;

        if (id != userSD.id) return false;
        if (username != null ? !username.equals(userSD.username) : userSD.username != null) return false;
        if (passwordDigest != null ? !passwordDigest.equals(userSD.passwordDigest) : userSD.passwordDigest != null)
            return false;
        if (createdAt != null ? !createdAt.equals(userSD.createdAt) : userSD.createdAt != null) return false;
        if (activatedAt != null ? !activatedAt.equals(userSD.activatedAt) : userSD.activatedAt != null) return false;
        if (email != null ? !email.equals(userSD.email) : userSD.email != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (passwordDigest != null ? passwordDigest.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (activatedAt != null ? activatedAt.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "roleId", referencedColumnName = "id", nullable = false)
    public Role getRoleByRoleId() {
        return roleByRoleId;
    }

    public void setRoleByRoleId(Role roleByRoleId) {
        this.roleByRoleId = roleByRoleId;
    }

    @ManyToOne
    @JoinColumn(name = "statusId", referencedColumnName = "id", nullable = false)
    public GenericStatus getGenericStatusByStatusId() {
        return genericStatusByStatusId;
    }

    public void setGenericStatusByStatusId(GenericStatus genericStatusByStatusId) {
        this.genericStatusByStatusId = genericStatusByStatusId;
    }
}
