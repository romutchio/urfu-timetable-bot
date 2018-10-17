package Server;

import java.util.Date;

public class User {
    public String handle;
    public Group group;
    public NotificationManager notifications;
    public int notificationAdvanceTime;
    public String lastNotified;
    public Message nextMessage;
    public String lastAnswer;
    public String lastDayRequest;
    public int lastClassNumRequest;
    public User(String handle, Group group, Message nextMessage, String lastAnswer, NotificationManager notifications)
    {
        this.handle = handle;
        this.group = group;
        this.nextMessage = nextMessage;
        this.lastAnswer = lastAnswer;
        this.notificationAdvanceTime = 15;
        this.lastNotified = null;
        this.notifications = notifications;
    }

}
