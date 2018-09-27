package Client;

import Server.AnswerHendler;
import Server.GraphOfMessages;
import Server.Message;
import org.apache.commons.lang.NotImplementedException;

import java.util.Scanner;

public class TimetableBotClient {
    public static void main(String[] args) {
        var username = AnswerHendler.initializeSession();
        var in = new Scanner(System.in);

        while(true)
        {
            var answer = in.nextLine(); //при инициализации ждет ввод 2 раза
            var answ = AnswerHendler.handleAnswer(username, answer);
            System.out.println(answ);
        }
    }

    private static Message GetResponse(String answer, Message lastMessage)
    {
        throw new NotImplementedException();
    }
}
