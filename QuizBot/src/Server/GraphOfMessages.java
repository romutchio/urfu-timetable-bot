package Server;

import java.util.HashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class GraphOfMessages {
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

    private static Message repeatAnswer = new Message(
            "Я вас не понял, повторите пожалуйста.",
            "repeat answer");

    private static Message invalidGroup = new Message(
            "К сожалению такой группы не существует. Попрубуйте ввести группу еще раз.",
            "invalid group");

    private static void graphInit() {
        transitionDict = new HashMap<String, Consumer<User>>();
        transitionDict.put("initialization", GraphOfMessages::onSessionInitialization);
        transitionDict.put("group addition", GraphOfMessages::onGroupAddition);
        transitionDict.put("get timetable", GraphOfMessages::onGetTimetable);
        transitionDict.put("get information about class", GraphOfMessages::onGetInformationAboutClass);
        transitionDict.put("get information about next class", GraphOfMessages::onGetInformationAboutClass);
        transitionDict.put("repeat answer", GraphOfMessages::transitToAnyNodes);
        transitionDict.put("invalid group", GraphOfMessages::onGroupAddition);
    }

    private static boolean transitToAnyNodes(User user) {
        if (handleTimetableOnClass(user)) {
            return true;
        }

        if (handleTimetableOnDate(user)) {
            return true;
        }

        return false;
    }

    private static void onGetTimetable(User user) {
        if (!transitToAnyNodes(user))
            user.nextMessage = repeatAnswer;
    }

    private static void onGetInformationAboutClass(User user) {
        if (!transitToAnyNodes(user))
            if (user.lastAnswer.contains("следующая пара")) {
                user.nextMessage = getInformationAboutNextClass;
                user.lastClassNumRequest++;

                user.nextMessage.question = getInformationAboutClass(
                        user.lastDayRequest + " " + user.lastClassNumRequest) +
                        "\n\nХотите узнать еще что-нибудь?";
                return;
            }
        user.nextMessage = repeatAnswer;
    }

    private static void onSessionInitialization(User user) {
        if (user.group == null)
            user.nextMessage = addGroupToUser;
        else {
            if (!transitToAnyNodes(user))
                user.nextMessage = sessionInitialization;
        }
    }

    private static void onGroupAddition(User user) {
        user.group = AnswerValidator.RecognizeGroup(user.lastAnswer);
        if (!transitToAnyNodes(user))
            user.nextMessage = invalidGroup;
    }

    private static String getTimetableOnDate(String date) {
        var calendarStr = TimetableParsing.ReadFile("./QuizBot/DataBase/calendar_fiit_202.ics");
        var cal = TimetableParsing.CreateTimeTableDataBase(calendarStr);
        return cal.get(date).stream()
                .map(subject -> subject.lessonStartTime + ": " + subject.lessonName)
                .collect(Collectors.joining("\n"));
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
        return String.format("%s\nНачало: %s\nКонец: %s\nПреподаватель: %s",
                subj.lessonName,
                subj.lessonStartTime,
                subj.lessonEndTime,
                subj.teachers.stream().collect(Collectors.joining(", ")));
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

    private static boolean almostEqual(String s1, String s2, int dist) {
        return levensteinDist(s1, s1) <= 3;
    }

    private static int levensteinDist(String S1, String S2) {
        var m = S1.length();
        var n = S2.length();
        int[] D1;
        int[] D2 = new int[n + 1];

        for (int i = 0; i <= n; i++)
            D2[i] = i;

        for (int i = 1; i <= m; i++) {
            D1 = D2;
            D2 = new int[n + 1];
            for (int j = 0; j <= n; j++) {
                if (j == 0) D2[j] = i;
                else {
                    int cost = (S1.charAt(i - 1) != S2.charAt(j - 1)) ? 1 : 0;
                    if (D2[j - 1] < D1[j] && D2[j - 1] < D1[j - 1] + cost)
                        D2[j] = D2[j - 1] + 1;
                    else if (D1[j] < D1[j - 1] + cost)
                        D2[j] = D1[j] + 1;
                    else
                        D2[j] = D1[j - 1] + cost;
                }
            }
        }
        return D2[n];
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
