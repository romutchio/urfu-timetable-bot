package Server;

import java.util.Scanner;


public class AnswerHandler {
    public static String initializeSession()
    {
        new GraphOfMessages();
        Message mes = GraphOfMessages.getInitMessage();
        System.out.println(mes.question);//только для консольного клиента, в tg будем получать token
        var operationId = mes.operationIdentifier;
        var transit = GraphOfMessages.getTransit(operationId);


        Scanner in = new Scanner(System.in);
        String username = in.nextLine();
        User user;


        if (DatabaseOfSessions.Contains(username)) {
            System.out.println("Можете спросить у меня что-нибудь про расписание");
            user = DatabaseOfSessions.GetUserByUsername(username);
        }
        else
        {
            user = new User(username, null, GraphOfMessages.getInitMessage(), null);
            DatabaseOfSessions.AddNewUserInDatabase(user);
            transit.accept(user);
        }
        DatabaseOfSessions.UpdateUserInDatabase(user);

        return username;
    }

    public static String handleAnswer(String username, String answer)
    {

        var user = DatabaseOfSessions.GetUserByUsername(username);
        user.lastAnswer = answer;

        GraphOfMessages.getTransit(user.nextMessage.operationIdentifier).accept(user);

        var message = user.nextMessage;
        DatabaseOfSessions.UpdateUserInDatabase(user);
        return message.question;
    }
}
