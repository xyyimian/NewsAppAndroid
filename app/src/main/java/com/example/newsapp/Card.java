package com.example.newsapp;

public class Card {
    private String title;
    private String time;
    private String section;
    private String imgurl;

    public Card(String title,String time,String section,String imgurl){
        this.title = title;
        this.time = time;
        this.section = section;
        this.imgurl = imgurl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
