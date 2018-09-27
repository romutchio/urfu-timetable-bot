package Server;

import java.util.HashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public final class GraphOfMessages {
    public GraphOfMessages() {
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

    private static void graphInit() {
        transitionDict = new HashMap<String, Consumer<User>>();
        transitionDict.put("initialization", GraphOfMessages::onSessionInitialization);
        transitionDict.put("group addition", GraphOfMessages::onGroupAddition);
        transitionDict.put("get timetable", GraphOfMessages::transitToAnyNodes);
        transitionDict.put("get information about class", GraphOfMessages::onGetInformationAboutClass);
        transitionDict.put("get information about next class", GraphOfMessages::onGetInformationAboutClass);
    }

    private static String getTimetableOnDate(String date) {
        var calendarStr = TimetableParsing.ReadFile("./QuizBot/DataBase/calendar_fiit_202.ics");
        var cal = TimetableParsing.CreateTimeTableDataBase(calendarStr);
        var calOnDate = cal.get(date).stream()
                .map(subject -> subject.lessonName)
                .collect(Collectors.joining("\n"));


        return calOnDate;
    }

    private static String getInformationAboutClass(String time) {
        var timeDict = time.split(" ");
        var day = timeDict[0];
        var classNumber = Integer.parseInt(timeDict[1]);

        var calendarStr = TimetableParsing.ReadFile("./QuizBot/DataBase/calendar_fiit_202.ics");
        var cal = TimetableParsing.CreateTimeTableDataBase(calendarStr);
        var dayCal = cal.get(day);
        var subj = dayCal.get(classNumber - 1);
//        return subj.lessonName + "\nНачало: " + subj.lessonStartTime;
        return subj.lessonName + "\nНачало: " + subj.lessonStartTime + "\nПреподаватель: " + subj.teacher;
    }

    private static boolean handleTimetableOnDate(User user) {
        if (!user.lastAnswer.toLowerCase().contains("расписание"))
            return false;

        var date = "";

        if (user.lastAnswer.toLowerCase().contains("понедельник"))
            date = "Понедельник";
        if (user.lastAnswer.toLowerCase().contains("вторник"))
            date = "Вторник";
        if (user.lastAnswer.toLowerCase().contains("среда"))
            date = "Среда";
        if (user.lastAnswer.toLowerCase().contains("четверг"))
            date = "Четверг";
        if (user.lastAnswer.toLowerCase().contains("пятница"))
            date = "Пятница";
        if (user.lastAnswer.toLowerCase().contains("суббота"))
            date = "Суббота";
        if (user.lastAnswer.toLowerCase().contains("воскресение"))
            date = "Воскресение";

        if (date.equals(""))
            return false;

        user.nextMessage = getTimetableOnDate;
        user.nextMessage.question = getTimetableOnDate(date) + "\n\nХотите узнать еще что-нибудь?";

        return true;
    }

    private static boolean handleTimetableOnClass(User user) {
        if (!user.lastAnswer.toLowerCase().contains("пара"))
            return false;

        var date = "";
        var classNum = "";

        if (user.lastAnswer.toLowerCase().contains("понедельник"))
            date = "Понедельник";
        if (user.lastAnswer.toLowerCase().contains("вторник"))
            date = "Вторник";
        if (user.lastAnswer.toLowerCase().contains("среда"))
            date = "Среда";
        if (user.lastAnswer.toLowerCase().contains("четверг"))
            date = "Четверг";
        if (user.lastAnswer.toLowerCase().contains("пятница"))
            date = "Пятница";
        if (user.lastAnswer.toLowerCase().contains("суббота"))
            date = "Суббота";
        if (user.lastAnswer.toLowerCase().contains("воскресение"))
            date = "Воскресение";

        if (user.lastAnswer.toLowerCase().contains("1"))
            classNum = "1";
        if (user.lastAnswer.toLowerCase().contains("2"))
            classNum = "2";
        if (user.lastAnswer.toLowerCase().contains("3"))
            classNum = "3";
        if (user.lastAnswer.toLowerCase().contains("4"))
            classNum = "4";
        if (user.lastAnswer.toLowerCase().contains("5"))
            classNum = "5";
        if (user.lastAnswer.toLowerCase().contains("6"))
            classNum = "6";

        if (date.equals("") || classNum.equals(""))
            return false;

        user.lastDayRequest = date;
        user.lastClassNumRequest = Integer.parseInt(classNum);
        user.nextMessage = getInformationAboutClass;
        user.nextMessage.question = getInformationAboutClass(date + " " + classNum)
                + "\n\nХотите узнать еще что-нибудь?";

        return true;
    }

    private static boolean transitToAnyNodes(User user) {
        if (handleTimetableOnClass(user)) {
            DatabaseOfSessions.UpdateUserInDatabase(user);
            return true;
        }

        if (handleTimetableOnDate(user)) {
            DatabaseOfSessions.UpdateUserInDatabase(user);
            return true;
        }

        return false;
    }

    private static void onGetInformationAboutClass(User user) {
        if (!transitToAnyNodes(user))
            if (user.lastAnswer.contains("следующая пара")) {
                user.nextMessage = getInformationAboutNextClass;

                user.nextMessage.question = getInformationAboutClass(
                        user.lastDayRequest + " " + user.lastClassNumRequest + 1);

                DatabaseOfSessions.UpdateUserInDatabase(user);
                return;
            }
        user.nextMessage = sessionInitialization;
        DatabaseOfSessions.UpdateUserInDatabase(user);
    }

    private static void onSessionInitialization(User user) {
        if (user.group == null)
            user.nextMessage = addGroupToUser;
        else {
            if (!transitToAnyNodes(user))
                user.nextMessage = sessionInitialization;
        }
        DatabaseOfSessions.UpdateUserInDatabase(user);
    }

    private static void onGroupAddition(User user) {
        user.group = AnswerValidator.RecognizeGroup(user.lastAnswer);
        if (!transitToAnyNodes(user))
            user.nextMessage = sessionInitialization;
        DatabaseOfSessions.UpdateUserInDatabase(user);
    }

    public static Consumer<User> getTransitionFunction(User user) {
        var id = user.nextMessage.operationIdentifier;
        return transitionDict.get(id);
    }

    public static Message getInitMessage() {
        return sessionInitialization;
    }

    public static Consumer<User> getTransit(String key) {
        return transitionDict.get(key);
    }
}
