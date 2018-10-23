package Clients;

public interface IClient {
    void handleRequest(String chatId, String s, TelegramAPI api);
}
