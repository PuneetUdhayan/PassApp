package com.example.passdemo1;

public class DataEntry {
    String time;
    String subject;
    String room;

    public DataEntry() {
    }

    public DataEntry(String time, String subject, String room) {
        this.time = time;
        this.subject = subject;
        this.room = room;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
