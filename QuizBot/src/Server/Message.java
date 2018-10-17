package Server;

public class Message {
    public String question;
    public String operationIdentifier;

    public Message(String question, String operationIdentifier) {
        this.question = question;
        this.operationIdentifier = operationIdentifier;
    }
}
