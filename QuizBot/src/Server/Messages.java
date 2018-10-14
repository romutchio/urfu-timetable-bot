package Server;

public class Messages {
    public Messages(){

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

    public  Message getInformationAboutClass = new Message(
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
}
