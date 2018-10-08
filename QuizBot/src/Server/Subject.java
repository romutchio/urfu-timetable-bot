package Server;

import java.util.Date;
import java.util.List;
import java.util.stream.Collector;

public class Subject {
    public String weekday;
    public String lessonName;
    public String lessonStartTime;
    public String lessonEndTime;
    public List teachers;
    public List rooms;

    public Subject(String weekday, String lessonName, String lessonStartTime, String lessonEndTime, List teachers, List rooms)
    {
        this.weekday = weekday;
        this.lessonName = lessonName;
        this.lessonStartTime = lessonStartTime;
        this.lessonEndTime = lessonEndTime;
        this.teachers = teachers;
        this.rooms = rooms;
    }

}
