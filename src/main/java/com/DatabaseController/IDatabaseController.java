package com.DatabaseController;

public interface IDatabaseController {
    enum UserInfo{
        token,
        group_name,
        notification_time,
        last_answer,
        last_day_request,
        last_class_num_request,
        notifications,
        next_message
    }

    void createNewTable();
    void addUser(Integer token);
    void removeUser(Integer token);
    void getUserInfo(Integer token);
    void updateUserInfo(Integer token, UserInfo field, Object newValue);
}
