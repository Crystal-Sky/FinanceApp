package com.example.finance.DBA;

public class InObject
{
    private int _id;
    private double money;
    private String name;
    private String type;
    private int year;
    private int month;
    private int day;
    private String note;

    public InObject(){
        super();
    }
    public InObject(int id,double money,String name,String type,int year,int month,int day,String note) {
        super();
        this._id = id;
        this.money = money;
        this.name=name;
        this.type=type;
        this.year=year;
        this.month=month;
        this.day=day;
        this.note = note;
    }

    public int getid(){
        return _id;
    }

    public void setid(int id){
        this._id = id;
    }

    public double getMoney(){
        return money;
    }

    public void setMoney(double money){
        this.money = money;
    }

    public int getYear(){
        return year;
    }

    public void setYear(int year){
        this.year = year;
    }
    public int getMonth(){
        return month;
    }

    public void setMonth(int month){
        this.month = month;
    }

    public int getDay(){
        return day;
    }

    public void setDay(int day){
        this.day = day;
    }
    public String getType(){
        return type;
    }

    public void setType(String type){
        this.type = type;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getNote(){
        return note;
    }

    public void setNote(String note){
        this.note = note;
    }
}
