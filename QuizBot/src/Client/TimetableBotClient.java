package Client;

import Server.*;
import org.apache.commons.lang.NotImplementedException;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;


public class TimetableBotClient extends TelegramLongPollingBot {

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
        String message = update.getMessage().getText();
        sendMsg(update.getMessage().getChatId().toString(), message);
    }

    public synchronized void sendMsg(String chatId, String s) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        User user;
        if (!DatabaseOfSessions.Contains(chatId)) {
            new GraphOfMessages();
            Message mes = GraphOfMessages.getInitMessage();
            sendMessage.setText(mes.question);//только для консольного клиента, в tg будем получать token
            var operationId = mes.operationIdentifier;
            var transit = GraphOfMessages.getTransit(operationId);
            user = new User(chatId, null, GraphOfMessages.getInitMessage(), null);
            DatabaseOfSessions.AddNewUserInDatabase(user);
            transit.accept(user);
        }
       else {
            new GraphOfMessages();
            user = DatabaseOfSessions.GetUserByUsername(chatId);
            user.lastAnswer = s;
            GraphOfMessages.getTransit(user.nextMessage.operationIdentifier).accept(user);
            var message = user.nextMessage;
            DatabaseOfSessions.UpdateUserInDatabase(user);
            sendMessage.setText(message.question);
        }

        System.out.println(chatId);
        System.out.println(s);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) {
//        var username = AnswerHandler.initializeSession();
//        var in = new Scanner(System.in);
//
//        while(true)
//        {
//            var answer = in.nextLine(); //при инициализации ждет ввод 2 раза
//            var answ = AnswerHandler.handleAnswer(username, answer);
//            System.out.println(answ);
//        }
//    }
//
    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new TimetableBotClient());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }

    private static Message GetResponse(String answer, Message lastMessage)
    {
        throw new NotImplementedException();
    }
}
