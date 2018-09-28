package Server;

import java.util.Date;
import java.util.List;
import java.util.stream.Collector;

public class Subject {
    String weekday;
    String lessonName;
    String lessonStartTime;
    String lessonEndTime;
    List teachers;

    public Subject(String weekday, String lessonName, String lessonStartTime, String lessonEndTime, List teachers)
    {
        this.weekday = weekday;
        this.lessonName = lessonName;
        this.lessonStartTime = lessonStartTime;
        this.lessonEndTime = lessonEndTime;
        this.teachers = teachers;
    }

}
