//package com.clients;
//
//import static org.junit.Assert.*;
//
//import com.server.*;
//import com.server.notificator.NotificationManager;
//import org.junit.Test;
//
//
//public class TestHandler {
//    public void initGraphOfMessages(Message nextMessage) {
//        var user = new User("testHandleAnswer",
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
//        user.lastDayRequest = "Вторник";
//        user.lastClassNumRequest = 1;
//
//        DatabaseOfSessions.AddNewUserInDatabase(user);
//    }
//
//    @Test
//    public void testHandleGetTimetableOnDate() {
//        var message = new Message(
//                "Я вас не понял, повторите пожалуйста",
//                "repeat answer");
//        initGraphOfMessages(message);
//        var answ = AnswerHandler.handleAnswer(
//                "testHandleAnswer",
//                "расписание на понедельник");
//        assertEquals("2018-10-22 09:00:00: История\n" +
//        "2018-10-22 10:40:00: История\n" +
//        "2018-10-22 12:50:00: Основы алгебры\n" +
//                        "\n" +
//                        "Хотите узнать еще что-нибудь?",
//                answ);
//
//        DatabaseOfSessions.RemoveUserFromDatabase("testHandleAnswer");
//    }
//
//    @Test
//    public void testHandleGetInformationAboutClass() {
//        var message = new Message(
//                "Я вас не понял, повторите пожалуйста",
//                "repeat answer");
//
//        initGraphOfMessages(message);
//
//        var answ = AnswerHandler.handleAnswer(
//                "testHandleAnswer",
//                "1 пара вторник");
//
//        assertEquals("Объектно-ориентированное программирование\n" +
//                        "Начало: 2018-10-23 10:40:00\n" +
//                        "Конец: 2018-10-23 12:10:00\n" +
//                        "Аудитория: 526\n" +
//                        "Преподаватель: Егоров Павел Владимирович\n" +
//                        "\n" +
//                        "Хотите узнать еще что-нибудь?",
//                answ);
//
//        DatabaseOfSessions.RemoveUserFromDatabase("testHandleAnswer");
//    }
//
//    @Test
//    public void testHandleGetInformationAboutNextClass() {
//        var message = new Message(
//                "",
//                "get information about class");
//        initGraphOfMessages(message);
//
//        var answ = AnswerHandler.handleAnswer(
//                "testHandleAnswer",
//                "следующая пара");
//
//        assertEquals("Компьютерные сети\n" +
//                        "Начало: 2018-10-23 12:50:00\n" +
//                        "Конец: 2018-10-23 14:20:00\n" +
//                        "Аудитория: 150\n" +
//                        "Преподаватель: Берсенев Александр Юрьевич\n" +
//                        "\n" +
//                        "Хотите узнать еще что-нибудь?",
//                answ);
//
//        DatabaseOfSessions.RemoveUserFromDatabase("testHandleAnswer");
//    }
//}
