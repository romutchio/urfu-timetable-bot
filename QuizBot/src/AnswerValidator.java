import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AnswerValidator {
    private static String ReadFile() {
        String content = null;
        try {
            String fileName = "./QuizBot/DataBase/Groups.txt";
            content = Files.lines(Paths.get(fileName)).reduce("", String::concat);
        } catch (Exception e) {
            System.out.println(e);
        }
        return content;
    }

    public static Group RecognizeGroup(String groupName) {
        String rawJson = ReadFile();
        Gson gson = new Gson();
        Type type = new TypeToken<List<Group>>() {
        }.getType();
        List<Group> listItemsDes = gson.fromJson(rawJson, type);
        return listItemsDes.stream().filter(item -> groupName.equals(item.title)).findFirst().orElse(null);
    }

}
