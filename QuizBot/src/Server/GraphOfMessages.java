package Server;

import java.util.HashMap;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public final class GraphOfMessages {
    public GraphOfMessages()
    {
        graphInit();
    }

    private static HashMap<String, Consumer<User>> transitionDict;

    private static Message sessionInitialization = new Message(
            "Доброго времени суток!\n" +
                    "Я чат-бот, который поможет тебе не пропустить пары\n" +
                    "и всегда иметь быстрый доступ к расписанию. Как твое имя?",
            "initialization");

    private static Message addGroupToUser = new Message(
            "Напиши свою группу в такой нотации -> 'МЕН-170810'",
            "group addition");

    private static Message getTimetableOnDate = new Message(
            "",
            "get timetable");

    private static Message getInformationAboutClass = new Message(
            "",
            "get information about class");

    private static Message getInformationAboutNextClass = new Message(
            "",
            "get information about next class");

    private static void graphInit()
    {
        transitionDict = new HashMap<String, Consumer<User>>();
        transitionDict.put("initialization", GraphOfMessages::onSessionInitialization);
        transitionDict.put("group addition", GraphOfMessages::onGroupAddition);
    }
    private static String getTimetableOnDate(String date)
    {
        var calendarStr = TimetableParsing.ReadFile("./QuizBot/DataBase/calendar.ics");
        var cal = TimetableParsing.CreateTimeTableDataBase(calendarStr);
        var calOnDate = cal.get(date).stream()
                .map(subject -> subject.lessonName)
                .collect(Collectors.joining("\n"));


        return calOnDate;
    }
    private static boolean transitToAnyNodes(User user)
    {
        if (user.lastAnswer.equals("расписание на четверг"))
        {
            user.nextMessage = getTimetableOnDate;
            user.nextMessage.question = getTimetableOnDate("Четверг");
            DatabaseOfSessions.UpdateUserInDatabase(user);
            return true;
        }

        if (user.lastAnswer.equals("какая первая пара в четверг"))
        {
            user.nextMessage = getInformationAboutClass;
            DatabaseOfSessions.UpdateUserInDatabase(user);
            return true;
        }

        return false;
    }
    private static void onSessionInitialization(User user)
    {
        if (user.group == null)
            user.nextMessage = addGroupToUser;
        else
        {
            if (!transitToAnyNodes(user))
                user.nextMessage = sessionInitialization;
        }
        DatabaseOfSessions.UpdateUserInDatabase(user);
    }

    private static void onGroupAddition(User user)
    {
        user.group = AnswerValidator.RecognizeGroup(user.lastAnswer);
        DatabaseOfSessions.UpdateUserInDatabase(user);
    }

    public static Consumer<User> getTransitionFunction(User user)
    {
        var id = user.nextMessage.operationIdentifier;
        return transitionDict.get(id);
    }

    public static Message getInitMessage()
    {
        return sessionInitialization;
    }
    public static Consumer<User> getTransit(String key)
    {
        return transitionDict.get(key);
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
