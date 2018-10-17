package Server;

public class User {
    public String token;
    public String handle;
    public Group group;
    public int notificationAdvanceTime;
    public String lastNotified;
    public Message nextMessage;
    public String lastAnswer;
    public String lastDayRequest;
    public int lastClassNumRequest;
    public User(String handle, Group group, Message nextMessage, String lastAnswer)
    {
        this.token = handle;
        this.handle = handle;
        this.group = group;
        this.nextMessage = nextMessage;
        this.lastAnswer = lastAnswer;
    }
    public User(String token, String handle, Group group, Message nextMessage, String lastAnswer)
    {
        this.token = token;
        this.handle = handle;
        this.group = group;
        this.nextMessage = nextMessage;
        this.lastAnswer = lastAnswer;
        this.notificationAdvanceTime = 15;
        this.lastNotified = null;
    }

}
