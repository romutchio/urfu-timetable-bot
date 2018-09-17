public class Messages {
    public String Welcome(){
        return "Доброго времени суток, %username%!\nЯ чат-бот, который поможет тебе не пропустить пары\n" +
                "и всегда иметь быстрый доступ к расписанию. Приступим?";
    }
    public String GroupQuestion(){
        return "Напиши свою группу в такой нотации -> 'МЕН-170810'";
    }

    public String GroupSelection(Group group){
        if (group != null)
            return String.format("Расписание для группы {%s} было успешно загружено. Пример для просмотра расписания: /1 - Понедельник", group.title);
        else return "Что-то пошло не так. Желаете повторить попытку?";
    }

    public String Decline(){
        return "Ну как хочешь, запоминай расписание сам ¯\\_(ツ)_/¯";
    }
}
