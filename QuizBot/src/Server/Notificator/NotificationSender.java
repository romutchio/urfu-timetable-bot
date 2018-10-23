package Server.Notificator;

public class NotificationSender implements Runnable {
    public void run() {
        System.out.println("Привет из побочного потока!");
    }
}
