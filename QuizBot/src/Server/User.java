package Server;

public class User {
    String handle;
    Group group;
    Message nextMessage;
    String lastAnswer;
    public User(String handle, Group group, Message nextMessage)
    {
        this.handle = handle;
        this.group = group;
        this.nextMessage = nextMessage;
    }
}
