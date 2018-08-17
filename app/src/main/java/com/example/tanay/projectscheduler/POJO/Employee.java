package com.example.tanay.projectscheduler.POJO;

public class Employee {
    String name, companyName, userName, password, eMail;
    Boolean isManager;

    public Employee(){

    };

    public Employee(String name, String companyName, String userName, String password, String eMail, Boolean isManager) {
        this.name = name;
        this.companyName = companyName;
        this.userName = userName;
        this.password = password;
        this.eMail = eMail;
        this.isManager = isManager;
    }

    public String getName() {
        return name;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String geteMail() {
        return eMail;
    }

    public Boolean getManager() {
        return isManager;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public void setManager(Boolean manager) {
        isManager = manager;
    }
}
