package com.DatabaseController;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseController implements IDatabaseController {
    private static String url = "jdbc:postgresql://ec2-54-225-115-234.compute-1.amazonaws.com:5432/d3oj1hcq2iv7r9?sslmode=require";
    private static String user = "fqgnjikpzainjg";
    private static String pass = "82ddf7e508862e026fdca08ef482484727ec7e5477ab55571228598577f19340";
    private static Connection connection;

    public DatabaseController() {
        connection = connect();
    }

    @Override
    public void createNewTable() {
        getRequest(createTableRequest());
    }

    @Override
    public void addUser(Integer token) {
        getRequest(addNewUserRequest(token));
    }

    @Override
    public void removeUser(Integer token) {
        getRequest(removeUserRequest(token));
    }

    @Override
    public void getUserInfo(Integer token) {
        postRequest(getUserInfoRequest(token));
    }

    @Override
    public void updateUserInfo(Integer token, UserInfo field, Object newValue) {
        getRequest(updateUserInfoRequest(token, field, newValue));
    }

    private Connection connect() {
//        try {
//            Class.forName("org.postgresql.Driver");
//        } catch (ClassNotFoundException e) {
//
//            System.out.println("PostgreSQL JDBC Driver wasn't found");
//            e.printStackTrace();
//        }
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            System.out.println("Connection Failed!");
            e.printStackTrace();
        }
        return connection;
    }

    private void getRequest(String task) {
        try {
            var statement = connection.createStatement();
            statement.executeUpdate(task);
            System.out.println("Success");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Object> postRequest(String task) {
        var values = new ArrayList<>();
        try {
            var statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(task);
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();

            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    values.add(resultSet.getObject(i));
                }
            }
            System.out.println(values);
            System.out.println("Success");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return values;
    }



    private String createTableRequest() {
        return "CREATE TABLE sessions (" +
                "token integer NOT NULL PRIMARY KEY," +
                "group_name varchar(20) NULL," +
                "notification_time integer NULL," +
                "last_answer varchar(40) NULL," +
                "last_day_request varchar(15)," +
                "last_class_num_request integer," +
                "notifications json NULL," +
                "next_message json NULL" +
                ");";
    }
    
    private String addNewUserRequest(Integer token) {
        return String.format("INSERT INTO sessions (token) VALUES (%d);", token);
    }    
    
    private String removeUserRequest(Integer token) {
        return String.format("DELETE FROM sessions WHERE token = %d;", token);
    }

    private String getUserInfoRequest(Integer token) {
        return String.format("SELECT * FROM sessions WHERE token = %d;", token);
    }

    private String updateUserInfoRequest(Integer token, UserInfo column, Object value) {
        return String.format("UPDATE sessions SET %s = \'%s\' WHERE token = %d;", column, value, token);
    }
    
}