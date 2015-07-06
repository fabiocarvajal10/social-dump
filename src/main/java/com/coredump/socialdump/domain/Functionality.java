package com.coredump.socialdump.domain;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by fabio on 05/07/15.
 */
@Entity
public class Functionality {
    private int id;
    private String module;
    private String name;
    private Collection<Privilege> privilegesById;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "module")
    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    @Basic
    @Column(name = "name")
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

        Functionality that = (Functionality) o;

        if (id != that.id) return false;
        if (module != null ? !module.equals(that.module) : that.module != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (module != null ? module.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "functionalityByFunctionalityId")
    public Collection<Privilege> getPrivilegesById() {
        return privilegesById;
    }

    public void setPrivilegesById(Collection<Privilege> privilegesById) {
        this.privilegesById = privilegesById;
    }
}
