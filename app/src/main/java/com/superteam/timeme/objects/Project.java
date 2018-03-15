package com.superteam.timeme.objects;

/**
 * Created by Ronit on 03/15/2018.
 */

public class Project {

    private String projectName, projectDescription, projectUID, clientUID;

    public Project(String projectName, String projectDescription) {
        this.projectName = projectName;
        this.projectDescription = projectDescription;
    }

    public Project() {
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public String getProjectUID() {
        return projectUID;
    }

    public void setProjectUID(String projectUID) {
        this.projectUID = projectUID;
    }

    public String getClientUID() {
        return clientUID;
    }

    public void setClientUID(String clientUID) {
        this.clientUID = clientUID;
    }

}
