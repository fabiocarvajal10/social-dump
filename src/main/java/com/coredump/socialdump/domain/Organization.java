package com.coredump.socialdump.domain;

import javax.persistence.*;
import java.sql.Timestamp;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.coredump.socialdump.domain.util.CustomDateTimeDeserializer;
import com.coredump.socialdump.domain.util.CustomDateTimeSerializer;
import org.hibernate.annotations.Type;

import javax.validation.constraints.*;
import java.io.Serializable;


/**
 * Created by fabio on 05/07/15.
 */
@Entity
@Table(name = "Organization")
public class Organization implements Serializable{
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "createdAt", nullable = false)
    private Timestamp createdAt;

    @ManyToOne
    @JoinColumn(name = "ownerId", referencedColumnName = "id", nullable = false)
    private UserSD userByOwnerId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public UserSD getUserByOwnerId() {
        return userByOwnerId;
    }

    public void setUserByOwnerId(UserSD userByOwnerId) {
        this.userByOwnerId = userByOwnerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Organization that = (Organization) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (createdAt != null ? !createdAt.equals(that.createdAt) : that.createdAt != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Organizationz{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", createdAt='" + createdAt+
            '}';
    }
}
