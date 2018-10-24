package com.clients;

public interface IClient {
    void handleRequest(String chatId, String s, TelegramAPI api);
}
