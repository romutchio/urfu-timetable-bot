package Server;

import java.util.Date;

public class Subject {
    String weekday;
    String lessonName;
    String lessonStartTime;
    String lessonEndTime;
    String teacher;

    public Subject(String weekday, String lessonName, String lessonStartTime, String lessonEndTime, String teacher)
    {
        this.weekday = weekday;
        this.lessonName = lessonName;
        this.lessonStartTime = lessonStartTime;
        this.lessonEndTime = lessonEndTime;
        this.teacher = teacher;
    }
}
