package Clients;

import Server.*;
import Server.Notificator.Notificator;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

public class TelegramClient {

    public static void initNewUser(String chatId, TelegramAPI api){
        Message mes = GraphOfMessages.getInitMessage();
//        sendMessage.setText(mes.question);//только для консольного клиента, в tg будем получать token
        var operationId = mes.operationIdentifier;
        var transit = GraphOfMessages.getTransit(operationId);
        var user = new User(chatId, null, GraphOfMessages.getInitMessage(), null, new NotificationManager());
        DatabaseOfSessions.AddNewUserInDatabase(user);
        transit.accept(user);
        api.sendMessage(chatId, mes.question);
    }
    public static void handleRequest(String chatId, String s, TelegramAPI api) {
        User user;
        if (!DatabaseOfSessions.Contains(chatId)) {
            Message mes = GraphOfMessages.getInitMessage();
            api.sendMessage(chatId, mes.question);//только для консольного клиента, в tg будем получать token
            var operationId = mes.operationIdentifier;
            var transit = GraphOfMessages.getTransit(operationId);
            user = new User(chatId, null, GraphOfMessages.getInitMessage(), null, new NotificationManager());
            DatabaseOfSessions.AddNewUserInDatabase(user);
            transit.accept(user);
        } else {
            user = DatabaseOfSessions.GetUserByToken(chatId);
            user.lastAnswer = s;
            GraphOfMessages.getTransit(user.nextMessage.operationIdentifier).accept(user);
            var message = user.nextMessage;
            DatabaseOfSessions.UpdateUserInDatabase(user);
            api.sendMessage(chatId, message.question);
        }
    }

    public static void main(String[] args) {
        ApiContextInitializer.init();
        var notificator = new Notificator();
        var thread = new Thread(notificator);
        thread.start();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new TelegramAPI());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }
}
