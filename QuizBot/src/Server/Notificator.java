package Server;

import Clients.TelegramClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class Notificator implements Runnable{
    private static String[] WeekDays = new String[]{"Воскресенье", "Понедельник", "Вторник", "Среда", "Четверг",
            "Пятница", "Суббота"};
    private static SimpleDateFormat TimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat DateFormatter  = new SimpleDateFormat("yyyy-MM-dd");


    
    public void run(){
        while (true){
            var currentDataBase = DatabaseOfSessions.getDatabaseOfUsers().values();
            var currentTime = new Date();
            for (var user: currentDataBase){
                var currentDayWeek = DetermineWeekDay(currentTime);
                var calendarStr = TimetableParsing.ReadFile("./DataBase/calendar_fiit_202.ics");
                var currentTimetable = TimetableParsing.CreateTimeTableDataBase(calendarStr);
                if (currentTimetable.get(currentDayWeek).size() == 0)
                    return;
                var userNotifications = user.notifications.Days;
                var currentDay = userNotifications.get(currentDayWeek);
                var advanceTime= currentDay.AdvanceTime;
                var notificationsForLessons = currentDay.Lessons;
                var repetitions = currentDay.Repetitions;

                var firstLesson = currentTimetable.get(currentDayWeek).get(0);
                var firstLessonStart = firstLesson.lessonStartTime;
                Date lessonStartDate = null;
                try {
                    lessonStartDate = TimeFormatter.parse(firstLessonStart);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                var timeDifference = getDateDiff(currentTime, lessonStartDate);
                if (timeDifference < user.notificationAdvanceTime && timeDifference > 0) {
                    var currentDate = DateFormatter.format(currentTime);
                    if (user.lastNotified == null || !user.lastNotified.equals(currentDate)) {
                        SendTelegramNotification(user, firstLesson, currentDate);
                    }
                }
            }
            try {
                TimeUnit.MINUTES.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private static String DetermineWeekDay(Date currentTime){
        var calendar = Calendar.getInstance();
        calendar.setTime(currentTime);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return WeekDays[dayOfWeek - 1];
    }
    
    private static void SendTelegramNotification(User user, Subject firstLesson, String currentDate){
        var notificationMessage = "Через " + user.notificationAdvanceTime + " минут начинается " + firstLesson.lessonName;
        new TelegramClient().sendMsg(user.handle, notificationMessage, true);
        user.lastNotified =  currentDate;
        DatabaseOfSessions.UpdateUserInDatabase(user);
    }


    private static long getDateDiff(Date currentTime, Date lessonStartTime) {
        long diffInMillies = lessonStartTime.getTime() - currentTime.getTime();
        return TimeUnit.MINUTES.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }
}
