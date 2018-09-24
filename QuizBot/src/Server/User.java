package Server;

public class User {
    public String handle;
    public Group group;
    public Message nextMessage;
    public String lastAnswer;

    public User(String handle, Group group, Message nextMessage)
    {
        this.handle = handle;
        this.group = group;
        this.nextMessage = nextMessage;
    }
    public User(String handle)
    {
        this.handle = handle;
    }
}
