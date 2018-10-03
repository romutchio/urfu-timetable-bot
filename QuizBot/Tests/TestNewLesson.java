
import Server.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class TestNewLesson
{
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
    public void test9thlesson() {
        var message = new Message(
                "",
                "get information about class");
        initGraphOfMessages(message);

        var answ = AnswerHandler.handleAnswer(
                "testHandleAnswer",
                "вторник 1 пара");

        assertEquals("Компьютерные сети\n" +
                        "Начало: 12:50\n" +
                        "Конец: 14:20\n" +
                        "Преподаватель: Берсенев Александр Юрьевич\n\n" +
                        "Хотите узнать еще что-нибудь?",
                answ);

        DatabaseOfSessions.RemoveUserFromDatabase("testHandleAnswer");
    }
}
