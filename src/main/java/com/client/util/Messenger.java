package com.client.util;

public class Messenger {

    private String message;

    private String status;

    private String payload;

    public Messenger() {
    }

    public Messenger(String message, String status, String payload) {
        this.message = message;
        this.status = status;
        this.payload = payload;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public static Messenger getMessenger(String message, String status, String payload){
        return new Messenger(message,status,payload);
    }
}
