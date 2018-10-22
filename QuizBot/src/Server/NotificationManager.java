package Server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class NotificationManager {
    private String[] WeekDays = new String[]{"Воскресенье", "Понедельник", "Вторник", "Среда", "Четверг",
            "Пятница", "Суббота"};
    public HashMap<String, Notification> Days = new HashMap<>();
    public NotificationManager(){
        addInfoAboutLessonsNotifications();
    }


    private void addInfoAboutLessonsNotifications(){
        for (var day: WeekDays){
            Days.put(day, new Notification());
        }
    }

//    public static void changeDayLessonsNotifications(String username, String day, ArrayList<Integer> lessons){
//        var user = DatabaseOfSessions.GetUserByToken(username);
//        var userDays = user.notifications.Days;
//        var currentDay = userDays.get(day);
//        currentDay.Lessons = lessons;
//        Days.replace(day, currentDay);
//    }
//
//    public static  void changeDayRepetitionsNotifications(String username, String day, Integer repetitions){
//        var user = DatabaseOfSessions.GetUserByToken(username);
//        var userDays = user.notifications.Days;
//        var currentDay = userDays.get(day);
//        currentDay.Repetitions = repetitions;
//        Days.replace(day, currentDay);
//    }
//
//    public static void changeDayAdvanceTimeNotifications(String username, String day, Integer time){
//        var user = DatabaseOfSessions.GetUserByToken(username);
//        var userDays = user.notifications.Days;
//        var currentDay = userDays.get(day);
//        currentDay.AdvanceTime = time;
//        Days.replace(day, currentDay);
//    }

    public void loadUsersNotifications(String username ){
        var currentDataBase = DatabaseOfSessions.GetUserByToken(username);

    }


}


