package Server;

import java.util.ArrayList;

public class Message {
    public String question;
    public ArrayList<String> availiableAnswers;

    public Message(String question, new ArrayList<String> availableAnswers) {
        this.question = question;
        this.availiableAnswers = availableAnswers;
    }
}
