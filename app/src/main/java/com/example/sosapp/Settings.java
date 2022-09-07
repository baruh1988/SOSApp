package com.example.sosapp;

public class Settings {

    private String message;
    private boolean notifyEnabled;
    private int amount;
    private static Settings instance;

    private Settings() {
        this.message = "I am in DANGER, i need help. Please urgently reach me out. Here are my coordinates.";
        this.notifyEnabled = false;
        this.amount = 5;
    }

    public static synchronized Settings getSettings() {
        if (instance == null)
            instance = new Settings();
        return instance;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isNotifyEnabled() {
        return notifyEnabled;
    }

    public void setNotifyEnabled(boolean notifyEnabled) {
        this.notifyEnabled = notifyEnabled;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
