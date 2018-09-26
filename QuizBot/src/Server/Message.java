package Server;

import java.util.function.Consumer;
import java.util.function.Function;

public class Message {
    public String question;
    public String operationIdentifier;

    public Message(String question, String operationIdentifier) {
        this.question = question;
        this.operationIdentifier = operationIdentifier;
    }
//    public Message() {
//        this.question = null;
//        this.answerValidator = null;
//    }
}
