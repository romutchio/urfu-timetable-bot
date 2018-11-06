package com.clients;

import static org.junit.Assert.*;

import com.server.*;
import com.server.notificator.NotificationManager;
import org.junit.Test;


public class TestHandler {
    public void initGraphOfMessages(Message nextMessage) {
        DatabaseOfSessions.SessionsDataBase = "DataBase/SessionsTest.json";

        var user = new User("testHandleAnswer",
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

        user.lastDayRequest = "Вторник";
        user.lastClassNumRequest = 1;

        DatabaseOfSessions.AddNewUserInDatabase(user);
    }

    @Test
    public void testHandleGetTimetableOnDate() {
        DatabaseOfSessions.SessionsDataBase = "DataBase/SessionsTest.json";

        var message = new Message(
                "Я вас не понял, повторите пожалуйста",
                "repeat answer");
        initGraphOfMessages(message);
        var answ = AnswerHandler.handleAnswer(
                "testHandleAnswer",
                "расписание на понедельник");
        assertEquals(153,
                answ.length());

        DatabaseOfSessions.RemoveUserFromDatabase("testHandleAnswer");
    }

    @Test
    public void testHandleGetInformationAboutClass() {
        DatabaseOfSessions.SessionsDataBase = "DataBase/SessionsTest.json";
        var message = new Message(
                "Я вас не понял, повторите пожалуйста",
                "repeat answer");

        initGraphOfMessages(message);

        var answ = AnswerHandler.handleAnswer(
                "testHandleAnswer",
                "1 пара вторник");

        assertEquals(135,
                answ.length());

        DatabaseOfSessions.RemoveUserFromDatabase("testHandleAnswer");
    }

    @Test
    public void testHandleGetInformationAboutNextClass() {
        DatabaseOfSessions.SessionsDataBase = "DataBase/SessionsTest.json";
        var message = new Message(
                "",
                "get information about class");
        initGraphOfMessages(message);

        var answ = AnswerHandler.handleAnswer(
                "testHandleAnswer",
                "следующая пара");

        assertEquals(135,
                answ.length());

        DatabaseOfSessions.RemoveUserFromDatabase("testHandleAnswer");
    }
}
