package Server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import flexjson.JSONSerializer;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class DatabaseOfSessions {
    private static String SessionsDataBase = "./DataBase/Sessions.json";
    
    private static String ReadFile() {
        String content = null;
        try {
            String fileName = "./DataBase/Sessions.json";
            content = Files.lines(Paths.get(fileName)).reduce("", String::concat);
        } catch (Exception e) {
            System.out.println(e);
        }
        return content;
    }

    private static void WriteFile(String filename, String textToWrite) {
        PrintWriter writer = null;
        
        try {
            writer = new PrintWriter(filename, "UTF-8");
            writer.println(textToWrite);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static HashMap<String, User> getDatabaseOfUsers()
    {
        var rawJson = ReadFile();
        var gson = new Gson();
//        var der = new JSONDeserializer<HashMap<String, User>>();
        var type = new TypeToken<HashMap<String, User>>() {
        }.getType();
//        System.out.println(rawJson);
//        return der.deserialize(rawJson);
        HashMap<String, User> dataBase = gson.fromJson(rawJson, type);
        return dataBase;
    }

    public static User GetUserByToken(String token)
    {
        var userDatabase = getDatabaseOfUsers();
        if (userDatabase != null && userDatabase.containsKey(token))
            return userDatabase.get(token);
        return null;
    }

    public static boolean Contains(String token)
    {
        var userDatabase = getDatabaseOfUsers();
        return userDatabase.containsKey(token);
    }

    public static void AddNewUserInDatabase(User user) {
//        var gson = new Gson();
        var ser = new JSONSerializer();
        var userDatabase = getDatabaseOfUsers();
        userDatabase.put(user.token, user);
        WriteFile(SessionsDataBase, ser.deepSerialize(userDatabase));
    }


    public static void UpdateUserInDatabase(User user) {
//        var gson = new Gson();
        var userDatabase = getDatabaseOfUsers();
        var ser = new JSONSerializer();
        userDatabase.remove(user.token);
        userDatabase.put(user.token, user);
        WriteFile(SessionsDataBase, ser.deepSerialize(userDatabase));
    }

    public static void RemoveUserFromDatabase(String token) {
        var ser = new JSONSerializer();
//        Gson gson = new Gson();
        HashMap<String, User> userDatabase = getDatabaseOfUsers();
        userDatabase.remove(token);
        WriteFile(SessionsDataBase, ser.deepSerialize(userDatabase));
    }
}
