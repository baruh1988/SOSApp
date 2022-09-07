package com.example.sosapp;

public class Contact {
    private int id;
    private String name;
    private String phoneNumber;

    public Contact(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = validatePhoneNumber(phoneNumber);
    }

    public Contact(int id, String name, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.phoneNumber = validatePhoneNumber(phoneNumber);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = validatePhoneNumber(phoneNumber);
    }

    private String validatePhoneNumber(String phoneNumber) {
        StringBuilder case1 = new StringBuilder("+1"); // This code used for testing on emulators, for real use change with country code
        StringBuilder case2 = new StringBuilder("");
        if (phoneNumber.charAt(0) != '+') {
            for (int i = 0; i < phoneNumber.length(); i++)
                if (phoneNumber.charAt(i) != '-' && phoneNumber.charAt(i) != ' ')
                    case1.append(phoneNumber.charAt(i));
            return case1.toString();
        } else {
            for (int i = 0; i < phoneNumber.length(); i++)
                if (phoneNumber.charAt(i) != '-' && phoneNumber.charAt(i) != ' ')
                    case2.append(phoneNumber.charAt(i));
            return case2.toString();
        }
    }
}
