package com.example.zoir.company;

/**
 * Created by Zoir on 11-Oct-17.
 */

public class Blog {

    private String title;
    private String desc;

    public Blog() {

    }

    public Blog(String title, String desc, String image ) {
        this.title = title;
        this.desc = desc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}


