package com.superdroid.test.mymemo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CheckList {
    String done;
    String title;
    String date;
    public CheckList(String title, Date date){
        this.title=title;
        this.done="미완료";
        String pattern ="yy-MM-dd HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String newDate=simpleDateFormat.format(date);
        this.date=newDate;
    }
    public CheckList(String done,String title, Date date){
        this.title=title;
        this.done=done;
        String pattern ="yy-MM-dd HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String newDate=simpleDateFormat.format(date);
        this.date=newDate;
    }
    public CheckList(String done,String title, String date){
        this.title=title;
        this.done=done;
        this.date=date;
    }
    public void setDate(Date date) {
        String pattern ="yy-MM-dd HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String newDate=simpleDateFormat.format(date);
        this.date=newDate;
    }
    public void setDate(String date) {
        this.date=date;
    }
    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String isDone() {
        return done;
    }

    public void setDone(String done) {
        this.done = done;
    }
}
