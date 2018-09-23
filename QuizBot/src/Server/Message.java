package Server;

import java.util.function.Consumer;
import java.util.function.Function;

public class Message {
    public String question;
    public Consumer<String> answerValidator;

    public Message(String question, Consumer<String> availableAnswers) {
        this.question = question;
        this.answerValidator = availableAnswers;
    }
}
