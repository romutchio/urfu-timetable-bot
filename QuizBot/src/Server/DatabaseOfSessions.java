package Server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

    private static HashMap<String, User> getDatabaseOfUsers()
    {
        var rawJson = ReadFile();
        var gson = new Gson();

        var type = new TypeToken<HashMap<String, User>>() {
        }.getType();
        return gson.fromJson(rawJson, type);

    }
    public static User GetUserByUsername(String username)
    {
        var userDatabase = getDatabaseOfUsers();
        return userDatabase.get(username);
    }

    public static void AddNewUserInDatabase(String userHandle) {
        var gson = new Gson();
        var userDatabase = getDatabaseOfUsers();
        userDatabase.put(userHandle, new User(userHandle));
        WriteFile(gson.toJson(userDatabase));
    }
    public static void UpdateUserInDatabase(User user) {
        var gson = new Gson();
        var userDatabase = getDatabaseOfUsers();

        userDatabase.remove(user.handle);
        userDatabase.put(user.handle, user);
        WriteFile(gson.toJson(userDatabase));
    }

    public static void RemoveUserFromDatabase(String userHandle) {
        Gson gson = new Gson();
        HashMap<String, User> userDatabase = getDatabaseOfUsers();
        userDatabase.remove(userHandle);
        WriteFile(gson.toJson(userDatabase));
    }
}
