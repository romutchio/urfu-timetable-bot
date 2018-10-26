package com.clients;


import com.server.*;
import com.server.notificator.NotificationManager;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class TestNewLesson {
    public void initGraphOfMessages(Message nextMessage) {
        var user = new User("testHandleAnswer",
                new Group(false,
                        "МЕН-180101",
                        1,
                        978680,
                        35175,
                        true,
                        25714),
                nextMessage,
                "",
                new NotificationManager());

        user.lastDayRequest = "Вторник";
        user.lastClassNumRequest = 1;

        DatabaseOfSessions.AddNewUserInDatabase(user);
    }

    @Test
    public void test9thlesson() {
        var message = new Message(
                "",
                "get information about class");
        initGraphOfMessages(message);

        var answ = AnswerHandler.handleAnswer(
                "testHandleAnswer",
                "вторник 1 пара");

        assertEquals(197,
                answ.length());

        DatabaseOfSessions.RemoveUserFromDatabase("testHandleAnswer");
    }
}
