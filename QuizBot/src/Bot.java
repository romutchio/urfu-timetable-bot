import jdk.jshell.spi.ExecutionControl;

import java.util.Scanner;

public class Bot {
    private static void handleWelcome(Messages messages, Scanner in) {
        System.out.println(messages.Welcome());
        String answer = in.nextLine();

        switch (answer.toLowerCase()) {
            case "да":
                System.out.println(messages.GroupQuestion());
                mainLoop(messages, in);
                break;
            case "нет":
                System.out.println(messages.Decline());
                break;
            default:
                System.out.println("Я вас не понял \nПопробуйте еще раз\n"); //перенести в ответы
                handleWelcome(messages, in);
                break;
        }
    }

    private static void mainLoop(Messages messages, Scanner in) {
        String groupName = in.nextLine();
        Group userGroup = AnswerValidator.RecognizeGroup(groupName);
        System.out.println(messages.GroupSelection(userGroup));
    }

    public static void main(String[] args) {
        Messages messages = new Messages();
        Scanner in = new Scanner(System.in);

        handleWelcome(messages, in);

        while (true) {
            String answer = in.nextLine();

            switch (answer) {
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

                case "/exit":
                    System.exit(1);
                    break;

                default:
                    System.out.println("Я вас не понял, чтобы увидеть подсказки наберите: \"/help\"");
                    break;
            }
        }
    }
}
