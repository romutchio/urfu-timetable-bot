package com.server;

import java.util.ArrayList;

public class Message {
    public String question;
    public String operationIdentifier;
    public ArrayList<String> textsToButtons;

    public Message(String question, String operationIdentifier) {
        this.question = question;
        this.operationIdentifier = operationIdentifier;
    }
    public Message(String question, String operationIdentifier, ArrayList<String> textsToButtons) {
        this.question = question;
        this.operationIdentifier = operationIdentifier;
        this.textsToButtons = textsToButtons;
    }
}