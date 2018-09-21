import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;

import java.io.DataInput;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Dictionary;
import java.util.List;

public class DatabaseOfSessions {
    private static String ReadFile() {
        String content = null;
        try {
            String fileName = "./QuizBot/DataBase/Sessions.txt";
            content = Files.lines(Paths.get(fileName)).reduce("", String::concat);
        } catch (Exception e) {
            System.out.println(e);
        }
        return content;
    }

    public static Group GetGroupByUser(User user) {
        String rawJson = ReadFile();
        Gson gson = new Gson();
        Type type = new TypeToken<List<Group>>() {
        }.getType();
        Dictionary<User, Group> userGroupDictionary = gson.fromJson(rawJson, type);

        return userGroupDictionary.get(user);
    }

    public static void AddNewUserInDatabase(User user, Group group) throws Exception {
        String rawJson = ReadFile();
        Gson gson = new Gson();
        Type type = new TypeToken<List<Group>>() {
        }.getType();
        Dictionary<User, Group> userGroupDictionary = gson.fromJson(rawJson, type);

        userGroupDictionary.put(user, group);

        throw new Exception();
    }
}
