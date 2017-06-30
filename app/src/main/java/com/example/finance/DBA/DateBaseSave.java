package com.example.finance.DBA;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DateBaseSave extends SQLiteOpenHelper {
    private static final int version=1;
    private static final String DadeBaseName="inandout.db";
    public DateBaseSave(Context context){
        super(context,DadeBaseName,null,version);
    }
    @Override
    public void onCreate(SQLiteDatabase database){
        database.execSQL("create table income (_id integer primary key,"+
                            "money decimal,name varchar(200),type varchar(10)," +
                            "year decimal,month decimal,day decimal,note varchar(300))");
        database.execSQL("create table expenditure (_id integer primary key,money decimal,name varchar(200),"
                + "type varchar(10),year decimal,month decimal,day decimal,note varchar(300))");
    }
    @Override
    public void onUpgrade(SQLiteDatabase database,int oldVersion,int newVersion){
    }
}
