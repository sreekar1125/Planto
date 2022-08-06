package com.dev.planto.model;

public class Transaction {

   private String Date,Money;

    public Transaction(String date, String money) {
        Date = date;
        Money = money;
    }
    public String getDate() {
        return Date;
    }

    public String getMoney() {
        return Money;
    }

    public Transaction() {
    }
}
