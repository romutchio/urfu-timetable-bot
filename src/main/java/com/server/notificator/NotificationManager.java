package com.server.notificator;

import java.util.HashMap;

public class NotificationManager {
    private String[] WeekDays = new String[]{"Воскресенье", "Понедельник", "Вторник", "Среда", "Четверг",
            "Пятница", "Суббота"};
    public HashMap<String, Notification> Days = new HashMap<>();

    public NotificationManager() {
        addInfoAboutLessonsNotifications();
    }


    private void addInfoAboutLessonsNotifications() {
        for (var day : WeekDays) {
            Days.put(day, new Notification());
        }
    }


}


