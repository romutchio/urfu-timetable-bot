import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import Server.*;


import org.junit.Test;


public class TestHandler {
    public void initGraphOfMessages(Message nextMessage) {
        new GraphOfMessages();
        var user = new User("testHandleAnswer",
                new Group(false,
                        "МЕН-180101",
                        1,
                        978680,
                        35175,
                        true,
                        25714),
                nextMessage,
                "");

        user.lastDayRequest = "Вторник";
        user.lastClassNumRequest = 1;

        DatabaseOfSessions.AddNewUserInDatabase(user);
    }

    @Test
    public void testHandleGetTimetableOnDate() {
        var message = new Message(
                "Я вас не понял, повторите пожалуйста",
                "repeat answer");
        initGraphOfMessages(message);
        var answ = AnswerHandler.handleAnswer(
                "testHandleAnswer",
                "расписание на понедельник");
        assertEquals("09:00: Дискретная математика\n" +
                        "10:40: Экономическая теория\n" +
                        "12:50: Объектно-ориентированное программирование\n\n" +
                        "Хотите узнать еще что-нибудь?",
                answ);

        DatabaseOfSessions.RemoveUserFromDatabase("testHandleAnswer");
    }

    @Test
    public void testHandleGetInformationAboutClass() {
        var message = new Message(
                "Я вас не понял, повторите пожалуйста",
                "repeat answer");

        initGraphOfMessages(message);

        var answ = AnswerHandler.handleAnswer(
                "testHandleAnswer",
                "1 пара вторник");

        assertEquals("Объектно-ориентированное программирование\n" +
                        "Начало: 10:40\n" +
                        "Конец: 12:10\n" +
                        "Преподаватель: null, Егоров Павел Владимирович\n\n" +
                        "Хотите узнать еще что-нибудь?",
                answ);

        DatabaseOfSessions.RemoveUserFromDatabase("testHandleAnswer");
    }

    @Test
    public void testHandleGetInformationAboutNextClass() {
        var message = new Message(
                "",
                "get information about class");
        initGraphOfMessages(message);

        var answ = AnswerHandler.handleAnswer(
                "testHandleAnswer",
                "следующая пара");

        assertEquals("Компьютерные сети\n" +
                        "Начало: 12:50\n" +
                        "Конец: 14:20\n" +
                        "Преподаватель: Берсенев Александр Юрьевич\n\n" +
                        "Хотите узнать еще что-нибудь?",
                answ);

        DatabaseOfSessions.RemoveUserFromDatabase("testHandleAnswer");
    }
}
