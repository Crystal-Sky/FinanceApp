package com.example.finance.AboutListView;

import com.example.finance.R;

public class TListItem {

    public static final int TYPE1= 0;
    public static final int TYPE_COUNT = 1;
    private String beizhu;//备注
    private String purtype;//消费类型
    private int year;//年
    private int month;//月
    private int day;//日
    private double money;//消费金额
    private int picture;//图像
    private int type;//标识
    private String name;
    private String IOU;//收入或支出标识
    public TListItem(int type,int picture,String purtype ,String name,String beizhu,int year,int month,int day,double money,String IOU) {
        this.type = type;
        this.purtype=purtype;
        this.name=name;
        this.beizhu= beizhu;
        this.year= year;
        this.month= month;
        this.day= day;
        this.money=money;
        this.picture=picture;
        this.IOU=IOU;
    }

    public int getType() {
        return type;
    }
    public String beizhu() {
        return beizhu;
    }
    public int year() {
        return year;
    }
    public int month() {
        return month;
    }
    public int day() {
        return day;
    }
    public int getPicture() {
        return picture;
    }
    public double getMoney(){return money;}
    public String getPurtype(){return purtype;}
    public String getName(){return name;}
    public String getIOU(){return IOU;}
}

