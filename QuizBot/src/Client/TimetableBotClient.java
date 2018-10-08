package Client;

import Server.AnswerHandler;
import Server.Message;
import org.apache.commons.lang.NotImplementedException;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.Scanner;
import java.util.logging.Level;

public class TimetableBotClient extends TelegramLongPollingBot {

    @Override
    public String getBotToken() {
        return "632792999:AAFr07dPw_iNZ6vNdg3dPXRqO7aeYpPe57E";
    }

    @Override
    public void onUpdateReceived(Update update) {
        String message = update.getMessage().getText();
        sendMsg(update.getMessage().getChatId().toString(), message);
    }

    @Override
    public String getBotUsername() {
        return "UrFUTimetableBot";
    }

    public synchronized void sendMsg(String chatId, String s) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
//        if (s.equals("/start"))
//            sendMessage.setText()
        sendMessage.setText(AnswerHandler.handleAnswer("vaspahomov", s));
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        var username = AnswerHandler.initializeSession();
        var in = new Scanner(System.in);

        while(true)
        {
            var answer = in.nextLine(); //при инициализации ждет ввод 2 раза
            var answ = AnswerHandler.handleAnswer(username, answer);
            System.out.println(answ);
        }
    }
//
//    public static void main(String[] args) {
//        ApiContextInitializer.init();
//        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
//        try {
//            telegramBotsApi.registerBot(new TimetableBotClient());
//        } catch (TelegramApiRequestException e) {
//            e.printStackTrace();
//        }
//    }

    private static Message GetResponse(String answer, Message lastMessage)
    {
        throw new NotImplementedException();
    }
}
