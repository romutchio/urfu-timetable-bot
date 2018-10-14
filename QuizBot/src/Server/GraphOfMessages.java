package Server;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class GraphOfMessages {
    public GraphOfMessages() {
        graphInit();
    }

    private static HashMap<String, Consumer<User>> transitionDict;
    private static Messages messageManager = new Messages();

    private static void graphInit() {
        transitionDict = new HashMap<>();
        transitionDict.put("initialization", GraphOfMessages::onSessionInitialization);
        transitionDict.put("group addition", GraphOfMessages::onGroupAddition);
        transitionDict.put("get timetable", GraphOfMessages::onGetTimetable);
        transitionDict.put("get information about class", GraphOfMessages::onGetInformationAboutClass);
        transitionDict.put("get information about next class", GraphOfMessages::onGetInformationAboutClass);
        transitionDict.put("repeat answer", GraphOfMessages::transitToAnyNodes);
        transitionDict.put("invalid group", GraphOfMessages::onGroupAddition);
        transitionDict.put("invalid class index", GraphOfMessages::transitToAnyNodes);
        transitionDict.put("group success", GraphOfMessages::onGetTimetable);
        transitionDict.put("change notification advance time", GraphOfMessages::onChangeNotificationAdvanceTime);
        transitionDict.put("notification advance time input", GraphOfMessages::onNotificationAdvanceTimeInput);
        transitionDict.put("invalid notification advance time input", GraphOfMessages::onNotificationAdvanceTimeInput);
    }

    private static void onNotificationAdvanceTimeInput(User user) {
        try {
            user.notificationAdvanceTime = Integer.parseInt(user.lastAnswer);
            transitToAnyNodes(user);// не работает переход
        } catch (Exception e) {
            user.nextMessage = messageManager.invalidNotificationAdvanceTimeInput;
        }
    }

    private static void onChangeNotificationAdvanceTime(User user) {
        user.nextMessage = messageManager.notificationAdvanceTimeInput;
    }

    private static boolean transitToAnyNodes(User user) {
//        if (checkContain("поменять оповещение", user.lastAnswer))
        if (checkContain("оповещение", user.lastAnswer))

            user.nextMessage = messageManager.changeNotificationAdvanceTime;
        return handleTimetableOnClass(user) || handleTimetableOnDate(user);
    }

    private static void onGetTimetable(User user) {
        if (!transitToAnyNodes(user))
            user.nextMessage = messageManager.repeatAnswer;
    }

    private static void onGetInformationAboutClass(User user) {
        var answer = user.lastAnswer;
        if (!transitToAnyNodes(user)) {
            if (checkContain("следующая", answer)) {
                user.nextMessage = messageManager.getInformationAboutNextClass;
                user.lastClassNumRequest++;

                var classInfo = getInformationAboutClass(
                        user.lastDayRequest + " " + user.lastClassNumRequest);
                if (classInfo == null) {
                    user.nextMessage = messageManager.invalidClassIndex;
                    return;
                }
                user.nextMessage.question = classInfo + "\n\nХотите узнать еще что-нибудь?";

            } else {
                user.nextMessage = messageManager.repeatAnswer;
            }
        }
    }

    private static void onSessionInitialization(User user) {
        if (user.group == null)
            user.nextMessage = messageManager.addGroupToUser;
        else {
            if (!transitToAnyNodes(user))
                user.nextMessage = messageManager.sessionInitialization;
        }
    }

    private static void onGroupAddition(User user) {
        var group = AnswerValidator.RecognizeGroup(user.lastAnswer);
        if (group == null)
            user.nextMessage = messageManager.invalidGroup;
        else {
            user.group = group;
            user.nextMessage = messageManager.successGroupAddition;
        }
    }

    private static String getTimetableOnDate(String date) {
        var calendarStr = TimetableParsing.ReadFile("./DataBase/calendar_fiit_202.ics");
        var cal = TimetableParsing.CreateTimeTableDataBase(calendarStr);
        return cal.get(date).stream()
                .map(subject -> subject.lessonStartTime + ": " + subject.lessonName)
                .collect(Collectors.joining("\n"));
    }

    private static String getInformationAboutClass(String time) {
        var timeDict = time.split(" ");
        var day = timeDict[0];
        var classNumber = Integer.parseInt(timeDict[1]);


        var calendarStr = TimetableParsing.ReadFile("./DataBase/calendar_fiit_202.ics");
        var cal = TimetableParsing.CreateTimeTableDataBase(calendarStr);
        var dayCal = cal.get(day);
        if (dayCal.size() < classNumber)
            return null;

        var subj = dayCal.get(classNumber - 1);
//        return subj.lessonName + "\nНачало: " + subj.lessonStartTime;

        return String.format("%s\nНачало: %s\nКонец: %s\nАудитория: %s\nПреподаватель: %s",
                subj.lessonName,
                subj.lessonStartTime,
                subj.lessonEndTime,
                subj.rooms.stream().collect(Collectors.joining(", ")),
                subj.teachers.stream().collect(Collectors.joining(", ")));

    }

    private static String recognizeWeekDay(String input) {
        String date = "";
        String[] words = {"Воскресенье", "Понедельник", "Вторник", "Среда", "Четверг",
                "Пятница", "Суббота"};
        int shortest = Integer.MAX_VALUE;
        var userInput = input.split(" ");
        for (var word : userInput) {
            for (var day : words) {
                var dist = levensteinDist(day, word);
                if (dist < shortest) {
                    shortest = dist;
                    date = day;
                }
            }
        }
        return date;
    }

    private static boolean checkContain(String expected, String input) {
        var words = input.split(" ");
        int shortestDist = Integer.MAX_VALUE;
        for (var word : words) {
            var dist = levensteinDist(expected, word);
            if (dist < shortestDist) {
                shortestDist = dist;
            }
        }
        return (shortestDist < 4);
    }

    private static boolean handleTimetableOnDate(User user) {
        var userInput = user.lastAnswer.toLowerCase();
        var date = recognizeWeekDay(userInput);
        if (!checkContain("расписание", userInput))
            return false;

        if (date.equals(""))
            return false;

        user.nextMessage = messageManager.getTimetableOnDate;
        user.nextMessage.question = getTimetableOnDate(date) + "\n\nХотите узнать еще что-нибудь?";

        return true;
    }

    private static boolean handleTimetableOnClass(User user) {
        var userInput = user.lastAnswer.toLowerCase();
        var classNum = userInput.replaceAll("\\D+", "");
        ;
        var date = recognizeWeekDay(userInput);


        if (date.equals("") || classNum.equals(""))
            return false;

        user.lastDayRequest = date;
        user.lastClassNumRequest = Integer.parseInt(classNum);
        user.nextMessage = messageManager.getInformationAboutClass;

        var classInfo = getInformationAboutClass(date + " " + classNum);
        if (classInfo == null) {
            user.nextMessage = messageManager.invalidClassIndex;
            return true;
        }

        user.nextMessage.question = classInfo + "\n\nХотите узнать еще что-нибудь?";
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
        return messageManager.sessionInitialization;
    }

    public static Consumer<User> getTransit(String key) {
        return transitionDict.get(key);
    }
}
