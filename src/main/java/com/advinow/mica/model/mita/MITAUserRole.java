package com.advinow.mica.model.mita;


public class MITAUserRole {

    private int roleId;
    private String name;
    private String description;

    public int getRoleId() {

        return roleId;
    }

    public void setRoleId(int roleId) {

        this.roleId = roleId;
    }

  
    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

     public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MITAUserRole)) return false;

        MITAUserRole mITAUserRole = (MITAUserRole) o;

        return roleId == mITAUserRole.roleId;
    }

    @Override
    public int hashCode() {

        return roleId;
    }

}