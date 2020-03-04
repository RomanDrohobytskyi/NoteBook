package com.example.notebook.note;

import android.content.Context;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * This class represents a Note object
 * Created by saeid on 5/8/2016.
 */
public class Note implements Serializable {

    private long date;
    private String title;
    private String content;

    public Note(long dateInMillis, String title, String content) {
        date = dateInMillis;
        this.title = title;
        this.content = content;
    }

    public void setDateTime(long dateTime) {
        date = dateTime;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getDateTime() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }


    public String getDateTimeFormatted(Context context) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"
                , context.getResources().getConfiguration().locale);
        formatter.setTimeZone(TimeZone.getDefault());
        return formatter.format(new Date(date));
    }

}