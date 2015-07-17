package com.coredump.socialdump.domain;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.*;

/**
 * Created by fabio on 09/07/15.
 */
@Entity
public class OrganizationRole implements Serializable {
  private short id;
  private String name;
  private Collection<OrganizationMember> organizationMembersById;
  private Collection<OrganizationPrivilege> organizationPrivilegesById;

  @Id
  @Column(name = "id", columnDefinition = "smallint(3) unsigned", nullable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  public short getId() {
    return id;
  }

  public void setId(short id) {
    this.id = id;
  }

  @Basic
  @Column(name = "name", length = 50, nullable = false)
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

    OrganizationRole that = (OrganizationRole) o;

    if (id != that.id) return false;
    if (name != null ? !name.equals(that.name) : that.name != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = (int) id;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    return result;
  }

  @OneToMany(mappedBy = "organizationRoleByRoleId")
  public Collection<OrganizationMember> getOrganizationMembersById() {
    return organizationMembersById;
  }

  public void setOrganizationMembersById(Collection<OrganizationMember> organizationMembersById) {
    this.organizationMembersById = organizationMembersById;
  }

  @OneToMany(mappedBy = "organizationRoleByRoleId")
  public Collection<OrganizationPrivilege> getOrganizationPrivilegesById() {
    return organizationPrivilegesById;
  }

  public void setOrganizationPrivilegesById(Collection<OrganizationPrivilege> organizationPrivilegesById) {
    this.organizationPrivilegesById = organizationPrivilegesById;
  }
}
