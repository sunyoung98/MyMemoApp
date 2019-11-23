package com.superdroid.test.mymemo;

import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MemoData {
    private String date;
    private String title;
    private String document;
    public MemoData(Date date, String title, String document ){
        String pattern ="yy-MM-dd HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String newDate=simpleDateFormat.format(date);
        this.date=newDate;
        this.title=title;
        this.document=document;
    }

    public void setDate(Date date) {
        String pattern ="yy-MM-dd HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String newDate=simpleDateFormat.format(date);
        this.date=newDate;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getDocument() {
        return document;
    }
}
