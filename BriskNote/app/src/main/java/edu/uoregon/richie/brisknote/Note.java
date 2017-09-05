package edu.uoregon.richie.brisknote;

import java.util.Calendar;

public class Note {
    private String title,
            body;
    private Calendar date;

    public Note() {
        title = "";
        body = "";
        date = Calendar.getInstance();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }
}
