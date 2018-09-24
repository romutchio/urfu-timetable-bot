package Server;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Function;

public class GraphOfMessages {
    public User user = new User("vaspahomov", null, null);

    public static Message sessionInitialization = new Message(
            "Доброго времени суток!\n" +
                    "Я чат-бот, который поможет тебе не пропустить пары\n" +
                    "и всегда иметь быстрый доступ к расписанию. Как твое имя?",
            GraphOfMessages::onSessionInitialization);

    public static Message addGroupToUser = new Message(
            "Напиши свою группу в такой нотации -> 'МЕН-170810'",
            GraphOfMessages::onGroupAddition);

    private static void onSessionInitialization(User user)
    {
        if (user.group == null)
            user.nextMessage = addGroupToUser;
        else
            user.nextMessage = null;
        DatabaseOfSessions.UpdateUserInDatabase(user);
    }

    private static void onGroupAddition(User user)
    {
        var group = AnswerValidator.RecognizeGroup(user.lastAnswer);
        user.group = group;
        DatabaseOfSessions.UpdateUserInDatabase(user);
    }

    public static String initializeSession(String username)
    {
        return sessionInitialization.question;
    }

    public static String HandleAnswer(String username, String answer)
    {
        var user = DatabaseOfSessions.GetUserByUsername(username);
        user.nextMessage.answerValidator.accept(user);
        var message = user.nextMessage;
        return message.question;
    }

    public void handleSessionInitializationAnswer(String answer)
    {

    }
    public static class Messages {


        public static String welcome =
                "Доброго времени суток, %username%!\n" +
                        "Я чат-бот, который поможет тебе не пропустить пары\n" +
                        "и всегда иметь быстрый доступ к расписанию. Приступим?";

        public static String groupQuestion =
                "Напиши свою группу в такой нотации -> 'МЕН-170810'";

        public static String groupSelection =
                "Расписание для группы {%s} было успешно загружено.\n" +
                        "Пример для просмотра расписания: /1 - Понедельник";

        public static String decline =
                "Ну как хочешь, запоминай расписание сам ¯\\_(ツ)_/¯";
    }

}
