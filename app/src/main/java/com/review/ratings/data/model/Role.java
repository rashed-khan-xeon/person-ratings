package com.review.ratings.data.model;

/**
 * Created by arifk on 30.12.17.
 */

public class Role {
    private int roleId;
    private String name;

    public Role() {
    }

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
}
