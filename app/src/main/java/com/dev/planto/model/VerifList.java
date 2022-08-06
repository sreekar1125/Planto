package com.dev.planto.model;

public class VerifList {
    private String Date;
    private String Status;
    private String Email;
    private String Name;

    public VerifList() {
    }

    public VerifList(String date, String status, String email, String name, String points) {
        Date = date;
        Status = status;
        Email = email;
        Name = name;
    }


    public String getDate() {
        return Date;
    }

    public String getStatus() {
        return Status;
    }

    public String getEmail() {
        return Email;
    }

    public String getName() {
        return Name;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setName(String name) {
        Name = name;
    }
}
