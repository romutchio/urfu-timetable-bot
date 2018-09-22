import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class DatabaseOfSessions {
    private static String ReadFile() {
        String content = null;
        try {
            String fileName = "./QuizBot/DataBase/Sessions.json";
            content = Files.lines(Paths.get(fileName)).reduce("", String::concat);
        } catch (Exception e) {
            System.out.println(e);
        }
        return content;
    }

    private static void WriteFile(String textToWrite) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("./QuizBot/DataBase/Sessions.json", "UTF-8");
            writer.println(textToWrite);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static Group GetGroupByUser(String userHandle) {
        var rawJson = ReadFile();
        var gson = new Gson();
        var type = new TypeToken<HashMap<String, Group>>() {
        }.getType();
        HashMap<String, Group> userGroupDictionary = gson.fromJson(rawJson, type);
        if (userGroupDictionary.isEmpty()) {
            return null;
        }
        if (!userGroupDictionary.containsKey(userHandle))
            return null;

        return userGroupDictionary.get(userHandle);
    }

    public static void AddNewUserInDatabase(String userHandle, Group group) {
        String rawJson = ReadFile();
        Gson gson = new Gson();

        Type type = new TypeToken<HashMap<String, Group>>() {
        }.getType();
        HashMap<String, Group> userGroupDictionary = gson.fromJson(rawJson, type);

        userGroupDictionary.put(userHandle, group);
        var jsonToWrite = gson.toJson(userGroupDictionary);

        WriteFile(jsonToWrite);
    }

    public static void RemoveUserFromDatabase(String userHandle) {
        String rawJson = ReadFile();
        Gson gson = new Gson();

        Type type = new TypeToken<HashMap<String, Group>>() {
        }.getType();
        HashMap<String, Group> userGroupDictionary = gson.fromJson(rawJson, type);

        userGroupDictionary.remove(userHandle);
        var jsonToWrite = gson.toJson(userGroupDictionary);

        WriteFile(jsonToWrite);
    }
}
