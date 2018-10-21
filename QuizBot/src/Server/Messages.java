package Server;

public class Messages {
    public Messages() {

    }

    public Message sessionInitialization = new Message(
            "Доброго времени суток!\n" +
                    "Я чат-бот, который поможет тебе не пропустить пары\n" +
                    "и всегда иметь быстрый доступ к расписанию. Как твое имя?",
            "initialization");

    public Message addGroupToUser = new Message(
            "Напиши свою группу в такой нотации -> 'МЕН-170810'",
            "group addition");

    public Message getTimetableOnDate = new Message(
            " ",
            "get timetable");

    public Message getInformationAboutClass = new Message(
            " ",
            "get information about class");

    public Message getInformationAboutNextClass = new Message(
            " ",
            "get information about next class");

    public Message repeatAnswer = new Message(
            "Я вас не понял, повторите пожалуйста.",
            "repeat answer");

    public Message successGroupAddition = new Message(
            "Расписание было успешно загружено",
            "group success");

    public Message invalidGroup = new Message(
            "К сожалению такой группы не существует. Попробуйте ввести группу еще раз.",
            "invalid group");

    public Message invalidClassIndex = new Message(
            "У вас сегодня меньше пар, можете отдыхать!",
            "invalid class index");

    public Message changeNotificationAdvanceTime = new Message(
            "За сколько минут до занятия вы хотите получать оповещение?",
            "change notification advance time");

    public Message successNotificationAdvanceTimeInput = new Message(
            "С этого момента оповещения о начале заняитий будут приходить за %s минут",
            "success notification advance time input");

    public Message invalidNotificationAdvanceTimeInput = new Message(
            "Я вас не понял, попробуйте ввести время еще раз.",
            "invalid notification advance time input");

    public Message addNotification = new Message(
            "Перед какой парой и в какой день хотите получать оповещение?",
            "add notification");

    public Message successNotificationAddition = new Message(
                "С этого момента вы будете получать оповещения во %s перед %s парой",
            "success notification addition");

    public Message invalidNotificationAddition = new Message(
            "Попробуйте ввести день и номер пары еще раз.",
            "invalid notification addition");

    public Message deleteNotification = new Message(
            "Перед какой парой и в какой день хотите удалить оповещение?",
            "delete notification");

    public Message successNotificationDeletion = new Message(
            "С этого момента вы не будете получать оповещения в %s перед %s парой",
            "success notification deletion");

    public Message invalidNotificationDeletion = new Message(
            "Попробуйте ввести день и номер пары еще раз.",
            "invalid notification deletion");

    public Message deleteAllNotification = new Message(
            "Напишите \"да\" если хотите удалить все оповещения",
            "delete all notification");

    public Message successAllNotificationDeletion = new Message(
            "Все оповещения были удалены",
            "success all notification deletion");

}
