package com.rashedkhan.ratings.data.model;


public class UserType {
    private int userTypeId;
    private String name;
    private int isPerson;
    private int isService;
    private int isBusiness;

    public UserType() {
    }

    public int getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(int userTypeId) {
        this.userTypeId = userTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsPerson() {
        return isPerson == 1;
    }

    public void setIsPerson(int isPerson) {
        this.isPerson = isPerson;
    }

    public boolean getIsService() {
        return isService == 1;
    }

    public void setIsService(int isService) {
        this.isService = isService;
    }

    public boolean getIsBusiness() {
        return isBusiness == 1;
    }

    public void setIsBusiness(int isBusiness) {
        this.isBusiness = isBusiness;
    }
}
