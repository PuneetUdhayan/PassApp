package com.example.passdemo1;

public class Cards {
    String time;
    String subject;
    String room;
    String user;
    String key;
    Boolean isAvailable;

    public Cards(){}

    public Cards(String time,String subject,String room,String user,String key,Boolean isAvailable) {
        this.time = time;
        this.subject = subject;
        this.room=room;
        this.user=user;
        this.key=key;
        this.isAvailable=isAvailable;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }
}
