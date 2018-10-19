package com.example.deniskalenga.myapplication.models;

public class Business {

    private String name;
    private String adminFullName;
    private String adminEmail;
    private String adminPhoneNumber;
    private String adminPassword;

    public Business() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdminFullName() {
        return adminFullName;
    }

    public void setAdminFullName(String adminFullName) {
        this.adminFullName = adminFullName;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public String getAdminPhoneNumber() {
        return adminPhoneNumber;
    }

    public void setAdminPhoneNumber(String adminPhoneNumber) {
        this.adminPhoneNumber = adminPhoneNumber;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    @Override
    public String toString() {
        return "Business{" +
                "name='" + name + '\'' +
                ", adminFullName='" + adminFullName + '\'' +
                ", adminEmail='" + adminEmail + '\'' +
                ", adminPhoneNumber='" + adminPhoneNumber + '\'' +
                ", adminPassword='" + adminPassword + '\'' +
                '}';
    }
}
