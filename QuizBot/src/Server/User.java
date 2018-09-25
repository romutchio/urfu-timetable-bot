package Server;

public class User {
    public String handle;
    public Group group;
    public Message nextMessage;
    public String lastAnswer;

    public User(String handle, Group group, Message nextMessage, String lastAnswer)
    {
        this.handle = handle;
        this.group = group;
        this.nextMessage = nextMessage;
        this.lastAnswer = lastAnswer;
    }

//    public User(String handle, Group group, Message nextMessage)
//    {
//        this.handle = handle;
//        this.group = group;
//        this.nextMessage = nextMessage;
//        this.lastAnswer = null;
//    }

//    public User(String handle)
//    {
//        this.handle = handle;
//        this.group = new Group();
//        this.nextMessage = new Message();
//        this.lastAnswer = null;
//    }
}
