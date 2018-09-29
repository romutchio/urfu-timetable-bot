import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import Server.*;

import org.junit.Test;


public class TestHandler {

    @Test
    public void testHandleAnswer() {
        DatabaseOfSessions mock = mock(DatabaseOfSessions.class);
        var user = new User("test",
                new Group(false,
                        "МЕН-180101",
                        1,
                        978680,
                        35175,
                        true,
                        25714),
                new Message(
                        "Я вас не понял, повторите пожалуйста",
                        "repeat answer"),
                "");

        when(DatabaseOfSessions.GetUserByUsername("vaspahomov")).thenReturn(user);

        doNothing().when(mock).UpdateUserInDatabase(user);

        var answ = AnswerHandler.handleAnswer(
                "vaspahomov",
                "расписание на понедельник");

        assertEquals(answ, "09:00: Дискретная математика\n" +
                "10:40: Экономическая теория\n" +
                "12:50: Объектно-ориентированное программирование");
    }

    @Test
    public void testTransitGetting() {
    }
}
