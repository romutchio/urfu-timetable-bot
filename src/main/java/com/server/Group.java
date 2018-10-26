package com.server;

public class Group {
    public boolean updated;
    public String title;
    public int course;
    public int id;
    public int external_id;
    public boolean actual;
    public int institute_id;

    public Group() {
        this.updated = false;
        this.title = null;
        this.course = 0;
        this.id = 0;
        this.external_id = 0;
        this.actual = false;
        this.institute_id = 0;
    }

    public Group(boolean updated, String title,
                 int course, int id, int external_id,
                 boolean actual, int institute_id) {
        this.updated = updated;
        this.title = title;
        this.course = course;
        this.id = id;
        this.external_id = external_id;
        this.actual = actual;
        this.institute_id = institute_id;
    }
}
