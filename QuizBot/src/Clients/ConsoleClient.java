package Clients;

import Server.AnswerHandler;

import java.util.Scanner;

public class ConsoleClient {
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
}
