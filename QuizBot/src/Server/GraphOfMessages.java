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

    private static void onSessionInitialization(String username)
    {
        var userGroup = DatabaseOfSessions.GetGroupByUser(username);
        if (userGroup == null)
        {
            var user = new User(username, null, addGroupToUser);
        }
        else
        {
            var user = new User(username, userGroup, null);
        }
    }

    private static void onGroupAddition(String username)
    {

        var group = AnswerValidator.RecognizeGroup(groupStr);
        DatabaseOfSessions.AddNewUserInDatabase(username, group);
    }
    public Message initializeSession()
    {
        var response = sessionInitialization;
        return sessionInitialization;
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
