package com.server;

import com.server.notificator.Notificator;
import net.fortuna.ical4j.model.Calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public final class GraphOfMessages {
    private GraphOfMessages() {
    }

    private final static HashMap<String, Consumer<User>> transitionDict;
    private final static Messages messageManager = new Messages();

    static {
        transitionDict = new HashMap<>();
        transitionDict.put("initialization", GraphOfMessages::onSessionInitialization);
        transitionDict.put("group addition", GraphOfMessages::onGroupAddition);
        transitionDict.put("get timetable", GraphOfMessages::onGetTimetable);
        transitionDict.put("get information about class", GraphOfMessages::onGetInformationAboutClass);
        transitionDict.put("get information about next class", GraphOfMessages::onGetInformationAboutClass);
        transitionDict.put("repeat answer", GraphOfMessages::onGetTimetable);
        transitionDict.put("invalid group", GraphOfMessages::onGroupAddition);
        transitionDict.put("invalid class index", GraphOfMessages::onGetTimetable);
        transitionDict.put("group success", GraphOfMessages::onGetTimetable);
        transitionDict.put("change notification advance time", GraphOfMessages::onChangeNotificationForSelectedDayAndLesson);
        transitionDict.put("success notification advance time input", GraphOfMessages::onGetTimetable);
        transitionDict.put("invalid notification advance time input", GraphOfMessages::onGetTimetable);
        transitionDict.put("add notification", GraphOfMessages::onNotificationAddition);
        transitionDict.put("delete notification", GraphOfMessages::onNotificationDeletion);
        transitionDict.put("success notification addition", GraphOfMessages::onGetTimetable);
        transitionDict.put("success notification deletion", GraphOfMessages::onGetTimetable);
        transitionDict.put("invalid notification addition", GraphOfMessages::onGetTimetable);
        transitionDict.put("invalid notification deletion", GraphOfMessages::onGetTimetable);
        transitionDict.put("delete all notification", GraphOfMessages::onAllNotificationDeletion);
        transitionDict.put("success all notification deletion", GraphOfMessages::onGetTimetable);
    }

    private static void onChangeNotificationForSelectedDayAndLesson(User user){
        try {
            var inputArray = user.lastAnswer.split(" ");
            var dayToChange = recognizeWeekDay(user.lastAnswer);
            if (dayToChange == "") {
                throw new Exception("Day hasn't been recognized");
            }

            var lessonNumber = Integer.parseInt(inputArray[0]);
            var userNotifications = user.notifications.Days;
            var currentDayNotifications = userNotifications.get(dayToChange);
            var lessonToChange = Lesson.findLesson(currentDayNotifications.Lessons, lessonNumber);

            if (lessonToChange == null) {
                throw new Exception("Lesson hasn't been found in DB");
            }

            var newTime = getNewNotificationTime(inputArray);
            lessonToChange.advanceTime = newTime;


            user.defaultNotificationAdvanceTime = newTime;
            user.nextMessage = messageManager.successNotificationAdvanceTimeInput;
            user.nextMessage.question = String.format(user.nextMessage.question, newTime);
        } catch (Exception e) {
            user.nextMessage = messageManager.invalidNotificationAdvanceTimeInput;
        }
    }

    private static int getNewNotificationTime(String[] inputArray){
        var index = 1;
        for (var word:inputArray)
        {
            if (word.equals("на")) {
                break;
            }
            index++;
        }
        return Integer.parseInt(inputArray[index]);
    }

    private static void onNotificationOnLessonAddition(User user, int lesson) {
        String[] days = {"Воскресенье", "Понедельник", "Вторник", "Среда", "Четверг",
                "Пятница", "Суббота"};
        for (var day : days) {
            try {
                Notificator.addNewNotificationAboutLesson(user, day, lesson);
            } catch (Exception e) {
            }
        }
        user.nextMessage = messageManager.successNotificationAddition;
        user.nextMessage.question =
                String.format(
                        "С этого момента вы будете получать оповещения перед %s парой",
                        lesson);
    }

    private static void onNotificationOnDayAddition(User user, String day) {
        var timeTable = Notificator.getDataBase(day, user);
        for (var lesson = 1; lesson <= timeTable.size(); lesson++) {
            try {
                Notificator.addNewNotificationAboutLesson(user, day, lesson);
            } catch (Exception e) {
            }
        }
        user.nextMessage = messageManager.successNotificationAddition;
        user.nextMessage.question =
                String.format("С этого момента вы будете получать оповещения в %s",
                        day.toLowerCase());
    }

    private static void onNotificationAddition(User user) {
        var classNum = 0;
        try {
            classNum = Integer.parseInt(user.lastAnswer.replaceAll("\\D+", ""));
        } catch (Exception e) {
        }
        var day = recognizeWeekDay(user.lastAnswer);

        if (day.equals("") && classNum == 0) {
            user.nextMessage = messageManager.invalidNotificationAddition;
        } else if (day.equals("")) {
            onNotificationOnLessonAddition(user, classNum);
        } else if (classNum == 0) {
            onNotificationOnDayAddition(user, day);
        } else {
            try {
                Notificator.addNewNotificationAboutLesson(user, day, classNum);
            } catch (Exception e) {
            }
            user.nextMessage = messageManager.successNotificationAddition;
            user.nextMessage.question =
                    String.format(
                            "С этого момента вы будете получать оповещения во %s перед %s парой",
                            day.toLowerCase(),
                            String.valueOf(classNum));
        }
    }

    private static void onNotificationDeletion(User user) {
        var classNum = 1;
        try {
            classNum = Integer.parseInt(user.lastAnswer.replaceAll("\\D+", ""));
        } catch (Exception e) {
            user.nextMessage = messageManager.invalidNotificationDeletion;
            return;
        }

        var day = recognizeWeekDay(user.lastAnswer);
        if (day.equals("")) {
            user.nextMessage = messageManager.invalidNotificationDeletion;
            return;
        }
        try {
            Notificator.deleteNotificationAboutLesson(user, day, classNum);
        } catch (Exception e) {
        }
        user.nextMessage = messageManager.successNotificationDeletion;
        user.nextMessage.question = String.format(user.nextMessage.question, day, String.valueOf(classNum));
    }

    private static void onAllNotificationDeletion(User user) {
        if (user.lastAnswer.toLowerCase().equals("да")) {
            try {
                Notificator.cancelAllUserNotification(user.token);
            } catch (Exception e) {
            }
            user.nextMessage = messageManager.successAllNotificationDeletion;
        } else {
            user.nextMessage = messageManager.successAllNotificationDeletion;
            user.nextMessage.question = "Оповещения не были удалены.";
        }
    }

    private static boolean transitToAnyNodes(User user) {
        if (checkContain("добавить", user.lastAnswer) &&
                checkContain("оповещение", user.lastAnswer)) {
            user.nextMessage = messageManager.addNotification;
            return true;
        } else if (checkContain("удалить", user.lastAnswer) &&
                checkContain("все", user.lastAnswer) &&
                checkContain("оповещения", user.lastAnswer)) {
            user.nextMessage = messageManager.deleteAllNotification;
            return true;
        } else if (checkContain("удалить", user.lastAnswer) &&
                checkContain("оповещение", user.lastAnswer)) {
            user.nextMessage = messageManager.deleteNotification;
            return true;
        } else if (checkContain("поменять", user.lastAnswer) &&
                checkContain("время", user.lastAnswer) &&
                checkContain("оповещения", user.lastAnswer)) {
            user.nextMessage = messageManager.changeNotificationAdvanceTime;
            return true;

        } else if (checkContain("группа", user.lastAnswer) && checkContain("сменить", user.lastAnswer)) {
            user.nextMessage = messageManager.addGroupToUser;
            return true;

        } else
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
                        user.lastDayRequest + " " + user.lastClassNumRequest, user);
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
        user.handle = user.lastAnswer;
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

    private static String getTimetableOnDate(String date, User user) {
//        Calendar calendarStr = TimetableParsing.ReadFile("./DataBase/calendar_fiit_202.ics");

        var calendarStr = TimetableParsing.getTimetableFromUrfuApi(user.group.id);
        var cal = TimetableParsing.CreateTimeTableDataBase(calendarStr);
        return cal.get(date).stream()
                .map(subject -> reformatTime(subject.lessonStartTime) + ": " + subject.lessonName)
                .collect(Collectors.joining("\n"));
    }

    private static String getInformationAboutClass(String time, User user) {
        var timeDict = time.split(" ");
        var day = timeDict[0];
        var classNumber = Integer.parseInt(timeDict[1]);

        var calendarStr = TimetableParsing.getTimetableFromUrfuApi(user.group.id);
//        Calendar calendarStr = TimetableParsing.ReadFile("DataBase/calendar_fiit_202.ics");
        var cal = TimetableParsing.CreateTimeTableDataBase(calendarStr);
        var dayCal = cal.get(day);
        if (dayCal.size() < classNumber)
            return null;

        var subj = dayCal.get(classNumber - 1);

        return String.format("%s\nНачало: %s\nКонец: %s\nАудитория: %s\nПреподаватель: %s",
                subj.lessonName,
                reformatTime(subj.lessonStartTime),
                reformatTime(subj.lessonEndTime),
                subj.rooms.stream().collect(Collectors.joining(", ")),
                subj.teachers.stream().collect(Collectors.joining(", ")));

    }

    private static String reformatTime(String time){
        SimpleDateFormat inputTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat outputTimeFormat = new SimpleDateFormat("HH:mm");
        String result = "";
        try {
            var inputTime = inputTimeFormat.parse(time);
            result = outputTimeFormat.format(inputTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
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
        if (shortest >= 3)
            return "";
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
        user.nextMessage.question = getTimetableOnDate(date, user) + "\n\nХотите узнать еще что-нибудь?";

        return true;
    }

    private static boolean handleTimetableOnClass(User user) {
        var userInput = user.lastAnswer.toLowerCase();
        var classNum = userInput.replaceAll("\\D+", "");

        var date = recognizeWeekDay(userInput);


        if (date.equals("") || classNum.equals(""))
            return false;

        user.lastDayRequest = date;
        user.lastClassNumRequest = Integer.parseInt(classNum);
        user.nextMessage = messageManager.getInformationAboutClass;

        var classInfo = getInformationAboutClass(date + " " + classNum, user);
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
