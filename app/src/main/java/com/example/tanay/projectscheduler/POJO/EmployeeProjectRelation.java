package com.example.tanay.projectscheduler.POJO;

public class EmployeeProjectRelation {
    String userName,projectName;

    public EmployeeProjectRelation(String userName, String projectName) {
        this.userName = userName;
        this.projectName = projectName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
