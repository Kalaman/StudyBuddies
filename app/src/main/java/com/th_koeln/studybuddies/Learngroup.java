package com.th_koeln.studybuddies;

/**
 * Created by Kalaman on 28.03.2018.
 */

public class Learngroup {
    String title;
    String description;
    String datetime;
    String location;
    String maxstudent;
    String studentCount;
    String course;
    String campus;

    public Learngroup(String title, String description, String datetime, String location, String maxstudent, String course, String campus, String studentCount) {
        this.title = title;
        this.description = description;
        this.datetime = datetime;
        this.location = location;
        this.maxstudent = maxstudent;
        this.course = course;
        this.campus = campus;
        this.studentCount = studentCount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDatetime() {
        return datetime;
    }

    public String getLocation() {
        return location;
    }

    public String getMaxstudent() {
        return maxstudent;
    }

    public String getCourse() {
        return course;
    }

    public String getCampus() {
        return campus;
    }

    public String getStudentCount() {
        return studentCount;
    }
}
