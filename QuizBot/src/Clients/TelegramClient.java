package Clients;

import Server.*;

public class TelegramClient {

    public static void initNewUser(String chatId, TelegramAPI api){
        Message mes = GraphOfMessages.getInitMessage();
//        sendMessage.setText(mes.question);//только для консольного клиента, в tg будем получать token
        var operationId = mes.operationIdentifier;
        var transit = GraphOfMessages.getTransit(operationId);
        var user = new User(chatId, null, GraphOfMessages.getInitMessage(), null);
        DatabaseOfSessions.AddNewUserInDatabase(user);
        transit.accept(user);
        api.sendMessage(chatId, mes.question);
    }
    public static void handleRequest(String chatId, String s, TelegramAPI api) {
        User user;
        if (!DatabaseOfSessions.Contains(chatId)) {
            initNewUser(chatId, api);
        } else {
            new GraphOfMessages();
            user = DatabaseOfSessions.GetUserByToken(chatId);
            user.lastAnswer = s;
            GraphOfMessages.getTransit(user.nextMessage.operationIdentifier).accept(user);
            var message = user.nextMessage;
            DatabaseOfSessions.UpdateUserInDatabase(user);
            api.sendMessage(chatId, message.question);
        }

        System.out.println(chatId);
        System.out.println(s);
    }

    public static void main(String[] args) {

        new GraphOfMessages();
        var api = new TelegramAPI();
    }
}
