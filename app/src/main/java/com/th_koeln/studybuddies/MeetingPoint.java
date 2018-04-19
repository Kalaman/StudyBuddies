package com.th_koeln.studybuddies;

/**
 * Created by alasdair on 16.04.18.
 */

public class MeetingPoint {
    private int mpid;
    private int cpid;
    private String name;

    public MeetingPoint(String name,int mpid, int cpid) {
        this.mpid = mpid;
        this.name = name;
        this.cpid = cpid;
    }

    public int getCpid() {
        return cpid;
    }

    public void setCpid(int cpid) {
        this.cpid = cpid;
    }

    public int getMpid() {
        return mpid;
    }

    public void setMpid(int mpid) {
        this.mpid = mpid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
