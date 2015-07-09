package com.coredump.socialdump.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

/**
 * Created by fabio on 09/07/15.
 */
@Entity
public class OrganizationFuncionality implements Serializable {
    private int id;
    private String name;
    private Collection<OrganizationPrivilege> organizationPrivilegesById;

    @Id
    @Column(name = "id", columnDefinition = "bigint(15) unsigned", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name", length = 80, nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrganizationFuncionality that = (OrganizationFuncionality) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "organizationFuncionalityByFunctionalityId")
    public Collection<OrganizationPrivilege> getOrganizationPrivilegesById() {
        return organizationPrivilegesById;
    }

    public void setOrganizationPrivilegesById(Collection<OrganizationPrivilege> organizationPrivilegesById) {
        this.organizationPrivilegesById = organizationPrivilegesById;
    }
}
