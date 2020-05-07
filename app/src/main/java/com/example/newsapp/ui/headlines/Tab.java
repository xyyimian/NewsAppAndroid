package com.example.newsapp.ui.headlines;

public class Tab {

    private String section;
    private boolean selected;

    public Tab(String section,boolean selected){
        this.section = section;
        this.selected = selected;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
