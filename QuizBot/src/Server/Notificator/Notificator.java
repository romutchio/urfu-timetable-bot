package Server.Notificator;

import Clients.TelegramAPI;
import Clients.TelegramClient;
import Server.*;
import Server.DatabaseOfSessions;
import Server.Subject;
import Server.TimetableParsing;
import Server.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Notificator implements Runnable {
    private static String[] WeekDays = new String[]{"Воскресенье", "Понедельник", "Вторник", "Среда", "Четверг",
            "Пятница", "Суббота"};
    private static SimpleDateFormat TimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static ConcurrentHashMap<String, HashMap<Date, ScheduledExecutorService>> NotificationSchedule = new ConcurrentHashMap<>();


    public void run() {
        while (true){
            createSchedule();
            try {
                TimeUnit.HOURS.sleep(24);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cancelAllNotifications();
            NotificationSchedule.clear();
        }
    }

    private static void createSchedule() {
        var currentDataBase = DatabaseOfSessions.getDatabaseOfUsers();
        var currentTime = new Date();
        for (var user : currentDataBase.values()) {
            var currentDayWeek = DetermineWeekDay(currentTime);
            var currentTimetable = getDataBase(currentDayWeek);
            if (currentTimetable.size() == 0)
                return;
            var userNotifications = user.notifications.Days;
            var currentDayNotifications = userNotifications.get(currentDayWeek);
            var lessonsToNotify = currentDayNotifications.Lessons;

            for (var lesson : lessonsToNotify) {
                if (lesson.lessonNumber - 1 < currentTimetable.size()) {
                    var firstLesson = currentTimetable.get(lesson.lessonNumber - 1);
                    createNewNotification(user, firstLesson.lessonStartTime, firstLesson.lessonName, lesson.advanceTime);
                }
            }
        }
    }

    private static void createNewNotification(User user, String lessonStart, String lessonName, Integer advanceTime) {
        System.out.println(lessonStart);
        Date lessonStartDate = null;
        try {
            lessonStartDate = TimeFormatter.parse(lessonStart);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long delay = lessonStartDate.getTime() - System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(advanceTime);
        Date notificationTime = new Date(lessonStartDate.getTime() - TimeUnit.MINUTES.toMillis(advanceTime));
        System.out.println(delay);
        if (delay > 0) {
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.schedule(() -> SendTelegramNotification(user, lessonName, advanceTime), delay, TimeUnit.MILLISECONDS);
            if (!NotificationSchedule.containsKey(user.token))
                NotificationSchedule.put(user.token, new HashMap<>());
            NotificationSchedule.get(user.token).put(notificationTime, scheduler);
        }


    }

    public static void addNewNotificationAboutLesson(User user, String day, Integer lessonNumber) {
        addNewNotificationAboutLesson(user, day, lessonNumber, false);
    }

    public static void addNewNotificationAboutLesson(User user, String day, Integer lessonNumber, boolean notifyOnce) {
        var userNotifications = user.notifications.Days;
        var currentDayNotifications = userNotifications.get(day);
        var currentTimetable = getDataBase(day);
        if (currentTimetable.size() < lessonNumber - 1)
            return;
        if (!notifyOnce) {
            currentDayNotifications.Lessons.add(new Lesson(lessonNumber, 15));
            DatabaseOfSessions.UpdateUserInDatabase(user);
        }

        if (lessonNumber - 1 < currentTimetable.size()) {
            var firstLesson = currentTimetable.get(lessonNumber - 1);
            var lesson = Lesson.findLesson(currentDayNotifications.Lessons, lessonNumber -1);
            createNewNotification(user, firstLesson.lessonStartTime, firstLesson.lessonName, lesson.advanceTime);
        }

    }

    public static void deleteNotificationAboutLesson(User user, String day, Integer lessonNumber) {
        deleteNotificationAboutLesson(user, day, lessonNumber, false);
    }

    public static void deleteNotificationAboutLesson(User user, String day, Integer lessonNumber, boolean deleteJustToday) {
        var userNotifications = user.notifications.Days;
        var currentDayNotifications = userNotifications.get(day);
        var currentTimetable = getDataBase(day);
        if (currentTimetable.size() < lessonNumber - 1)
            return;
        var lessonToRemove = Lesson.findLesson(currentDayNotifications.Lessons, lessonNumber-1);
        if (!deleteJustToday && lessonToRemove != null) {
            currentDayNotifications.Lessons.remove(lessonToRemove);
            DatabaseOfSessions.UpdateUserInDatabase(user);
        }
        if (lessonNumber - 1 < currentTimetable.size()) {
            var firstLesson = currentTimetable.get(lessonNumber - 1);
            Date lessonStartDate = null;
            try {
                lessonStartDate = TimeFormatter.parse(firstLesson.lessonStartTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            var lesson = Lesson.findLesson(currentDayNotifications.Lessons, lessonNumber -1);
            Date notificationTime = new Date(lessonStartDate.getTime() - TimeUnit.MINUTES.toMillis(lesson.advanceTime));
            if (NotificationSchedule.get(user.token).containsKey(notificationTime)) {
                NotificationSchedule.get(user.token).get(notificationTime).shutdownNow();
                NotificationSchedule.get(user.token).remove(notificationTime);
            }
        }
    }

    public static void cancelAllUserNotification(String token) {
        for (var notification : NotificationSchedule.get(token).values()) {
            notification.shutdownNow();
        }
        NotificationSchedule.get(token).clear();
    }

    private static void cancelAllNotifications(){
        for (var user: NotificationSchedule.values()){
            for (var notification: user.values()){
                notification.shutdownNow();
            }
        }
    }

    private static ArrayList<Subject> getDataBase(String currentDayWeek) {
        var calendarStr = TimetableParsing.ReadFile("./DataBase/calendar_fiit_202.ics");
        var weekTimetable = TimetableParsing.CreateTimeTableDataBase(calendarStr);
        var currentTimetable = weekTimetable.get(currentDayWeek);

        return currentTimetable;
    }

    public static String DetermineWeekDay(Date currentTime) {
        var calendar = Calendar.getInstance();
        calendar.setTime(currentTime);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return WeekDays[dayOfWeek - 1];
    }

    private static void SendTelegramNotification(User user, String lessonName, Integer advanceTime) {
        var notificationMessage = "Через " + advanceTime + " минут начинается " + lessonName;
        new TelegramAPI().sendMessage(user.token, notificationMessage);
        DatabaseOfSessions.UpdateUserInDatabase(user);
    }

}
