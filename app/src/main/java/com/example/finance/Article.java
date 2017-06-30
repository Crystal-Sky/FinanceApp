package com.example.finance;


public class Article {
    private String title="";
    private String summary="";
    private String postTime="";

    public Article(String title,String summary,String postTime){
        this.title=title;
        this.summary=summary;
        this.postTime=postTime;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getSummary() {
        return summary;
    }
    public void setSummary(String summary) {
        this.summary = summary;
    }
    public String getPostTime() {
        return postTime;
    }
    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }


}

