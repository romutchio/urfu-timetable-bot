package com.clients;

import com.server.DatabaseOfSessions;
import com.server.Message;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class TelegramAPI extends TelegramLongPollingBot {
    public TelegramClient client = new TelegramClient();

    private ReplyKeyboardMarkup getReplyKeynoard(Message message) {
        var replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        // Создаем список строк клавиатуры
        List<KeyboardRow> keyboard = new ArrayList<>();

        for (var buttonText : message.textsToButtons) {
            var keyboardRow = new KeyboardRow();
            keyboardRow.add(buttonText);
            keyboard.add(keyboardRow);
        }
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    @Override
    public String getBotToken() {
        return "632792999:AAFr07dPw_iNZ6vNdg3dPXRqO7aeYpPe57E";
    }

    @Override
    public String getBotUsername() {
        return "UrFUTimetableBot";
    }

    public void handleSpecialCommands(String token, String request) {
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
                sendMessage(token, "Здесь будут настройки");
                break;
            case "/users":
                for (var user : DatabaseOfSessions.getDatabaseOfUsers().values()) {
                    sendMessage(token, user.handle);
                }
                break;
            case "/stat":
                if (DatabaseOfSessions.Contains(token)) {
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
                break;
            default:
                sendMessage(token, request);
                break;
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        var request = update.getMessage().getText();
        var token = update.getMessage().getChatId().toString();
        if (request.charAt(0) == '/') {
            handleSpecialCommands(token, request);
        } else
            client.handleRequest(update.getMessage().getChatId().toString(), request, this);
    }

    public synchronized void sendMessage(String chatId, String answer) {
        sendMessage(chatId, new Message(answer, null));
    }

    public synchronized void sendMessage(String chatId, Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(message.question);

        System.out.println(chatId);
        System.out.println(message.question);
        if (message.textsToButtons != null && message.textsToButtons.size() != 0)
            sendMessage.setReplyMarkup(getReplyKeynoard(message));
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
