import jdk.jshell.spi.ExecutionControl;

import java.util.Scanner;

public class Bot {
    private static void handleWelcome(Messages messages, Scanner in)
    {
        System.out.println(messages.Welcome());
        String answer = in.nextLine();

        if (answer.toLowerCase().equals("да")) {
            System.out.println(messages.GroupQuestion());
            mainLoop(messages, in);
        }

        else if (answer.toLowerCase().equals("нет")) {
            System.out.println(messages.Decline());
        } else {
            System.out.println("Я вас не понял \nПопробуйте еще раз\n\n"); //перенести в ответы
            handleWelcome(messages, in);
        }
    }

    private static void mainLoop(Messages messages, Scanner in)
    {
        String groupName = in.nextLine();
        Group userGroup = AnswerValidator.RecognizeGroup(groupName);
        System.out.println(messages.GroupSelection(userGroup));
    }

    public static void main(String[] args)
    {
        Messages messages = new Messages();
        Scanner in = new Scanner(System.in);

        handleWelcome(messages, in);

        while (true)
        {
            String answer = in.nextLine();

            switch (answer){
                case "/start":
                    handleWelcome(messages, in);
                    break;

                case "/help":
                    System.out.println("Данная функция еще не реализованна ¯\\_(ツ)_/¯");
                    //throw new Exception();
                    break;

                case "/settings":
                    System.out.println("Данная функция еще не реализованна ¯\\_(ツ)_/¯");
                    //throw new Exception();
                    break;

            }
        }
    }
}
