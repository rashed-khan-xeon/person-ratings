package com.rashedkhan.ratings.data.model;

/**
 * Created by arifk on 30.12.17.
 */

public class User {
    private int userId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
    private int userTypeId;
    private UserType userType;
    private UserSetting userSetting;
    private UserRole userRole;
    private String designation;
    private String orgName;
    private String image;
    private int hasVerified;

    private int active;

    public User() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(int userTypeId) {
        this.userTypeId = userTypeId;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public UserSetting getUserSetting() {
        return userSetting;
    }

    public void setUserSetting(UserSetting userSetting) {
        this.userSetting = userSetting;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public boolean getActive() {
        return active == 1;
    }

    public void setActive(boolean act) {
        if (act) {
            active = 1;
        } else {
            active = 0;
        }
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", userTypeId=" + userTypeId +
                ", userType=" + userType +
                ", userSetting=" + userSetting +
                ", userRole=" + userRole +
                ", designation='" + designation + '\'' +
                ", orgName='" + orgName + '\'' +
                ", image='" + image + '\'' +
                ", active=" + active +
                '}';
    }

    public boolean hasVerified() {
        return hasVerified == 1;
    }

    public void setHasVerified(int hasVerified) {
        this.hasVerified = hasVerified;
    }
}
