package com.example.zoir.wificonnection;

/**
 * Created by Zoir on 6-Jun-17.
 */

public class Element {
    private String title;
    private String security;
    private String level;

    public Element(String title, String security, String level) {
        this.title = title;
        this.security = security;
        this.level = level;
    }

    public String getTitle() {
        return title;
    }

    public String getSecurity() {
        return security;
    }

    public String getLevel() {
        return level;
    }
}
