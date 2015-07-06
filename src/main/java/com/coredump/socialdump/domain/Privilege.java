package com.coredump.socialdump.domain;

import javax.persistence.*;

/**
 * Created by fabio on 05/07/15.
 */
@Entity
public class Privilege {
    private int id;
    private Functionality functionalityByFunctionalityId;
    private Role roleByRoleId;

    @Id
    @Column(name = "id")
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

        Privilege privilege = (Privilege) o;

        if (id != privilege.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @ManyToOne
    @JoinColumn(name = "functionalityId", referencedColumnName = "id", nullable = false)
    public Functionality getFunctionalityByFunctionalityId() {
        return functionalityByFunctionalityId;
    }

    public void setFunctionalityByFunctionalityId(Functionality functionalityByFunctionalityId) {
        this.functionalityByFunctionalityId = functionalityByFunctionalityId;
    }

    @ManyToOne
    @JoinColumn(name = "roleId", referencedColumnName = "id", nullable = false)
    public Role getRoleByRoleId() {
        return roleByRoleId;
    }

    public void setRoleByRoleId(Role roleByRoleId) {
        this.roleByRoleId = roleByRoleId;
    }
}
