package com.superteam.timeme.objects;

/**
 * Created by Ronit on 03/15/2018.
 */

public class Session {

    private int startTime, endTime, duration;
    String sessionUID, projectUID, clientUID;

    public Session(int startTime, int endTime, int duration) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
    }

    public Session() {
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public String getSessionUID() {
        return sessionUID;
    }

    public void setSessionUID(String sessionUID) {
        this.sessionUID = sessionUID;
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

}
