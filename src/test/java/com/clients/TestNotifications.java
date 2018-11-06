package com.clients;

import com.server.*;
import com.server.notificator.NotificationManager;
import com.server.notificator.Notificator;
import org.junit.Test;


import static org.junit.Assert.assertEquals;

public class TestNotifications {
    public User initGraphOfMessages(Message nextMessage) {
        var user = new User("test",
                new Group(false,
                        "РИ-270018",
                        2,
                        977620,
                        34115,
                        true,
                        15077),
                nextMessage,
                "",
                new NotificationManager());

        DatabaseOfSessions.AddNewUserInDatabase(user);
        return user;
    }

    @Test
    public void test_notification_add() {
        DatabaseOfSessions.SessionsDataBase = "DataBase/SessionsTest.json";
        var message = new Message(
                "",
                "add notification");
        initGraphOfMessages(message);

        var answ = AnswerHandler.handleAnswer(
                "test",
                "вторник");
        var currentDataBase = DatabaseOfSessions.getDatabaseOfUsers();
        var user = currentDataBase.get("test");
        var timeTable = Notificator.getDataBase("Вторник", user);
        var userNotifications = user.notifications.Days;
        var currentDayNotifications = userNotifications.get("Вторник");
        assertEquals(timeTable.size(), currentDayNotifications.Lessons.size());
    }

    @Test
    public void test_cancel_all_notification() {
        DatabaseOfSessions.SessionsDataBase = "DataBase/SessionsTest.json";
        var message = new Message(
                "",
                "delete all notification");
        initGraphOfMessages(message);
        AnswerHandler.handleAnswer(
                "test",
                "Да");
        var currentDataBase = DatabaseOfSessions.getDatabaseOfUsers();
        var user = currentDataBase.get("test");
        var days = user.notifications.Days;
        for (var day: days.values()){
            assertEquals(0, day.Lessons.size());
        }

    }

}
