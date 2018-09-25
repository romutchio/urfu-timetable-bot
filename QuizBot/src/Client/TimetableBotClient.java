package Client;

import Server.GraphOfMessages;
import Server.Message;
import org.apache.commons.lang.NotImplementedException;

import java.util.Scanner;

public class TimetableBotClient {
    public static void main(String[] args) {
        var username = GraphOfMessages.initializeSession();
        Scanner in = new Scanner(System.in);
        while(true)
        {
            String answer = in.nextLine();
            var answ = GraphOfMessages.HandleAnswer(username, answer);
            System.out.println(answ);
        }
    }

    private static Message GetResponse(String answer, Message lastMessage)
    {
        throw new NotImplementedException();
    }
}
