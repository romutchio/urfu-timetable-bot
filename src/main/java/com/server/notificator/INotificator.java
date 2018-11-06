package com.server.notificator;

import com.server.User;

public interface INotificator {
    void run();

    void addNewNotificationAboutLesson(User user, String day, Integer lessonNumber);

    void addNewNotificationAboutLesson(User user, String day, Integer lessonNumber, boolean notifyOnce);

    void deleteNotificationAboutLesson(User user, String day, Integer lessonNumber);

    void deleteNotificationAboutLesson(User user, String day, Integer lessonNumber, boolean deleteJustToday);

    void cancelAllUserNotification(String token);
}
