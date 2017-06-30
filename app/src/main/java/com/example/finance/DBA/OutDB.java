package com.example.finance.DBA;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;

import com.example.finance.BaseMessage;

public class OutDB {
    private DateBaseSave helper;
    private SQLiteDatabase database;
    public static double sum=0;
    public  OutDB(Context context){
        helper=new DateBaseSave(context);
        database=helper.getWritableDatabase();
    }
    public void add(OutObject out_object) {
        database.execSQL(
                "insert into expenditure (_id,money,name,type,year,month,day,note) values (?,?,?,?,?,?,?,?)",
                new Object[] { out_object.getid(), out_object.getMoney(),
                        out_object.getName(), out_object.getType(),
                        out_object.getYear(), out_object.getMonth(),
                        out_object.getDay(),out_object.getNote() });
    }
    public void update(OutObject out_object) {
        database.execSQL(
                "update expenditure set money = ?,name = ?,type = ?,year=?,month=?,day=?,note = ? where _id = ?",
                new Object[] { out_object.getMoney(),
                        out_object.getName(), out_object.getType(),
                        out_object.getYear(),out_object.getMonth(),
                        out_object.getDay(),out_object.getNote(),
                        out_object.getid() });
    }
    public OutObject find(int id) {
        Cursor cursor = database
                .rawQuery(
                        "select _id,money,name,type,time,note from expenditure where _id = ?",
                        new String[] { String.valueOf(id) });
        if (cursor.moveToNext()){
            return new OutObject(
                    cursor.getInt(cursor.getColumnIndex("_id")),
                    cursor.getDouble(cursor.getColumnIndex("money")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getString(cursor.getColumnIndex("type")),
                    cursor.getInt(cursor.getColumnIndex("year")),
                    cursor.getInt(cursor.getColumnIndex("month")),
                    cursor.getInt(cursor.getColumnIndex("day")),
                    cursor.getString(cursor.getColumnIndex("note")));
        }
        cursor.close();
        return null;
    }
    public double OutSum() {
        int id=0;
        Cursor cursor;
        double inSum=0;
        Calendar c = Calendar.getInstance();// 获取当前系统日期
        int year = c.get(Calendar.YEAR);// 获取年份
        int month = c.get(Calendar.MONTH)+1;// 获取月份
        int day= c.get(Calendar.DAY_OF_MONTH);// 获取天数
        cursor = database
                .rawQuery("select _id,money,name,type,year,month,day,note from expenditure where year = ? and month=? and day=?",
                        new String[] { String.valueOf(year),String.valueOf(month),String.valueOf(day)});
        if(cursor!=null&&cursor.getCount()>0){
            while(cursor.moveToNext()){
                inSum=inSum+cursor.getDouble(cursor.getColumnIndex("money"));
            }
            cursor.close();
        }
        return inSum;
    }
    public double MonthOutSum() {
        int id=0;
        Cursor cursor;
        double inSum=0;
        Calendar c = Calendar.getInstance();// 获取当前系统日期
        int year = c.get(Calendar.YEAR);// 获取年份
        int month = c.get(Calendar.MONTH)+1;// 获取月份
        int day= c.get(Calendar.DAY_OF_MONTH);// 获取天数
        cursor = database
                .rawQuery("select _id,money,name,type,year,month,day,note from expenditure where year = ? and month=? ",
                        new String[] { String.valueOf(year),String.valueOf(month)});
        if(cursor!=null&&cursor.getCount()>0){
            while(cursor.moveToNext()){
                inSum=inSum+cursor.getDouble(cursor.getColumnIndex("money"));
            }
            cursor.close();
        }
        return inSum;
    }
    public double YearOutSum() {
        int id=0;
        Cursor cursor;
        double inSum=0;
        Calendar c = Calendar.getInstance();// 获取当前系统日期
        int year = c.get(Calendar.YEAR);// 获取年份
        int month = c.get(Calendar.MONTH)+1;// 获取月份
        int day= c.get(Calendar.DAY_OF_MONTH);// 获取天数
        cursor = database
                .rawQuery("select _id,money,name,type,year,month,day,note from expenditure where year = ? ",
                        new String[] { String.valueOf(year)});
        if(cursor!=null&&cursor.getCount()>0){
            while(cursor.moveToNext()){
                inSum=inSum+cursor.getDouble(cursor.getColumnIndex("money"));
            }
            cursor.close();
        }
        return inSum;
    }
    public double WeekOutSum() {
        int id=0;
        double outSum=0;
        Calendar c = Calendar.getInstance();// 获取当前系统日期
        int year = c.get(Calendar.YEAR);// 获取年份
        int month = c.get(Calendar.MONTH)+1;// 获取月份
        int day= c.get(Calendar.DAY_OF_MONTH);// 获取天数
        int last=c.get(Calendar.DAY_OF_WEEK)-1;//周几
        Cursor cursor;
        if (day>=last) {
            int temp=last;
            int tempday=day;
            while(temp>0)
            {
                cursor = database.rawQuery(
                        "select _id,money,name,type,year,month,day,note from expenditure where year = ? and month=? and day=?",
                        new String[]{String.valueOf(year), String.valueOf(month), String.valueOf(tempday)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        outSum=outSum+cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
                temp--;
                tempday--;
            }
        }
        else if (day<last&&(month==3||month==5||month==7||month==8||month==10||month==12))
        {
            int temp=last;
            int tempday=day;
            while (temp>0&&tempday>0){
                cursor = database.rawQuery(
                        "select _id,money,name,type,year,month,day,note from expenditure where year = ? and month=? and day=?",
                        new String[]{String.valueOf(year), String.valueOf(month), String.valueOf(tempday)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        outSum=outSum+cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
                temp--;
                tempday--;
            }
            while (temp>0){
                tempday=31;
                cursor = database.rawQuery(
                        "select _id,money,name,type,year,month,day,note from expenditure where year = ? and month=? and day=?",
                        new String[]{String.valueOf(year), String.valueOf(month-1), String.valueOf(tempday)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        outSum=outSum+cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
                temp--;
                tempday--;
            }
        }
        else if (day<last&&(month==4||month==6||month==9||month==11))
        {
            int temp=last;
            int tempday=day;
            while (temp>0&&tempday>0){
                cursor = database.rawQuery(
                        "select _id,money,name,type,year,month,day,note from expenditure where year = ? and month=? and day=?",
                        new String[]{String.valueOf(year), String.valueOf(month), String.valueOf(tempday)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        outSum=outSum+cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
                temp--;
                tempday--;
            }
            while (temp>0){
                tempday=30;
                cursor = database.rawQuery(
                        "select _id,money,name,type,year,month,day,note from expenditure where year = ? and month=? and day=?",
                        new String[]{String.valueOf(year), String.valueOf(month-1), String.valueOf(tempday)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        outSum=outSum+cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
                temp--;
                tempday--;
            }
        }
        else if (day<last&&(month==2))
        {
            int temp=last;
            int tempday=day;
            while (temp>0&&tempday>0){
                cursor = database.rawQuery(
                        "select _id,money,name,type,year,month,day,note from expenditure where year = ? and month=? and day=?",
                        new String[]{String.valueOf(year), String.valueOf(month), String.valueOf(tempday)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        outSum=outSum+cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
                temp--;
                tempday--;
            }
            while (temp>0){
                tempday=28;
                cursor = database.rawQuery(
                        "select _id,money,name,type,year,month,day,note from expenditure where year = ? and month=? and day=?",
                        new String[]{String.valueOf(year), String.valueOf(month-1), String.valueOf(tempday)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        outSum=outSum+cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
                temp--;
                tempday--;
            }
        }
        else if(day<last&&month==1) {
            int temp = last;
            int tempday = day;
            while (temp > 0 && tempday > 0) {
                cursor = database.rawQuery(
                        "select _id,money,name,type,year,month,day,note from expenditure where year = ? and month=? and day=?",
                        new String[]{String.valueOf(year), String.valueOf(month), String.valueOf(tempday)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        outSum=outSum+cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
                temp--;
                tempday--;
            }
            while (temp > 0) {
                tempday = 31;
                cursor = database.rawQuery(
                        "select _id,money,name,type,year,month,day,note from expenditure where year = ? and month=? and day=?",
                        new String[]{String.valueOf(year-1), String.valueOf(12), String.valueOf(tempday)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        outSum=outSum+cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
                temp--;
                tempday--;
            }
        }
        return outSum;
    }
    public List<OutObject> queryToday(){
        Calendar c = Calendar.getInstance();// 获取当前系统日期
        int year = c.get(Calendar.YEAR);// 获取年份
        int month = c.get(Calendar.MONTH)+1;// 获取月份
        int day= c.get(Calendar.DAY_OF_MONTH);// 获取天数
        //ArrayList<TListItem> list=new ArrayList<TListItem>();
        List<OutObject> list = new ArrayList<OutObject>();
        SQLiteDatabase db=helper.getReadableDatabase();
        Cursor cursor = database.rawQuery(
                "select _id,money,name,type,year,month,day,note from expenditure where year = ? and month=? and day=?",
                new String[] { String.valueOf(year),String.valueOf(month),String.valueOf(day)});
        if(cursor!=null&&cursor.getCount()>0){
            while(cursor.moveToNext()){
                //TListItem item=
                /*
                list.add(new TListItem(0, R.mipmap.car,cursor.getString(cursor.getColumnIndex("type")),
                        cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex("note")),cursor.getInt(cursor.getColumnIndex("year")),
                        cursor.getInt(cursor.getColumnIndex("month")),cursor.getInt(cursor.getColumnIndex("day")),
                        cursor.getDouble(cursor.getColumnIndex("money"))));
                        */

                list.add(new OutObject(
                        cursor.getInt(cursor.getColumnIndex("_id")),
                        cursor.getDouble(cursor.getColumnIndex("money")),
                        cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex("type")),
                        cursor.getInt(cursor.getColumnIndex("year")),
                        cursor.getInt(cursor.getColumnIndex("month")),
                        cursor.getInt(cursor.getColumnIndex("day")),
                        cursor.getString(cursor.getColumnIndex("note"))));
            }
            cursor.close();
        }
        db.close();
        return list;
    }
    public List<OutObject> queryMonth(){
        Calendar c = Calendar.getInstance();// 获取当前系统日期
        int year = c.get(Calendar.YEAR);// 获取年份
        int month = c.get(Calendar.MONTH)+1;// 获取月份
        int day= c.get(Calendar.DAY_OF_MONTH);// 获取天数
        //ArrayList<TListItem> list=new ArrayList<TListItem>();
        List<OutObject> list = new ArrayList<OutObject>();
        SQLiteDatabase db=helper.getReadableDatabase();
        Cursor cursor = database.rawQuery(
                "select _id,money,name,type,year,month,day,note from expenditure where year = ? and month=?",
                new String[] { String.valueOf(year),String.valueOf(month)});
        if(cursor!=null&&cursor.getCount()>0){
            while(cursor.moveToNext()){
                list.add(new OutObject(
                        cursor.getInt(cursor.getColumnIndex("_id")),
                        cursor.getDouble(cursor.getColumnIndex("money")),
                        cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex("type")),
                        cursor.getInt(cursor.getColumnIndex("year")),
                        cursor.getInt(cursor.getColumnIndex("month")),
                        cursor.getInt(cursor.getColumnIndex("day")),
                        cursor.getString(cursor.getColumnIndex("note"))));
            }
            cursor.close();
        }
        db.close();
        return list;
    }
    public List<OutObject> queryYear(){
        Calendar c = Calendar.getInstance();// 获取当前系统日期
        int year = c.get(Calendar.YEAR);// 获取年份
        int month = c.get(Calendar.MONTH)+1;// 获取月份
        int day= c.get(Calendar.DAY_OF_MONTH);// 获取天数
        List<OutObject> list = new ArrayList<OutObject>();
        SQLiteDatabase db=helper.getReadableDatabase();
        Cursor cursor = database.rawQuery(
                "select _id,money,name,type,year,month,day,note from expenditure where year = ?",
                new String[] { String.valueOf(year)});
        if(cursor!=null&&cursor.getCount()>0){
            while(cursor.moveToNext()){
                list.add(new OutObject(
                        cursor.getInt(cursor.getColumnIndex("_id")),
                        cursor.getDouble(cursor.getColumnIndex("money")),
                        cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex("type")),
                        cursor.getInt(cursor.getColumnIndex("year")),
                        cursor.getInt(cursor.getColumnIndex("month")),
                        cursor.getInt(cursor.getColumnIndex("day")),
                        cursor.getString(cursor.getColumnIndex("note"))));
            }
            cursor.close();
        }
        db.close();
        return list;
    }
    public List<OutObject> queryWeek(){

        Calendar c = Calendar.getInstance();// 获取当前系统日期
        int year = c.get(Calendar.YEAR);// 获取年份
        int month = c.get(Calendar.MONTH)+1;// 获取月份
        int day= c.get(Calendar.DAY_OF_MONTH);// 获取天数
        int last=c.get(Calendar.DAY_OF_WEEK)-1;//周几
        List<OutObject> list = new ArrayList<OutObject>();
        SQLiteDatabase db=helper.getReadableDatabase();
        Cursor cursor;
        if (day>=last) {
            int temp=last;
            int tempday=day;
            while(temp>0)
            {
                cursor = database.rawQuery(
                        "select _id,money,name,type,year,month,day,note from expenditure where year = ? and month=? and day=?",
                        new String[]{String.valueOf(year), String.valueOf(month), String.valueOf(tempday)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        list.add(new OutObject(
                                cursor.getInt(cursor.getColumnIndex("_id")),
                                cursor.getDouble(cursor.getColumnIndex("money")),
                                cursor.getString(cursor.getColumnIndex("name")),
                                cursor.getString(cursor.getColumnIndex("type")),
                                cursor.getInt(cursor.getColumnIndex("year")),
                                cursor.getInt(cursor.getColumnIndex("month")),
                                cursor.getInt(cursor.getColumnIndex("day")),
                                cursor.getString(cursor.getColumnIndex("note"))));
                    }
                    cursor.close();
                }
                temp--;
                tempday--;
            }
        }
        else if (day<last&&(month==3||month==5||month==7||month==8||month==10||month==12))
        {
            int temp=last;
            int tempday=day;
            while (temp>0&&tempday>0){
                cursor = database.rawQuery(
                        "select _id,money,name,type,year,month,day,note from expenditure where year = ? and month=? and day=?",
                        new String[]{String.valueOf(year), String.valueOf(month), String.valueOf(tempday)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        list.add(new OutObject(
                                cursor.getInt(cursor.getColumnIndex("_id")),
                                cursor.getDouble(cursor.getColumnIndex("money")),
                                cursor.getString(cursor.getColumnIndex("name")),
                                cursor.getString(cursor.getColumnIndex("type")),
                                cursor.getInt(cursor.getColumnIndex("year")),
                                cursor.getInt(cursor.getColumnIndex("month")),
                                cursor.getInt(cursor.getColumnIndex("day")),
                                cursor.getString(cursor.getColumnIndex("note"))));
                    }
                    cursor.close();
                }
                temp--;
                tempday--;
            }
            while (temp>0){
                tempday=31;
                cursor = database.rawQuery(
                        "select _id,money,name,type,year,month,day,note from expenditure where year = ? and month=? and day=?",
                        new String[]{String.valueOf(year), String.valueOf(month-1), String.valueOf(tempday)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        list.add(new OutObject(
                                cursor.getInt(cursor.getColumnIndex("_id")),
                                cursor.getDouble(cursor.getColumnIndex("money")),
                                cursor.getString(cursor.getColumnIndex("name")),
                                cursor.getString(cursor.getColumnIndex("type")),
                                cursor.getInt(cursor.getColumnIndex("year")),
                                cursor.getInt(cursor.getColumnIndex("month")),
                                cursor.getInt(cursor.getColumnIndex("day")),
                                cursor.getString(cursor.getColumnIndex("note"))));
                    }
                    cursor.close();
                }
                temp--;
                tempday--;
            }
        }
        else if (day<last&&(month==4||month==6||month==9||month==11))
        {
            int temp=last;
            int tempday=day;
            while (temp>0&&tempday>0){
                cursor = database.rawQuery(
                        "select _id,money,name,type,year,month,day,note from expenditure where year = ? and month=? and day=?",
                        new String[]{String.valueOf(year), String.valueOf(month), String.valueOf(tempday)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        list.add(new OutObject(
                                cursor.getInt(cursor.getColumnIndex("_id")),
                                cursor.getDouble(cursor.getColumnIndex("money")),
                                cursor.getString(cursor.getColumnIndex("name")),
                                cursor.getString(cursor.getColumnIndex("type")),
                                cursor.getInt(cursor.getColumnIndex("year")),
                                cursor.getInt(cursor.getColumnIndex("month")),
                                cursor.getInt(cursor.getColumnIndex("day")),
                                cursor.getString(cursor.getColumnIndex("note"))));
                    }
                    cursor.close();
                }
                temp--;
                tempday--;
            }
            while (temp>0){
                tempday=30;
                cursor = database.rawQuery(
                        "select _id,money,name,type,year,month,day,note from expenditure where year = ? and month=? and day=?",
                        new String[]{String.valueOf(year), String.valueOf(month-1), String.valueOf(tempday)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        list.add(new OutObject(
                                cursor.getInt(cursor.getColumnIndex("_id")),
                                cursor.getDouble(cursor.getColumnIndex("money")),
                                cursor.getString(cursor.getColumnIndex("name")),
                                cursor.getString(cursor.getColumnIndex("type")),
                                cursor.getInt(cursor.getColumnIndex("year")),
                                cursor.getInt(cursor.getColumnIndex("month")),
                                cursor.getInt(cursor.getColumnIndex("day")),
                                cursor.getString(cursor.getColumnIndex("note"))));
                    }
                    cursor.close();
                }
                temp--;
                tempday--;
            }
        }
        else if (day<last&&(month==2))
        {
            int temp=last;
            int tempday=day;
            while (temp>0&&tempday>0){
                cursor = database.rawQuery(
                        "select _id,money,name,type,year,month,day,note from expenditure where year = ? and month=? and day=?",
                        new String[]{String.valueOf(year), String.valueOf(month), String.valueOf(tempday)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        list.add(new OutObject(
                                cursor.getInt(cursor.getColumnIndex("_id")),
                                cursor.getDouble(cursor.getColumnIndex("money")),
                                cursor.getString(cursor.getColumnIndex("name")),
                                cursor.getString(cursor.getColumnIndex("type")),
                                cursor.getInt(cursor.getColumnIndex("year")),
                                cursor.getInt(cursor.getColumnIndex("month")),
                                cursor.getInt(cursor.getColumnIndex("day")),
                                cursor.getString(cursor.getColumnIndex("note"))));
                    }
                    cursor.close();
                }
                temp--;
                tempday--;
            }
            while (temp>0){
                tempday=28;
                cursor = database.rawQuery(
                        "select _id,money,name,type,year,month,day,note from expenditure where year = ? and month=? and day=?",
                        new String[]{String.valueOf(year), String.valueOf(month-1), String.valueOf(tempday)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        list.add(new OutObject(
                                cursor.getInt(cursor.getColumnIndex("_id")),
                                cursor.getDouble(cursor.getColumnIndex("money")),
                                cursor.getString(cursor.getColumnIndex("name")),
                                cursor.getString(cursor.getColumnIndex("type")),
                                cursor.getInt(cursor.getColumnIndex("year")),
                                cursor.getInt(cursor.getColumnIndex("month")),
                                cursor.getInt(cursor.getColumnIndex("day")),
                                cursor.getString(cursor.getColumnIndex("note"))));
                    }
                    cursor.close();
                }
                temp--;
                tempday--;
            }
        }
        //TODO 修改
        else if(day<last&&month==1) {
            int temp = last;
            int tempday = day;
            while (temp > 0 && tempday > 0) {
                cursor = database.rawQuery(
                        "select _id,money,name,type,year,month,day,note from expenditure where year = ? and month=? and day=?",
                        new String[]{String.valueOf(year), String.valueOf(month), String.valueOf(tempday)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        list.add(new OutObject(
                                cursor.getInt(cursor.getColumnIndex("_id")),
                                cursor.getDouble(cursor.getColumnIndex("money")),
                                cursor.getString(cursor.getColumnIndex("name")),
                                cursor.getString(cursor.getColumnIndex("type")),
                                cursor.getInt(cursor.getColumnIndex("year")),
                                cursor.getInt(cursor.getColumnIndex("month")),
                                cursor.getInt(cursor.getColumnIndex("day")),
                                cursor.getString(cursor.getColumnIndex("note"))));
                    }
                    cursor.close();
                }
                temp--;
                tempday--;
            }
            while (temp > 0) {
                tempday = 31;
                cursor = database.rawQuery(
                        "select _id,money,name,type,year,month,day,note from expenditure where year = ? and month=? and day=?",
                        new String[]{String.valueOf(year-1), String.valueOf(12), String.valueOf(tempday)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        list.add(new OutObject(
                                cursor.getInt(cursor.getColumnIndex("_id")),
                                cursor.getDouble(cursor.getColumnIndex("money")),
                                cursor.getString(cursor.getColumnIndex("name")),
                                cursor.getString(cursor.getColumnIndex("type")),
                                cursor.getInt(cursor.getColumnIndex("year")),
                                cursor.getInt(cursor.getColumnIndex("month")),
                                cursor.getInt(cursor.getColumnIndex("day")),
                                cursor.getString(cursor.getColumnIndex("note"))));
                    }
                    cursor.close();
                }
                temp--;
                tempday--;
            }
        }
        db.close();
        return list;
    }
    public void detele(Integer... ids) {
        if (ids.length > 0){
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < ids.length; i++){
                sb.append('?').append(',');
            }
            sb.deleteCharAt(sb.length() - 1);
            database.execSQL("delete from expenditure where _id in (" + sb + ")",
                    (Object[]) ids);
        }
    }
    //获取收入最大编号
    public int getMaxId() {
        Cursor cursor = database.rawQuery("select max(_id) from expenditure", null);
        while (cursor.moveToLast()) {// 访问Cursor中的最后一条数据
            return cursor.getInt(0);// 获取访问到的数据，即最大编号
        }
        cursor.close();// 关闭游标
        return 0;// 如果没有数据，则返回0
    }
    //获取总记录数
    public long getCount() {
        Cursor cursor = database.rawQuery("select count(_id) from expenditure",
                null);// 获取支出信息的记录数
        if (cursor.moveToNext()){// 判断Cursor中是否有数据
            return cursor.getLong(0);// 返回总记录数
        }
        cursor.close();// 关闭游标
        return 0;// 如果没有数据，则返回0
    }
    //收入信息汇总
    public Map<String,Float> getTotal() {
        Cursor cursor = database.rawQuery("select type,sum(money) from expenditure group by type",null);
        int count=0;
        count=cursor.getCount();
        Map<String,Float> map=new HashMap<String,Float>();	//创建一个Map对象
        cursor.moveToFirst();	//移动第一条记录
        for(int i=0;i<count;i++){// 遍历所有的收入汇总信息
            map.put(cursor.getString(0),cursor.getFloat(1));
            //System.out.println("收入："+cursor.getString(0)+cursor.getFloat(1));
            cursor.moveToNext();//移到下条记录
        }
        cursor.close();// 关闭游标
        return map;// 返回Map对象
    }
    //获取收入信息
    public List<OutObject> getScrollData(int start, int count) {
        List<OutObject> out_object = new ArrayList<OutObject>();
        Cursor cursor = database.rawQuery("select * from expenditure limit ?,?",
                new String[] { String.valueOf(start), String.valueOf(count) });
        while (cursor.moveToNext()){
            out_object.add(new OutObject(cursor.getInt(cursor.getColumnIndex("_id")),
                    cursor.getDouble(cursor.getColumnIndex("money")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getString(cursor.getColumnIndex("type")),
                    cursor.getInt(cursor.getColumnIndex("year")),
                    cursor.getInt(cursor.getColumnIndex("month")),
                    cursor.getInt(cursor.getColumnIndex("day")),
                    cursor.getString(cursor.getColumnIndex("note"))));
        }
        cursor.close();// 关闭游标
        return out_object;// 返回集合
    }
    //TODO 图表分析支出数据获取
    public List<BaseMessage> outcomeData_analysis(int year1, int month1, int day1, int year2, int month2, int day2) {
        List<BaseMessage> mList;
        SQLiteDatabase db = helper.getReadableDatabase();
        double clothes = 0, food = 0, live = 0, drive = 0, other = 0;
        mList = new ArrayList<>();

        //TODO 年月相同
        if ((year1 == year2) && (month1 == month2)) {
            Cursor cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from expenditure where year=? and month=? and type=? and " +
                            "day<? and day>?",
                    new String[]{String.valueOf(year1), String.valueOf(month1), "衣服", String.valueOf(day2 + 1), String.valueOf(day1 - 1)});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    clothes = clothes + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from expenditure where year=? and month=? and type=? and " +
                            "day<? and day>?",
                    new String[]{String.valueOf(year1), String.valueOf(month1), "饮食", String.valueOf(day2 + 1), String.valueOf(day1 - 1)});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    food = food + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from expenditure where year=? and month=? and type=? and " +
                            "day<? and day>?",
                    new String[]{String.valueOf(year1), String.valueOf(month1), "住宿", String.valueOf(day2 + 1), String.valueOf(day1 - 1)});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    live = live + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from expenditure where year=? and month=? and type=? and " +
                            "day<? and day>?",
                    new String[]{String.valueOf(year1), String.valueOf(month1), "出行", String.valueOf(day2 + 1), String.valueOf(day1 - 1)});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    drive = drive + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from expenditure where year=? and month=? and type=? and " +
                            "day<? and day>?",
                    new String[]{String.valueOf(year1), String.valueOf(month1), "其他", String.valueOf(day2 + 1), String.valueOf(day1 - 1)});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    other = other + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
        }
        //TODO 年相同月不同
        else if (year1 == year2 && month1 < month2) {
            Cursor cursor;
            // TODO 年不同月不同 衣服
            //开始月
            if (month1 == 1 || month1 == 3 || month1 == 5 || month1 == 7 || month1 == 8 || month1 == 10 || month1 == 12) {
                Log.i("month1", month1 + "a");
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from expenditure where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "衣服", String.valueOf(32), String.valueOf(day1 - 1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        clothes = clothes + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            } else if (month1 == 4 || month1 == 6 || month1 == 9 || month1 == 11) {
                Log.i("month1", month1 + "b");
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from expenditure where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "衣服", String.valueOf(31), String.valueOf(day1 - 1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        clothes = clothes + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            } else if (month1 == 2) {
                Log.i("month1", month1 + "c");
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from expenditure where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "衣服", String.valueOf(30), String.valueOf(day1 - 1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        clothes = clothes + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            }
            //中间月
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from expenditure where year=? and month<? and month>? and type=?",
                    new String[]{String.valueOf(year1), String.valueOf(month2 + 1), String.valueOf(month1 - 1), "衣服"});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    clothes = clothes + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            //结束月
            Log.i("结束月", month2 + "");
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from expenditure where year=? and month=? and type=? and day<? and day>?",
                    new String[]{String.valueOf(year1), String.valueOf(month2), "衣服",
                            String.valueOf(day2 + 1), String.valueOf(0)});
//            Log.i("结束月",month2+"");
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    clothes = clothes + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            // TODO 年不同月不同 饮食
            //开始月
            if (month1 == 1 || month1 == 3 || month1 == 5 || month1 == 7 || month1 == 8 || month1 == 10 || month1 == 12) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from expenditure where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "饮食", String.valueOf(32), String.valueOf(day1 - 1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        food = food + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            } else if (month1 == 4 || month1 == 6 || month1 == 9 || month1 == 11) {
                Log.i("month1", month1 + "b");
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from expenditure where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "饮食", String.valueOf(31), String.valueOf(day1 - 1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        food = food + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            } else if (month1 == 2) {
                Log.i("month1", month1 + "c");
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from expenditure where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "饮食", String.valueOf(30), String.valueOf(day1 - 1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        food = food + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            }
            //中间月
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from expenditure where year=? and month<? and month>? and type=?",
                    new String[]{String.valueOf(year1), String.valueOf(month2 + 1), String.valueOf(month1 - 1), "饮食"});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    food = food + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            //结束月
            Log.i("结束月", month2 + "");
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from expenditure where year=? and month=? and type=? and day<? and day>?",
                    new String[]{String.valueOf(year1), String.valueOf(month2), "饮食",
                            String.valueOf(day2 + 1), String.valueOf(0)});
//            Log.i("结束月",month2+"");
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    food = food + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            // TODO 年不同月不同 红包
            //开始月
            if (month1 == 1 || month1 == 3 || month1 == 5 || month1 == 7 || month1 == 8 || month1 == 10 || month1 == 12) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from expenditure where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "住宿", String.valueOf(32), String.valueOf(day1 - 1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        live = live + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            } else if (month1 == 4 || month1 == 6 || month1 == 9 || month1 == 11) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from expenditure where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "住宿", String.valueOf(31), String.valueOf(day1 - 1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        live = live + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            } else if (month1 == 2) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from expenditure where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "住宿", String.valueOf(30), String.valueOf(day1 - 1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        live = live + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            }
            //中间月
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from expenditure where year=? and month<? and month>? and type=?",
                    new String[]{String.valueOf(year1), String.valueOf(month2 + 1), String.valueOf(month1 - 1), "住宿"});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    live = live + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            //结束月
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from expenditure where year=? and month=? and type=? and day<? and day>?",
                    new String[]{String.valueOf(year1), String.valueOf(month2), "住宿",
                            String.valueOf(day2 + 1), String.valueOf(0)});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    live = live + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            // TODO 年不同月不同 兼职
            //开始月
            if (month1 == 1 || month1 == 3 || month1 == 5 || month1 == 7 || month1 == 8 || month1 == 10 || month1 == 12) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from expenditure where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "出行", String.valueOf(32), String.valueOf(day1 - 1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        drive = drive + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            } else if (month1 == 4 || month1 == 6 || month1 == 9 || month1 == 11) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from expenditure where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "出行", String.valueOf(31), String.valueOf(day1 - 1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        drive = drive + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            } else if (month1 == 2) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from expenditure where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "出行", String.valueOf(30), String.valueOf(day1 - 1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        drive = drive + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            }
            //中间月
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from expenditure where year=? and month<? and month>? and type=?",
                    new String[]{String.valueOf(year1), String.valueOf(month2 + 1), String.valueOf(month1 - 1), "出行"});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    drive = drive + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            //结束月
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from expenditure where year=? and month=? and type=? and day<? and day>?",
                    new String[]{String.valueOf(year1), String.valueOf(month2), "出行",
                            String.valueOf(day2 + 1), String.valueOf(0)});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    drive = drive + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            // TODO 年不同月不同 其他
            //开始月
            if (month1 == 1 || month1 == 3 || month1 == 5 || month1 == 7 || month1 == 8 || month1 == 10 || month1 == 12) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from expenditure where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "其他", String.valueOf(32), String.valueOf(day1 - 1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        other = other + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            } else if (month1 == 4 || month1 == 6 || month1 == 9 || month1 == 11) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from expenditure where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "其他", String.valueOf(31), String.valueOf(day1 - 1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        other = other + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            } else if (month1 == 2) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from expenditure where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "其他", String.valueOf(30), String.valueOf(day1 - 1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        other = other + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            }
            //中间月
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from expenditure where year=? and month<? and month>? and type=?",
                    new String[]{String.valueOf(year1), String.valueOf(month2 + 1),
                            String.valueOf(month1 - 1), "其他"});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    other = other + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            //结束月
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from expenditure where year=? and month=? and type=? and day<? and day>?",
                    new String[]{String.valueOf(year1), String.valueOf(month2), "其他",
                            String.valueOf(day2 + 1), String.valueOf(0)});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    other = other + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }


        }
        //TODO 年不同
        else if (year1 < year2) {
            Cursor cursor;
            //TODO 工资
            //TODO 开始年 开始月
            if (month1 == 1 || month1 == 3 || month1 == 5 || month1 == 7 || month1 == 8 || month1 == 10 || month1 == 12) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from expenditure where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "衣服", String.valueOf(32), String.valueOf(day1-1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        clothes = clothes + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            } else if (month1 == 4 || month1 == 6 || month1 == 9 || month1 == 11) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from expenditure where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "衣服", String.valueOf(31), String.valueOf(day1-1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        clothes = clothes + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            } else if (month1 == 2) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from expenditure where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "衣服", String.valueOf(29), String.valueOf(day1-1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        clothes = clothes + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            }
            //TODO 开始年 中间到结束月
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from expenditure where year=? and month<? and month>? and type=?",
                    new String[]{String.valueOf(year1), String.valueOf(13), String.valueOf(month1), "衣服"});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    clothes = clothes + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            //TODO 中间年
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from expenditure where year<? and year>? and type=?",
                    new String[]{String.valueOf(year2), String.valueOf(year1), "衣服"});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    clothes = clothes + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            //TODO 结束年 结束月前面所有数据
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from expenditure where year=? and month<?  and type=?",
                    new String[]{String.valueOf(year2), String.valueOf(month2), "衣服"});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    clothes = clothes + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            //TODO 结束月数据
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from expenditure where year=? and month=?  and type=? and day<?",
                    new String[]{String.valueOf(year2), String.valueOf(month2), "衣服", String.valueOf(day2 + 1)});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    clothes = clothes + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }

            //TODO 股票
            //TODO 开始年 开始月
            if (month1 == 1 || month1 == 3 || month1 == 5 || month1 == 7 || month1 == 8 || month1 == 10 || month1 == 12) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from expenditure where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "饮食", String.valueOf(32), String.valueOf(day1-1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        food = food + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            } else if (month1 == 4 || month1 == 6 || month1 == 9 || month1 == 11) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from expenditure where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "饮食", String.valueOf(31), String.valueOf(day1-1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        food = food + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            } else if (month1 == 2) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from expenditure where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "饮食", String.valueOf(29), String.valueOf(day1-1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        food = food + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            }
            //TODO 开始年 中间到结束月
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from expenditure where year=? and month<? and month>? and type=?",
                    new String[]{String.valueOf(year1), String.valueOf(13), String.valueOf(month1), "饮食"});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    food = food + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            //TODO 中间年
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from expenditure where year<? and year>? and type=?",
                    new String[]{String.valueOf(year2), String.valueOf(year1), "饮食"});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    food = food + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            //TODO 结束年 结束月前面所有数据
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from expenditure where year=? and month<?  and type=?",
                    new String[]{String.valueOf(year2), String.valueOf(month2), "饮食"});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    food = food + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            //TODO 结束月数据
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from expenditure where year=? and month=?  and type=? and day<?",
                    new String[]{String.valueOf(year2), String.valueOf(month2), "饮食", String.valueOf(day2 + 1)});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    food = food + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }

            //TODO 住宿
            //TODO 开始年 开始月
            if (month1 == 1 || month1 == 3 || month1 == 5 || month1 == 7 || month1 == 8 || month1 == 10 || month1 == 12) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from expenditure where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "住宿", String.valueOf(32), String.valueOf(day1-1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        live = live + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            } else if (month1 == 4 || month1 == 6 || month1 == 9 || month1 == 11) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from expenditure where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "住宿", String.valueOf(31), String.valueOf(day1-1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        live = live + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            } else if (month1 == 2) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from expenditure where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "住宿", String.valueOf(29), String.valueOf(day1-1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        live = live + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            }
            //TODO 开始年 中间到结束月
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from expenditure where year=? and month<? and month>? and type=?",
                    new String[]{String.valueOf(year1), String.valueOf(13), String.valueOf(month1), "住宿"});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    live = live + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            //TODO 中间年
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from expenditure where year<? and year>? and type=?",
                    new String[]{String.valueOf(year2), String.valueOf(year1), "住宿"});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    live = live + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            //TODO 结束年 结束月前面所有数据
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from expenditure where year=? and month<?  and type=?",
                    new String[]{String.valueOf(year2), String.valueOf(month2), "住宿"});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    live = live + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            //TODO 结束月数据
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from expenditure where year=? and month=?  and type=? and day<?",
                    new String[]{String.valueOf(year2), String.valueOf(month2), "住宿", String.valueOf(day2 + 1)});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    live = live + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }

            //TODO 出行
            //TODO 开始年 开始月
            if (month1 == 1 || month1 == 3 || month1 == 5 || month1 == 7 || month1 == 8 || month1 == 10 || month1 == 12) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from expenditure where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "出行", String.valueOf(32), String.valueOf(day1-1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        drive = drive + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            } else if (month1 == 4 || month1 == 6 || month1 == 9 || month1 == 11) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from expenditure where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "出行", String.valueOf(31), String.valueOf(day1-1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        drive = drive + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            } else if (month1 == 2) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from expenditure where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "出行", String.valueOf(29), String.valueOf(day1-1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        drive = drive + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            }
            //TODO 开始年 中间到结束月
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from expenditure where year=? and month<? and month>? and type=?",
                    new String[]{String.valueOf(year1), String.valueOf(13), String.valueOf(month1), "出行"});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    drive = drive + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            //TODO 中间年
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from expenditure where year<? and year>? and type=?",
                    new String[]{String.valueOf(year2), String.valueOf(year1), "出行"});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    drive = drive + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            //TODO 结束年 结束月前面所有数据
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from expenditure where year=? and month<?  and type=?",
                    new String[]{String.valueOf(year2), String.valueOf(month2), "出行"});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    drive = drive + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            //TODO 结束月数据
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from expenditure where year=? and month=?  and type=? and day<?",
                    new String[]{String.valueOf(year2), String.valueOf(month2), "出行", String.valueOf(day2 + 1)});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    drive = drive + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            //TODO 其他
            //TODO 开始年 开始月
            if (month1 == 1 || month1 == 3 || month1 == 5 || month1 == 7 || month1 == 8 || month1 == 10 || month1 == 12) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from expenditure where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "其他", String.valueOf(32), String.valueOf(day1-1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        other = other + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            } else if (month1 == 4 || month1 == 6 || month1 == 9 || month1 == 11) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from expenditure where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "其他", String.valueOf(31), String.valueOf(day1-1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        other = other + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            } else if (month1 == 2) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from expenditure where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "其他", String.valueOf(29), String.valueOf(day1-1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        other = other + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            }
            //TODO 开始年 中间到结束月
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from expenditure where year=? and month<? and month>? and type=?",
                    new String[]{String.valueOf(year1), String.valueOf(13), String.valueOf(month1), "其他"});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    other = other + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            //TODO 中间年
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from expenditure where year<? and year>? and type=?",
                    new String[]{String.valueOf(year2), String.valueOf(year1), "其他"});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    other = other + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            //TODO 结束年 结束月前面所有数据
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from expenditure where year=? and month<?  and type=?",
                    new String[]{String.valueOf(year2), String.valueOf(month2), "其他"});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    other = other + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            //TODO 结束月数据
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from expenditure where year=? and month=?  and type=? and day<?",
                    new String[]{String.valueOf(year2), String.valueOf(month2), "其他", String.valueOf(day2 + 1)});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    other = other + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }

        }

        if (clothes != 0) {
            BaseMessage mes = new BaseMessage();
            mes.percent = (float) clothes;
            mes.content = "衣服";
            mes.color = Color.parseColor("#0ff000");
            mList.add(mes);
        }

        if (food != 0) {
            BaseMessage message = new BaseMessage();
            message.percent = (float) food;
            message.content = "饮食";
            message.color = Color.parseColor("#fff000");
            mList.add(message);
        }

        if (live != 0) {
            BaseMessage mes0 = new BaseMessage();
            mes0.percent = (float) live;
            mes0.content = "住宿";
            mes0.color = Color.parseColor("#ff00ff");
            mList.add(mes0);
        }

        if (drive != 0) {
            BaseMessage mes1 = new BaseMessage();
            mes1.percent = (float) drive;
            mes1.content = "出行";
            mes1.color = Color.parseColor("#00ffff");
            mList.add(mes1);
        }
        if (other != 0) {
            BaseMessage mes4 = new BaseMessage();
            mes4.percent = (float) other;
            mes4.content = "其他";
            mes4.color = Color.parseColor("#009933");
            mList.add(mes4);
        }

        sum = clothes + food + live + drive + other;
        db.close();
        return mList;
    }
}
