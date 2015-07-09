package com.coredump.socialdump.domain;

import javax.persistence.*;

/**
 * Created by fabio on 09/07/15.
 */
@Entity
public class OrganizationPrivilege {
    private int id;
    private OrganizationFuncionality organizationFuncionalityByFunctionalityId;
    private OrganizationRole organizationRoleByRoleId;

    @Id
    @Column(name = "id", columnDefinition = "int(7) unsigned", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrganizationPrivilege that = (OrganizationPrivilege) o;

        if (id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @ManyToOne
    @JoinColumn(name = "functionalityId", referencedColumnName = "id", nullable = false)
    public OrganizationFuncionality getOrganizationFuncionalityByFunctionalityId() {
        return organizationFuncionalityByFunctionalityId;
    }

    public void setOrganizationFuncionalityByFunctionalityId(OrganizationFuncionality organizationFuncionalityByFunctionalityId) {
        this.organizationFuncionalityByFunctionalityId = organizationFuncionalityByFunctionalityId;
    }

    @ManyToOne
    @JoinColumn(name = "roleId", referencedColumnName = "id", nullable = false)
    public OrganizationRole getOrganizationRoleByRoleId() {
        return organizationRoleByRoleId;
    }

    public void setOrganizationRoleByRoleId(OrganizationRole organizationRoleByRoleId) {
        this.organizationRoleByRoleId = organizationRoleByRoleId;
    }
}
