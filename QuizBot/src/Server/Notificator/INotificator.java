package Server.Notificator;

import Server.User;

public interface INotificator {
    void run();

    void addNewNotificationAboutLesson(User user, String day, Integer lessonNumber);

    void addNewNotificationAboutLesson(User user, String day, Integer lessonNumber, boolean notifyOnce);

    void deleteNotificationAboutLesson(User user, String day, Integer lessonNumber);

    void deleteNotificationAboutLesson(User user, String day, Integer lessonNumber, boolean deleteJustToday);

    void cancelAllNotification(String token);
}
