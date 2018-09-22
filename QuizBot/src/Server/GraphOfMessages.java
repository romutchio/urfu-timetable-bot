package Server;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class GraphOfMessages {
    public Message SessionInitialization()
    {
        var response = new Message(
                "Доброго времени суток!\n" +
                "Я чат-бот, который поможет тебе не пропустить пары\n" +
                "и всегда иметь быстрый доступ к расписанию. Как твое имя?",
                new ArrayList<String>());

        return response;
    }

    public void HandleSessionInitializationAnswer(String answer)
    {

    }
    public class Messages {


        public String Welcome =
                "Доброго времени суток, %username%!\n" +
                        "Я чат-бот, который поможет тебе не пропустить пары\n" +
                        "и всегда иметь быстрый доступ к расписанию. Приступим?";

        public String GroupQuestion =
                "Напиши свою группу в такой нотации -> 'МЕН-170810'";

        public String GroupSelection =
                "Расписание для группы {%s} было успешно загружено.\n" +
                        "Пример для просмотра расписания: /1 - Понедельник";

        public String Decline =
                "Ну как хочешь, запоминай расписание сам ¯\\_(ツ)_/¯";
    }

}
