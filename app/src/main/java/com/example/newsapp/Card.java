package com.example.newsapp;

public class Card {
    private String id;
    private String url;
    private String title;
    private String description;
    private String time;
    private String section;
    private String imgurl;

    public Card(String id, String url, String title, String description, String time, String section, String imgurl) {
        this.id = id;
        this.url = url;
        this.title = title;
        this.description = description;
        this.time = time;
        this.section = section;
        this.imgurl = imgurl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }
}
