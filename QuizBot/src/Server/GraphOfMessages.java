package Server;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Function;

public class GraphOfMessages {
//    public User user = new User("vaspahomov", null, null);

    public static Message sessionInitialization = new Message(
            "Доброго времени суток!\n" +
                    "Я чат-бот, который поможет тебе не пропустить пары\n" +
                    "и всегда иметь быстрый доступ к расписанию. Как твое имя?",
            0);

    public static Message addGroupToUser = new Message(
            "Напиши свою группу в такой нотации -> 'МЕН-170810'",
            1);

    private static void onSessionInitialization(User user)
    {
        if (user.group == null)
            user.nextMessage = addGroupToUser;
        else
            user.nextMessage = sessionInitialization;
        DatabaseOfSessions.UpdateUserInDatabase(user);
    }

    private static void onGroupAddition(User user)
    {
        var group = AnswerValidator.RecognizeGroup(user.lastAnswer);
        user.group = group;
        DatabaseOfSessions.UpdateUserInDatabase(user);
    }

    public static String initializeSession()
    {
        Message mes = sessionInitialization;
        System.out.println(mes.question);
        Scanner in = new Scanner(System.in);
        String username = in.nextLine();
        User user;
        if (DatabaseOfSessions.Contains(username)) {
            System.out.println("Here");
            user = DatabaseOfSessions.GetUserByUsername(username);
        }
        else
        {
            user = new User(username, new Group(), addGroupToUser, null);
            DatabaseOfSessions.AddNewUserInDatabase(user);
        }
        DatabaseOfSessions.UpdateUserInDatabase(user);

        return username;
    }

    public static String HandleAnswer(String username, String answer)
    {
        var user = DatabaseOfSessions.GetUserByUsername(username);
        user.lastAnswer = answer;
        var id = user.nextMessage.operationIdentifier;
        System.out.println(id);
        if (id ==0)
            onGroupAddition(user);
        else onGroupAddition(user);
        var message = user.nextMessage;
        DatabaseOfSessions.UpdateUserInDatabase(user);
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
