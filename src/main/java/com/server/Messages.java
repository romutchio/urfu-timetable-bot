package com.server;

import java.util.ArrayList;

public class Messages {
    public Messages() {

    }

    public ArrayList<String> stndardButtons = new ArrayList<String>() {{
        add("Расписание на понедельник");
        add("Расписание на вторник");
        add("Расписание на среду");
        add("Расписание на четверг");
        add("Расписание на пятницу");
        add("Расписание на субботу");
        add("Добавить оповещение");
        add("Удалить оповещение");
        add("Удалить все оповещения");
        add("Изменить время оповещения");
        add("1 пара понедельник");
        add("1 пара вторник");
        add("1 пара среду");
        add("1 пара четверг");
        add("1 пара пятницу");
        add("1 пара субботу");
    }};

    public Message sessionInitialization = new Message(
            "Доброго времени суток!\n" +
                    "Я чат-бот, который поможет тебе не пропустить пары\n" +
                    "и всегда иметь быстрый доступ к расписанию. Как твое имя?",
            "initialization");

    public Message addGroupToUser = new Message(
            "Напиши свою группу в такой нотации -> 'МЕН-270810'",
            "group addition",
            new ArrayList<String>() {{
                add("МЕН-270810");
                add("МЕН-180810");
            }});

    public Message getTimetableOnDate = new Message(
            " ",
            "get timetable");

    public Message getInformationAboutClass = new Message(
            " ",
            "get information about class",
            new ArrayList<String>() {{
                this.addAll(stndardButtons);
                add("Следующая пара");
            }});

    public Message getInformationAboutNextClass = new Message(
            " ",
            "get information about next class",
            new ArrayList<String>() {{
                this.addAll(stndardButtons);
                add("Следующая пара");
            }});

    public Message repeatAnswer = new Message(
            "Я вас не понял, повторите пожалуйста.",
            "repeat answer");

    public Message successGroupAddition = new Message(
            "Расписание было успешно загружено",
            "group success",
            stndardButtons);

    public Message invalidGroup = new Message(
            "К сожалению такой группы не существует. Попробуйте ввести группу еще раз.",
            "invalid group");

    public Message invalidClassIndex = new Message(
            "У вас сегодня меньше пар, можете отдыхать!",
            "invalid class index");

    public Message changeNotificationAdvanceTime = new Message(
            "В какой день, перед каким занятием и на какое время вы хотите поменять оповещения?",
            "change notification advance time",
            new ArrayList<String>() {{
                add("1 пара понедельник на 10");
                add("2 пара понедельник на 10");
                add("1 пара вторник на 10");
            }});

    public Message successNotificationAdvanceTimeInput = new Message(
            "С этого момента оповещения о начале заняитий будут приходить за %s минут",
            "success notification advance time input",
            stndardButtons);

    public Message invalidNotificationAdvanceTimeInput = new Message(
            "Я вас не понял, время не было изменено",
            "invalid notification advance time input",
            stndardButtons);

    public Message addNotification = new Message(
            "Перед какой парой и в какой день хотите получать оповещение?",
            "add notification",
            new ArrayList<String>() {{
                add("1 пара понедельник");
                add("Все понедельники");
                add("1 пара");
            }});

    public Message successNotificationAddition = new Message(
            "С этого момента вы будете получать оповещения во %s перед %s парой",
            "success notification addition",
            stndardButtons);

    public Message invalidNotificationAddition = new Message(
            "Попробуйте ввести день и номер пары еще раз.",
            "invalid notification addition",
            stndardButtons);

    public Message deleteNotification = new Message(
            "Перед какой парой и в какой день хотите удалить оповещение?",
            "delete notification",
            new ArrayList<String>() {{
                add("1 пара понедельник");
                add("2 пара понедельник");
                add("1 пара вторник");
                add("1 пара среда");
            }});

    public Message successNotificationDeletion = new Message(
            "С этого момента вы не будете получать оповещения в %s перед %s парой",
            "success notification deletion",
            stndardButtons);

    public Message invalidNotificationDeletion = new Message(
            "Попробуйте ввести день и номер пары еще раз.",
            "invalid notification deletion",
            stndardButtons);

    public Message deleteAllNotification = new Message(
            "Напишите \"да\" если хотите удалить все оповещения",
            "delete all notification",
            new ArrayList<String>() {{
                add("Да");
                add("Нет");
            }});

    public Message successAllNotificationDeletion = new Message(
            "Все оповещения были удалены",
            "success all notification deletion",
            stndardButtons);

}
