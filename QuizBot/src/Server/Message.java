package Server;

import java.util.function.Consumer;
import java.util.function.Function;

public class Message {
    public String question;
    public Consumer<User> answerValidator;

    public Message(String question, Consumer<User> availableAnswers) {
        this.question = question;
        this.answerValidator = availableAnswers;
    }
    public Message() {
        this.question = null;
        this.answerValidator = null;
    }
}
