package com.clients;

import com.server.DatabaseOfSessions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramAPI extends TelegramLongPollingBot {
    public TelegramClient client = new TelegramClient();
    @Override
    public String getBotToken() {
        return "632792999:AAFr07dPw_iNZ6vNdg3dPXRqO7aeYpPe57E";
    }

    @Override
    public String getBotUsername() {
        return "UrFUTimetableBot";
    }

    @Override
    public void onUpdateReceived(Update update) {
        var request = update.getMessage().getText();
        var token = update.getMessage().getChatId().toString();
        if (request.charAt(0) == '/')
        {
            switch (request) {
                case "/help":
                    sendMessage(token, "Здесь будет help");
                    break;
                case "/start":
                    if (DatabaseOfSessions.Contains(token))
                        DatabaseOfSessions.RemoveUserFromDatabase(token);
                    client.initSession(token, this);
                    break;
                case "/settings":
                    if (DatabaseOfSessions.Contains(token))
                    {
                        var user = DatabaseOfSessions.GetUserByToken(token);
                        sendMessage(token, "token: " + user.token);
                        sendMessage(token, "handle: " + user.handle);
                        sendMessage(token, "lastAnswer: " + user.lastAnswer);
                        sendMessage(token, "nextMessage.question: " + user.nextMessage.question);
                        sendMessage(token, "nextMessage.operationIdentifier: " + user.nextMessage.operationIdentifier);
                        sendMessage(token, "group.title: " + user.group.title);
                        sendMessage(token, "group.id: " + String.valueOf(user.group.id));
                        sendMessage(token, "group.actual: " + String.valueOf(user.group.actual));
                        sendMessage(token, "group.course: " + String.valueOf(user.group.course));
                        sendMessage(token, "lastDayRequest: " + user.lastDayRequest);
                        sendMessage(token, "notifications: " + user.notifications.toString());
                    }
                        sendMessage(token, "Здесь будут настройки");
                    break;
                default:
                    sendMessage(token, request);
                    break;
            }

        }
        else
            client.handleRequest(update.getMessage().getChatId().toString(), request, this);
    }


    public synchronized void sendMessage(String chatId, String answer) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(answer);

        System.out.println(chatId);
        System.out.println(answer);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
