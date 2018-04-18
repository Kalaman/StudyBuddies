package com.th_koeln.studybuddies;

/**
 * Created by Kalaman on 28.03.2018.
 */

public class Learngroup {
    String title;
    String creator;
    String description;
    String datetimeFrom;
    String datetimeTo;
    String location;
    String maxstudent;
    String studentCount;
    String course;
    String campus;
    String lid;

    public Learngroup(String lid, String title, String description, String datetimeFrom, String datetimeTo, String location, String maxstudent, String course, String campus, String studentCount) {
        this.lid = lid;
        this.title = title;
        this.description = description;
        this.datetimeFrom = datetimeFrom;
        this.location = location;
        this.maxstudent = maxstudent;
        this.course = course;
        this.campus = campus;
        this.studentCount = studentCount;
        this.datetimeTo = datetimeTo;

    }

    public String getLid() { return lid; }

    public void setLid(String lid) {
        this.lid = lid;
    }

    public String getDatetimeFrom() { return datetimeFrom; }

    public void setDatetimeFrom(String from) {
        this.datetimeFrom = from.substring(8,10) + "." + from.substring(5,7) + "." +
                from.substring(0,4) + from.substring(10,16);
    }

    public void setDatetimeTo(String to) {
        this.datetimeTo = to.substring(8,10) + "." + to.substring(5,7) + "." +
                to.substring(0,4) + to.substring(10,16);
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

    public String getDatetimeTo() {
        return datetimeTo;
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

    public String getFormattedDateTimeFrom() {
        return datetimeFrom.substring(6,10) + "-" + datetimeFrom.substring(3,5) + "-" + datetimeFrom.substring(0,2) + " " + datetimeFrom.substring(11,16) + ":00";
    }
}
