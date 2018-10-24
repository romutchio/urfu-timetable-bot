//package com.clients;
//
//import com.server.*;
//import com.server.notificator.NotificationManager;
//import com.server.notificator.Notificator;
//import org.junit.Test;
//
//import java.time.Clock;
//import java.time.Instant;
//import java.time.ZoneOffset;
//
//import static org.junit.Assert.assertEquals;
//
//public class TestNotifications {
//    public User initGraphOfMessages(Message nextMessage) {
//        var user = new User("test",
//                new Group(false,
//                        "МЕН-180101",
//                        1,
//                        978680,
//                        35175,
//                        true,
//                        25714),
//                nextMessage,
//                "",
//                new NotificationManager());
//
//        DatabaseOfSessions.AddNewUserInDatabase(user);
//        return user;
//    }
//
//    @Test
//    public void test_notification_add(){
//        var time = Instant.now(Clock.fixed(
//                Instant.parse("2018-10-22T08:40:00Z"),
//                ZoneOffset.UTC));
//        var message = new Message(
//                "",
//                "add notification");
//        initGraphOfMessages(message);
//        var timeTable = Notificator.getDataBase("Вторник", user);
//
//        var answ = AnswerHandler.handleAnswer(
//                "test",
//                "вторник");
//        var currentDataBase = DatabaseOfSessions.getDatabaseOfUsers();
//        var user = currentDataBase.get("test");
//        var userNotifications = user.notifications.Days;
//        var currentDayNotifications = userNotifications.get("Вторник");
//        assertEquals(timeTable.size(), currentDayNotifications.Lessons.size());
//    }
//
//}
