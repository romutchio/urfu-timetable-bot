package Server;

import Clients.TelegramAPI;
import Clients.TelegramClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Notificator implements Runnable{
    private static String[] WeekDays = new String[]{"Воскресенье", "Понедельник", "Вторник", "Среда", "Четверг",
            "Пятница", "Суббота"};
    private static SimpleDateFormat TimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static HashMap<String, HashMap<Date, ScheduledExecutorService>> NotificationSchedule = new HashMap<>();


    
    public void run(){
        createSchedule();
    }

    private void createSchedule(){
        var currentDataBase = DatabaseOfSessions.getDatabaseOfUsers().values();
        var currentTime = new Date();
        for (var user: currentDataBase){
            var currentDayWeek = DetermineWeekDay(currentTime);
            var currentTimetable = getDataBase(currentDayWeek);
            if (currentTimetable.size() == 0)
                return;

            var userNotifications = user.notifications.Days;
            var currentDayNotifications = userNotifications.get(currentDayWeek);
            var advanceTime= currentDayNotifications.AdvanceTime;
            var lessonsToNotify = currentDayNotifications.Lessons;

            var lessonsStartTime = new HashMap<String, String>();
            for (var lessonNumber: lessonsToNotify){
                if(lessonNumber-1 < currentTimetable.size()){
                    var firstLesson = currentTimetable.get(lessonNumber-1);
                    lessonsStartTime.put(firstLesson.lessonStartTime, firstLesson.lessonName);
                }
            }
            for (var lessonStart: lessonsStartTime.keySet()){
                var lessonName = lessonsStartTime.get(lessonStart);
                createNewNotification(user, lessonStart, lessonName, advanceTime);
            }
        }
//        addNewNotificationAboutLesson(DatabaseOfSessions.GetUserByToken("349845203"), "Пятница", 2);
//        deleteNotificationAboutLesson(DatabaseOfSessions.GetUserByToken("349845203"), "Пятница", 1, false);
    }

    private void createNewNotification(User user, String lessonStart, String lessonName, Integer advanceTime){
        Date lessonStartDate = null;
        try {
            lessonStartDate = TimeFormatter.parse(lessonStart);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long delay = lessonStartDate.getTime() - System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(advanceTime);
        Date notificationTime = new Date(lessonStartDate.getTime() - TimeUnit.MINUTES.toMillis(advanceTime));
        System.out.println(delay);
        if (delay > 0){
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.schedule(() -> SendTelegramNotification(user, lessonName, advanceTime), delay, TimeUnit.MILLISECONDS);
            if (!NotificationSchedule.containsKey(user.token))
                NotificationSchedule.put(user.token, new HashMap<>());
            NotificationSchedule.get(user.token).put(notificationTime, scheduler);
        }



    }

    public void addNewNotificationAboutLesson(User user, String day, Integer lessonNumber, boolean notifyOnce){
        var userNotifications = user.notifications.Days;
        var currentDayNotifications = userNotifications.get(day);
        var currentTimetable = getDataBase(day);
        if (currentTimetable.size() < lessonNumber-1)
            return;
        if (!notifyOnce){
            currentDayNotifications.Lessons.add(lessonNumber);
            DatabaseOfSessions.UpdateUserInDatabase(user);
        }

        if(lessonNumber-1 < currentTimetable.size()){
            var firstLesson = currentTimetable.get(lessonNumber-1);
            createNewNotification(user, firstLesson.lessonStartTime, firstLesson.lessonName, currentDayNotifications.AdvanceTime);
        }

    }

    public void deleteNotificationAboutLesson(User user, String day, Integer lessonNumber, boolean deleteJustToday){
        var userNotifications = user.notifications.Days;
        var currentDayNotifications = userNotifications.get(day);
        var currentTimetable = getDataBase(day);
        if (currentTimetable.size() < lessonNumber-1)
            return;
        if (!deleteJustToday && currentDayNotifications.Lessons.contains(lessonNumber)){
            currentDayNotifications.Lessons.remove(lessonNumber);
            DatabaseOfSessions.UpdateUserInDatabase(user);
        }
        if(lessonNumber-1 < currentTimetable.size()){
            var firstLesson = currentTimetable.get(lessonNumber-1);
            Date lessonStartDate = null;
            try {
                lessonStartDate = TimeFormatter.parse(firstLesson.lessonStartTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date notificationTime = new Date(lessonStartDate.getTime() - TimeUnit.MINUTES.toMillis(currentDayNotifications.AdvanceTime));
            if (NotificationSchedule.get(user.token).containsKey(notificationTime)){
                NotificationSchedule.get(user.token).get(notificationTime).shutdownNow();
                NotificationSchedule.get(user.token).remove(notificationTime);
            }
        }
    }

    public void cancelAllNotification(String token){
        for (var notification :NotificationSchedule.get(token).values()){
            notification.shutdownNow();

        }
        NotificationSchedule.get(token).clear();
    }

    private ArrayList<Subject> getDataBase(String currentDayWeek){
        var calendarStr = TimetableParsing.ReadFile("./DataBase/calendar_fiit_202.ics");
        var weekTimetable = TimetableParsing.CreateTimeTableDataBase(calendarStr);
        var currentTimetable = weekTimetable.get(currentDayWeek);

        return currentTimetable;
    }

    private static String DetermineWeekDay(Date currentTime){
        var calendar = Calendar.getInstance();
        calendar.setTime(currentTime);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return WeekDays[dayOfWeek - 1];
    }
    
    private static void SendTelegramNotification(User user, String lessonName, Integer advanceTime){
        var notificationMessage = "Через " + advanceTime + " минут начинается " + lessonName;
        new TelegramAPI().sendMessage(user.token, notificationMessage);
        DatabaseOfSessions.UpdateUserInDatabase(user);
    }

}
