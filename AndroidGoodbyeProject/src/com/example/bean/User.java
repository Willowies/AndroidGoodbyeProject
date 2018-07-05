package com.example.bean;

public class User {
    private int id;
    private int account;
    private String name;
    private int iconVersion;

    public int getIconVersion() {
        return iconVersion;
    }

    public void setIconVersion(int iconVersion) {
        this.iconVersion = iconVersion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccount() {
        return account;
    }

    public void setAccount(int account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
