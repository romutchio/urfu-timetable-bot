package com.server;

import java.util.Collection;

public class Lesson {
    public Integer advanceTime;
    public Integer lessonNumber;
    public Lesson(){

    }
    public Lesson(Integer lessonNumber, Integer advanceTime){
        this.advanceTime = advanceTime;
        this.lessonNumber = lessonNumber;
    }

    public static Lesson findLesson(Collection<Lesson> lessons, Integer lessonNumber) {
        return lessons.stream().filter(lesson -> lessonNumber == lesson.lessonNumber).findFirst().orElse(null);
    }

}
