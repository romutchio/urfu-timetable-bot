package com.DatabaseController;
import com.server.User;
import java.util.HashMap;

public interface IDtatabaseUserEditor {

    HashMap<String, User> getDatabaseOfUsers() ;

    User GetUserByToken(String token);

    boolean Contains(String token);

    void AddNewUserInDatabase(User user);

    void UpdateUserInDatabase(User user);

    void RemoveUserFromDatabase(String token) ;
}
