package com.example.finance.DBA;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import com.example.finance.AboutListView.TListItem;
import com.example.finance.BaseMessage;
import com.example.finance.R;
import com.example.finance.TodayRecord;

public class InDB {
    private DateBaseSave helper;
    private SQLiteDatabase database;
    TodayRecord TD;
    public static double sum=0;
    public static double currentSum = 0;
    public InDB(Context context) {
        helper = new DateBaseSave(context);
        database = helper.getWritableDatabase();
    }

    public void add(InObject in_object) {
        database.execSQL(
                "insert into income (_id,money,name,type,year,month,day,note) values (?,?,?,?,?,?,?,?)",
                new Object[]{in_object.getid(), in_object.getMoney(),
                        in_object.getName(), in_object.getType(),
                        in_object.getYear(), in_object.getMonth(),
                        in_object.getDay(), in_object.getNote()});
    }
    public List<BaseMessage> currentYearData(){
        List<BaseMessage> mList = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        int day1=1, day2;
        //判断当前的月
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month ==12){
            day2 = 31;
        }else if (month == 2){
            day2 = 29;
        }else {
            day2 = 30;
        }

        double salary = 0, stock = 0, red_packet = 0, part_time = 0, bonus = 0, divide = 0, other = 0;

        Cursor cursor = database.rawQuery(
                "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
                        "day<? and day>?",
                new String[]{String.valueOf(year), String.valueOf(month), "工资", String.valueOf(day2 + 1), String.valueOf(day1 - 1)});
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                salary = salary + cursor.getDouble(cursor.getColumnIndex("money"));
            }
            cursor.close();
        }
        cursor = database.rawQuery(
                "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
                        "day<? and day>?",
                new String[]{String.valueOf(year), String.valueOf(month), "股票", String.valueOf(day2 + 1), String.valueOf(day1 - 1)});
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                stock = stock + cursor.getDouble(cursor.getColumnIndex("money"));
            }
            cursor.close();
        }
        cursor = database.rawQuery(
                "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
                        "day<? and day>?",
                new String[]{String.valueOf(year), String.valueOf(month), "红包", String.valueOf(day2 + 1), String.valueOf(day1 - 1)});
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                red_packet = red_packet + cursor.getDouble(cursor.getColumnIndex("money"));
            }
            cursor.close();
        }
        cursor = database.rawQuery(
                "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
                        "day<? and day>?",
                new String[]{String.valueOf(year), String.valueOf(month), "兼职", String.valueOf(day2 + 1), String.valueOf(day1 - 1)});
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                part_time = part_time + cursor.getDouble(cursor.getColumnIndex("money"));
            }
            cursor.close();
        }
        cursor = database.rawQuery(
                "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
                        "day<? and day>?",
                new String[]{String.valueOf(year), String.valueOf(month), "奖金", String.valueOf(day2 + 1), String.valueOf(day1 - 1)});
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                bonus = bonus + cursor.getDouble(cursor.getColumnIndex("money"));
            }
            cursor.close();
        }
        cursor = database.rawQuery(
                "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
                        "day<? and day>?",
                new String[]{String.valueOf(year), String.valueOf(month), "分红", String.valueOf(day2 + 1), String.valueOf(day1 - 1)});
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                divide = divide + cursor.getDouble(cursor.getColumnIndex("money"));
            }
            cursor.close();
        }
        cursor = database.rawQuery(
                "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
                        "day<? and day>?",
                new String[]{String.valueOf(year), String.valueOf(month), "其他", String.valueOf(day2 + 1), String.valueOf(day1 - 1)});
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                other = other + cursor.getDouble(cursor.getColumnIndex("money"));
            }
            cursor.close();
        }
        if (salary != 0) {
            BaseMessage mes = new BaseMessage();
            mes.percent = (float) salary;
            mes.content = "工资";
            mes.color = Color.parseColor("#0ff000");
            mList.add(mes);
        }

        if (stock != 0) {
            BaseMessage message = new BaseMessage();
            message.percent = (float) stock;
            message.content = "股票";
            message.color = Color.parseColor("#fff000");
            mList.add(message);
        }

        if (red_packet != 0) {
            BaseMessage mes0 = new BaseMessage();
            mes0.percent = (float) red_packet;
            mes0.content = "红包";
            mes0.color = Color.parseColor("#ff00ff");
            mList.add(mes0);
        }

        if (part_time != 0) {
            BaseMessage mes1 = new BaseMessage();
            mes1.percent = (float) part_time;
            mes1.content = "兼职";
            mes1.color = Color.parseColor("#00ffff");
            mList.add(mes1);
        }
        if (bonus != 0) {
            BaseMessage mes2 = new BaseMessage();
            mes2.percent = (float) bonus;
            mes2.content = "奖金";
            mes2.color = Color.parseColor("#0099ff");
            mList.add(mes2);
        }
        if (divide != 0) {
            BaseMessage mes3 = new BaseMessage();
            mes3.percent = (float) divide;
            mes3.content = "分红";
            mes3.color = Color.parseColor("#0099dd");
            mList.add(mes3);
        }
        if (other != 0) {
            BaseMessage mes4 = new BaseMessage();
            mes4.percent = (float) other;
            mes4.content = "其他";
            mes4.color = Color.parseColor("#009933");
            mList.add(mes4);
        }
        currentSum = salary + stock + red_packet + part_time + bonus + divide + other;
        Log.i("currentSum",currentSum+"");
        Log.i("currentSalary",salary+"");
        Log.i("month",month+"");
        db.close();
        return mList;
    }
    public void update(InObject in_object) {
        database.execSQL(
                "update income set money = ?,name = ?,type = ?,year=?,month=?,day = ?,note = ? where _id = ?",
                new Object[]{in_object.getMoney(),
                        in_object.getName(), in_object.getType(),
                        in_object.getYear(), in_object.getMonth(),
                        in_object.getDay(), in_object.getNote(),
                        in_object.getid()});
    }

    public InObject find(int id) {
        Cursor cursor = database
                .rawQuery(
                        "select _id,money,name,type,year,month,day,note from income where _id = ?",
                        new String[]{String.valueOf(id)});
        if (cursor.moveToNext()) {
            return new InObject(
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

    public double InSum() {
        int id = 0;
        Cursor cursor;
        double inSum = 0;
        Calendar c = Calendar.getInstance();// 获取当前系统日期
        int year = c.get(Calendar.YEAR);// 获取年份
        int month = c.get(Calendar.MONTH) + 1;// 获取月份
        int day = c.get(Calendar.DAY_OF_MONTH);// 获取天数
        //while (id<=getMaxId())
        //{
         /*
            cursor = database
                    .rawQuery(
                            "select _id,money from income where _id = ?",
                            new String[] { String.valueOf(id) });*/
        cursor = database
                .rawQuery("select _id,money,name,type,year,month,day,note from income where year = ? and month=? and day=?",
                        new String[]{String.valueOf(year), String.valueOf(month), String.valueOf(day)});
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                inSum = inSum + cursor.getDouble(cursor.getColumnIndex("money"));
            }
            cursor.close();
        }
        //    id++;
        // }
        return inSum;
    }

    public double MonthInSum() {
        int id = 0;
        Cursor cursor;
        double inSum = 0;
        Calendar c = Calendar.getInstance();// 获取当前系统日期
        int year = c.get(Calendar.YEAR);// 获取年份
        int month = c.get(Calendar.MONTH) + 1;// 获取月份
        int day = c.get(Calendar.DAY_OF_MONTH);// 获取天数
        cursor = database
                .rawQuery("select _id,money,name,type,year,month,day,note from income where year = ? and month=? ",
                        new String[]{String.valueOf(year), String.valueOf(month)});
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                inSum = inSum + cursor.getDouble(cursor.getColumnIndex("money"));
            }
            cursor.close();
        }
        return inSum;
    }

    public double YearInSum() {
        int id = 0;
        Cursor cursor;
        double inSum = 0;
        Calendar c = Calendar.getInstance();// 获取当前系统日期
        int year = c.get(Calendar.YEAR);// 获取年份
        int month = c.get(Calendar.MONTH) + 1;// 获取月份
        int day = c.get(Calendar.DAY_OF_MONTH);// 获取天数
        cursor = database
                .rawQuery("select _id,money,name,type,year,month,day,note from income where year = ?  ",
                        new String[]{String.valueOf(year)});
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                inSum = inSum + cursor.getDouble(cursor.getColumnIndex("money"));
            }
            cursor.close();
        }
        return inSum;
    }

    public double WeekInSum() {
        int id = 0;
        double inSum = 0;
        Calendar c = Calendar.getInstance();// 获取当前系统日期
        int year = c.get(Calendar.YEAR);// 获取年份
        int month = c.get(Calendar.MONTH) + 1;// 获取月份
        int day = c.get(Calendar.DAY_OF_MONTH);// 获取天数
        int last = c.get(Calendar.DAY_OF_WEEK)-1;//周几
        if(last==0){
            last=7;
        }
        Cursor cursor;
        if (day >= last) {
            int temp = last;
            int tempday = day;
            while (temp > 0) {
                cursor = database.rawQuery(
                        "select _id,money,name,type,year,month,day,note from income where year = ? and month=? and day=?",
                        new String[]{String.valueOf(year), String.valueOf(month), String.valueOf(tempday)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        inSum = inSum + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
                temp--;
                tempday--;
            }
        } else if (day < last && (month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12)) {
            int temp = last;
            int tempday = day;
            while (temp > 0 && tempday > 0) {
                cursor = database.rawQuery(
                        "select _id,money,name,type,year,month,day,note from income where year = ? and month=? and day=?",
                        new String[]{String.valueOf(year), String.valueOf(month), String.valueOf(tempday)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        inSum = inSum + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
                temp--;
                tempday--;
            }
            while (temp > 0) {
                tempday = 31;
                cursor = database.rawQuery(
                        "select _id,money,name,type,year,month,day,note from income where year = ? and month=? and day=?",
                        new String[]{String.valueOf(year), String.valueOf(month - 1), String.valueOf(tempday)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        inSum = inSum + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
                temp--;
                tempday--;
            }
        } else if (day < last && (month == 4 || month == 6 || month == 9 || month == 11)) {
            int temp = last;
            int tempday = day;
            while (temp > 0 && tempday > 0) {
                cursor = database.rawQuery(
                        "select _id,money,name,type,year,month,day,note from income where year = ? and month=? and day=?",
                        new String[]{String.valueOf(year), String.valueOf(month), String.valueOf(tempday)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        inSum = inSum + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
                temp--;
                tempday--;
            }
            while (temp > 0) {
                tempday = 30;
                cursor = database.rawQuery(
                        "select _id,money,name,type,year,month,day,note from income where year = ? and month=? and day=?",
                        new String[]{String.valueOf(year), String.valueOf(month - 1), String.valueOf(tempday)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        inSum = inSum + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
                temp--;
                tempday--;
            }
        } else if (day < last && (month == 2)) {
            int temp = last;
            int tempday = day;
            while (temp > 0 && tempday > 0) {
                cursor = database.rawQuery(
                        "select _id,money,name,type,year,month,day,note from income where year = ? and month=? and day=?",
                        new String[]{String.valueOf(year), String.valueOf(month), String.valueOf(tempday)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        inSum = inSum + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
                temp--;
                tempday--;
            }
            while (temp > 0) {
                tempday = 28;
                cursor = database.rawQuery(
                        "select _id,money,name,type,year,month,day,note from income where year = ? and month=? and day=?",
                        new String[]{String.valueOf(year), String.valueOf(month - 1), String.valueOf(tempday)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        inSum = inSum + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
                temp--;
                tempday--;
            }
        }
        //TODO 修改
        else if (day < last && month == 1) {
            int temp = last;
            int tempday = day;
            while (temp > 0 && tempday > 0) {
                cursor = database.rawQuery(
                        "select _id,money,name,type,year,month,day,note from income where year = ? and month=? and day=?",
                        new String[]{String.valueOf(year), String.valueOf(month), String.valueOf(tempday)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        inSum = inSum + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
                temp--;
                tempday--;
            }
            while (temp > 0) {
                tempday = 31;
                cursor = database.rawQuery(
                        "select _id,money,name,type,year,month,day,note from income where year = ? and month=? and day=?",
                        new String[]{String.valueOf(year - 1), String.valueOf(12), String.valueOf(tempday)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        inSum = inSum + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
                temp--;
                tempday--;
            }
        }
        return inSum;
    }

    public List<InObject> queryToday() {
        Calendar c = Calendar.getInstance();// 获取当前系统日期
        int year = c.get(Calendar.YEAR);// 获取年份
        int month = c.get(Calendar.MONTH) + 1;// 获取月份
        int day = c.get(Calendar.DAY_OF_MONTH);// 获取天数
        List<InObject> list = new ArrayList<InObject>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = database.rawQuery(
                "select _id,money,name,type,year,month,day,note from income where year = ? and month=? and day=?",
                new String[]{String.valueOf(year), String.valueOf(month), String.valueOf(day)});
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                list.add(new InObject(
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

    public List<InObject> queryMonth() {
        Calendar c = Calendar.getInstance();// 获取当前系统日期
        int year = c.get(Calendar.YEAR);// 获取年份
        int month = c.get(Calendar.MONTH) + 1;// 获取月份
        int day = c.get(Calendar.DAY_OF_MONTH);// 获取天数
        //ArrayList<TListItem> list=new ArrayList<TListItem>();
        List<InObject> list = new ArrayList<InObject>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = database.rawQuery(
                "select _id,money,name,type,year,month,day,note from income where year = ? and month=?",
                new String[]{String.valueOf(year), String.valueOf(month)});
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                list.add(new InObject(
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

    public List<InObject> queryYear() {
        Calendar c = Calendar.getInstance();// 获取当前系统日期
        int year = c.get(Calendar.YEAR);// 获取年份
        int month = c.get(Calendar.MONTH) + 1;// 获取月份
        int day = c.get(Calendar.DAY_OF_MONTH);// 获取天数
        List<InObject> list = new ArrayList<InObject>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = database.rawQuery(
                "select _id,money,name,type,year,month,day,note from income where year = ?",
                new String[]{String.valueOf(year)});
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                list.add(new InObject(
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

    public List<InObject> queryWeek() {

        Calendar c = Calendar.getInstance();// 获取当前系统日期
        int year = c.get(Calendar.YEAR);// 获取年份
        int month = c.get(Calendar.MONTH) + 1;// 获取月份
        int day = c.get(Calendar.DAY_OF_MONTH);// 获取天数
        int last = c.get(Calendar.DAY_OF_WEEK)-1;//周几
        if(last==0){
            last=7;
        }
        List<InObject> list = new ArrayList<InObject>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor;
        if (day >= last) {
            int temp = last;
            int tempday = day;
            while (temp > 0) {
                cursor = database.rawQuery(
                        "select _id,money,name,type,year,month,day,note from income where year = ? and month=? and day=?",
                        new String[]{String.valueOf(year), String.valueOf(month), String.valueOf(tempday)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        list.add(new InObject(
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
        } else if (day < last && (month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12)) {
            int temp = last;
            int tempday = day;
            while (temp > 0 && tempday > 0) {
                cursor = database.rawQuery(
                        "select _id,money,name,type,year,month,day,note from income where year = ? and month=? and day=?",
                        new String[]{String.valueOf(year), String.valueOf(month), String.valueOf(tempday)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        list.add(new InObject(
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
                        "select _id,money,name,type,year,month,day,note from income where year = ? and month=? and day=?",
                        new String[]{String.valueOf(year), String.valueOf(month - 1), String.valueOf(tempday)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        list.add(new InObject(
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
        } else if (day < last && (month == 4 || month == 6 || month == 9 || month == 11)) {
            int temp = last;
            int tempday = day;
            while (temp > 0 && tempday > 0) {
                cursor = database.rawQuery(
                        "select _id,money,name,type,year,month,day,note from income where year = ? and month=? and day=?",
                        new String[]{String.valueOf(year), String.valueOf(month), String.valueOf(tempday)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        list.add(new InObject(
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
                tempday = 30;
                cursor = database.rawQuery(
                        "select _id,money,name,type,year,month,day,note from income where year = ? and month=? and day=?",
                        new String[]{String.valueOf(year), String.valueOf(month - 1), String.valueOf(tempday)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        list.add(new InObject(
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
        } else if (day < last && (month == 2)) {
            int temp = last;
            int tempday = day;
            while (temp > 0 && tempday > 0) {
                cursor = database.rawQuery(
                        "select _id,money,name,type,year,month,day,note from income where year = ? and month=? and day=?",
                        new String[]{String.valueOf(year), String.valueOf(month), String.valueOf(tempday)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        list.add(new InObject(
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
                tempday = 28;
                cursor = database.rawQuery(
                        "select _id,money,name,type,year,month,day,note from income where year = ? and month=? and day=?",
                        new String[]{String.valueOf(year), String.valueOf(month - 1), String.valueOf(tempday)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        list.add(new InObject(
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
        else if (day < last && month == 1) {
            int temp = last;
            int tempday = day;
            while (temp > 0 && tempday > 0) {
                cursor = database.rawQuery(
                        "select _id,money,name,type,year,month,day,note from income where year = ? and month=? and day=?",
                        new String[]{String.valueOf(year), String.valueOf(month), String.valueOf(tempday)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        list.add(new InObject(
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
                        "select _id,money,name,type,year,month,day,note from income where year = ? and month=? and day=?",
                        new String[]{String.valueOf(year - 1), String.valueOf(12), String.valueOf(tempday)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        list.add(new InObject(
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
        if (ids.length > 0) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < ids.length; i++) {
                sb.append('?').append(',');
            }
            sb.deleteCharAt(sb.length() - 1);
            database.execSQL("delete from income where _id in (" + sb + ")",
                    (Object[]) ids);
        }
    }

    //获取收入最大编号
    public int getMaxId() {
        Cursor cursor = database.rawQuery("select max(_id) from income", null);
        while (cursor.moveToLast()) {// 访问Cursor中的最后一条数据
            return cursor.getInt(0);// 获取访问到的数据，即最大编号
        }
        cursor.close();// 关闭游标
        return 0;// 如果没有数据，则返回0
    }

    //获取总记录数
    public long getCount() {
        Cursor cursor = database.rawQuery("select count(_id) from income",
                null);// 获取支出信息的记录数
        if (cursor.moveToNext()) {// 判断Cursor中是否有数据
            return cursor.getLong(0);// 返回总记录数
        }
        cursor.close();// 关闭游标
        return 0;// 如果没有数据，则返回0
    }

    //收入信息汇总
    public Map<String, Float> getTotal() {
        Cursor cursor = database.rawQuery("select type,sum(money) from income group by type", null);
        int count = 0;
        count = cursor.getCount();
        Map<String, Float> map = new HashMap<String, Float>();    //创建一个Map对象
        cursor.moveToFirst();    //移动第一条记录
        for (int i = 0; i < count; i++) {// 遍历所有的收入汇总信息
            map.put(cursor.getString(0), cursor.getFloat(1));
            //System.out.println("收入："+cursor.getString(0)+cursor.getFloat(1));
            cursor.moveToNext();//移到下条记录
        }
        cursor.close();// 关闭游标
        return map;// 返回Map对象
    }

    //获取收入信息
    public List<InObject> getScrollData(int start, int count) {
        List<InObject> in_object = new ArrayList<InObject>();
        Cursor cursor = database.rawQuery("select * from income limit ?,?",
                new String[]{String.valueOf(start), String.valueOf(count)});
        while (cursor.moveToNext()) {
            in_object.add(new InObject(
                    cursor.getInt(cursor.getColumnIndex("_id")),
                    cursor.getDouble(cursor.getColumnIndex("money")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getString(cursor.getColumnIndex("type")),
                    cursor.getInt(cursor.getColumnIndex("year")),
                    cursor.getInt(cursor.getColumnIndex("month")),
                    cursor.getInt(cursor.getColumnIndex("day")),
                    cursor.getString(cursor.getColumnIndex("note"))));
        }
        cursor.close();// 关闭游标
        return in_object;// 返回集合
    }

    //TODO 图表分析数据获取
    public List<BaseMessage> incomeData_analysis(int year1, int month1, int day1, int year2, int month2, int day2) {
        List<BaseMessage> mList;
        SQLiteDatabase db = helper.getReadableDatabase();
        double salary = 0, stock = 0, red_packet = 0, part_time = 0, bonus = 0, divide = 0, other = 0;
        mList = new ArrayList<>();

        //TODO 年月相同
        if ((year1 == year2) && (month1 == month2)) {
            Cursor cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
                            "day<? and day>?",
                    new String[]{String.valueOf(year1), String.valueOf(month1), "工资", String.valueOf(day2 + 1), String.valueOf(day1 - 1)});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    salary = salary + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
                            "day<? and day>?",
                    new String[]{String.valueOf(year1), String.valueOf(month1), "股票", String.valueOf(day2 + 1), String.valueOf(day1 - 1)});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    stock = stock + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
                            "day<? and day>?",
                    new String[]{String.valueOf(year1), String.valueOf(month1), "红包", String.valueOf(day2 + 1), String.valueOf(day1 - 1)});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    red_packet = red_packet + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
                            "day<? and day>?",
                    new String[]{String.valueOf(year1), String.valueOf(month1), "兼职", String.valueOf(day2 + 1), String.valueOf(day1 - 1)});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    part_time = part_time + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
                            "day<? and day>?",
                    new String[]{String.valueOf(year1), String.valueOf(month1), "奖金", String.valueOf(day2 + 1), String.valueOf(day1 - 1)});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    bonus = bonus + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
                            "day<? and day>?",
                    new String[]{String.valueOf(year1), String.valueOf(month1), "分红", String.valueOf(day2 + 1), String.valueOf(day1 - 1)});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    divide = divide + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
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
            // TODO 年不同月不同 工资
            //开始月
            if (month1 == 1 || month1 == 3 || month1 == 5 || month1 == 7 || month1 == 8 || month1 == 10 || month1 == 12) {
                Log.i("month1", month1 + "a");
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "工资", String.valueOf(32), String.valueOf(day1 - 1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        salary = salary + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            } else if (month1 == 4 || month1 == 6 || month1 == 9 || month1 == 11) {
                Log.i("month1", month1 + "b");
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "工资", String.valueOf(31), String.valueOf(day1 - 1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        salary = salary + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            } else if (month1 == 2) {
                Log.i("month1", month1 + "c");
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "工资", String.valueOf(30), String.valueOf(day1 - 1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        salary = salary + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            }
            //中间月
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from income where year=? and month<? and month>? and type=?",
                    new String[]{String.valueOf(year1), String.valueOf(month2 + 1), String.valueOf(month1 - 1), "工资"});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    salary = salary + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            //结束月
            Log.i("结束月", month2 + "");
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from income where year=? and month=? and type=? and day<? and day>?",
                    new String[]{String.valueOf(year1), String.valueOf(month2), "工资",
                            String.valueOf(day2 + 1), String.valueOf(0)});
//            Log.i("结束月",month2+"");
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    salary = salary + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            // TODO 年不同月不同 股票
            //开始月
            if (month1 == 1 || month1 == 3 || month1 == 5 || month1 == 7 || month1 == 8 || month1 == 10 || month1 == 12) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "股票", String.valueOf(32), String.valueOf(day1 - 1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        stock = stock + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            } else if (month1 == 4 || month1 == 6 || month1 == 9 || month1 == 11) {
                Log.i("month1", month1 + "b");
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "股票", String.valueOf(31), String.valueOf(day1 - 1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        stock = stock + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            } else if (month1 == 2) {
                Log.i("month1", month1 + "c");
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "股票", String.valueOf(30), String.valueOf(day1 - 1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        stock = stock + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            }
            //中间月
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from income where year=? and month<? and month>? and type=?",
                    new String[]{String.valueOf(year1), String.valueOf(month2 + 1), String.valueOf(month1 - 1), "股票"});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    stock = stock + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            //结束月
            Log.i("结束月", month2 + "");
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from income where year=? and month=? and type=? and day<? and day>?",
                    new String[]{String.valueOf(year1), String.valueOf(month2), "股票",
                            String.valueOf(day2 + 1), String.valueOf(0)});
//            Log.i("结束月",month2+"");
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    stock = stock + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            // TODO 年不同月不同 红包
            //开始月
            if (month1 == 1 || month1 == 3 || month1 == 5 || month1 == 7 || month1 == 8 || month1 == 10 || month1 == 12) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "红包", String.valueOf(32), String.valueOf(day1 - 1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        red_packet = red_packet + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            } else if (month1 == 4 || month1 == 6 || month1 == 9 || month1 == 11) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "红包", String.valueOf(31), String.valueOf(day1 - 1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        red_packet = red_packet + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            } else if (month1 == 2) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "红包", String.valueOf(30), String.valueOf(day1 - 1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        red_packet = red_packet + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            }
            //中间月
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from income where year=? and month<? and month>? and type=?",
                    new String[]{String.valueOf(year1), String.valueOf(month2 + 1), String.valueOf(month1 - 1), "红包"});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    red_packet = red_packet + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            //结束月
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from income where year=? and month=? and type=? and day<? and day>?",
                    new String[]{String.valueOf(year1), String.valueOf(month2), "红包",
                            String.valueOf(day2 + 1), String.valueOf(0)});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    red_packet = red_packet + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            // TODO 年不同月不同 兼职
            //开始月
            if (month1 == 1 || month1 == 3 || month1 == 5 || month1 == 7 || month1 == 8 || month1 == 10 || month1 == 12) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "兼职", String.valueOf(32), String.valueOf(day1 - 1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        part_time = part_time + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            } else if (month1 == 4 || month1 == 6 || month1 == 9 || month1 == 11) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "兼职", String.valueOf(31), String.valueOf(day1 - 1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        part_time = part_time + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            } else if (month1 == 2) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "兼职", String.valueOf(30), String.valueOf(day1 - 1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        part_time = part_time + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            }
            //中间月
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from income where year=? and month<? and month>? and type=?",
                    new String[]{String.valueOf(year1), String.valueOf(month2 + 1), String.valueOf(month1 - 1), "兼职"});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    part_time = part_time + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            //结束月
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from income where year=? and month=? and type=? and day<? and day>?",
                    new String[]{String.valueOf(year1), String.valueOf(month2), "兼职",
                            String.valueOf(day2 + 1), String.valueOf(0)});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    part_time = part_time + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            // TODO 年不同月不同 奖金
            //开始月
            if (month1 == 1 || month1 == 3 || month1 == 5 || month1 == 7 || month1 == 8 || month1 == 10 || month1 == 12) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "奖金", String.valueOf(32), String.valueOf(day1 - 1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        bonus = bonus + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            } else if (month1 == 4 || month1 == 6 || month1 == 9 || month1 == 11) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "奖金", String.valueOf(31), String.valueOf(day1 - 1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        bonus = bonus + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            } else if (month1 == 2) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "奖金", String.valueOf(30), String.valueOf(day1 - 1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        bonus = bonus + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            }
            //中间月
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from income where year=? and month<? and month>? and type=?",
                    new String[]{String.valueOf(year1), String.valueOf(month2 + 1), String.valueOf(month1 - 1), "奖金"});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    bonus = bonus + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            //结束月
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from income where year=? and month=? and type=? and day<? and day>?",
                    new String[]{String.valueOf(year1), String.valueOf(month2), "奖金",
                            String.valueOf(day2 + 1), String.valueOf(0)});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    bonus = bonus + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            // TODO 年不同月不同 分红
            //开始月
            if (month1 == 1 || month1 == 3 || month1 == 5 || month1 == 7 || month1 == 8 || month1 == 10 || month1 == 12) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "分红", String.valueOf(32), String.valueOf(day1 - 1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        divide = divide + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            } else if (month1 == 4 || month1 == 6 || month1 == 9 || month1 == 11) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "分红", String.valueOf(31), String.valueOf(day1 - 1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        divide = divide + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            } else if (month1 == 2) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "分红", String.valueOf(30), String.valueOf(day1 - 1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        divide = divide + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            }
            //中间月
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from income where year=? and month<? and month>? and type=?",
                    new String[]{String.valueOf(year1), String.valueOf(month2 + 1), String.valueOf(month1 - 1),
                            "分红"});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    divide = divide + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            //结束月
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from income where year=? and month=? and type=? and day<? and day>?",
                    new String[]{String.valueOf(year1), String.valueOf(month2), "分红",
                            String.valueOf(day2 + 1), String.valueOf(0)});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    divide = divide + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            // TODO 年不同月不同 其他
            //开始月
            if (month1 == 1 || month1 == 3 || month1 == 5 || month1 == 7 || month1 == 8 || month1 == 10 || month1 == 12) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
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
                        "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
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
                        "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
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
                    "select _id,money,year,month,day,type from income where year=? and month<? and month>? and type=?",
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
                    "select _id,money,year,month,day,type from income where year=? and month=? and type=? and day<? and day>?",
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
                        "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "工资", String.valueOf(32), String.valueOf(day1-1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        salary = salary + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            } else if (month1 == 4 || month1 == 6 || month1 == 9 || month1 == 11) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "工资", String.valueOf(31), String.valueOf(day1-1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        salary = salary + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            } else if (month1 == 2) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "工资", String.valueOf(29), String.valueOf(day1-1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        salary = salary + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            }
            //TODO 开始年 中间到结束月
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from income where year=? and month<? and month>? and type=?",
                    new String[]{String.valueOf(year1), String.valueOf(13), String.valueOf(month1), "工资"});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    salary = salary + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            //TODO 中间年
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from income where year<? and year>? and type=?",
                    new String[]{String.valueOf(year2), String.valueOf(year1), "工资"});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    salary = salary + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            //TODO 结束年 结束月前面所有数据
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from income where year=? and month<?  and type=?",
                    new String[]{String.valueOf(year2), String.valueOf(month2), "工资"});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    salary = salary + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            //TODO 结束月数据
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from income where year=? and month=?  and type=? and day<?",
                    new String[]{String.valueOf(year2), String.valueOf(month2), "工资", String.valueOf(day2 + 1)});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    salary = salary + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }

            //TODO 股票
            //TODO 开始年 开始月
            if (month1 == 1 || month1 == 3 || month1 == 5 || month1 == 7 || month1 == 8 || month1 == 10 || month1 == 12) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "股票", String.valueOf(32), String.valueOf(day1-1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        stock = stock + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            } else if (month1 == 4 || month1 == 6 || month1 == 9 || month1 == 11) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "股票", String.valueOf(31), String.valueOf(day1-1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        stock = stock + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            } else if (month1 == 2) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "股票", String.valueOf(29), String.valueOf(day1-1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        stock = stock + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            }
            //TODO 开始年 中间到结束月
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from income where year=? and month<? and month>? and type=?",
                    new String[]{String.valueOf(year1), String.valueOf(13), String.valueOf(month1), "股票"});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    stock = stock + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            //TODO 中间年
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from income where year<? and year>? and type=?",
                    new String[]{String.valueOf(year2), String.valueOf(year1), "股票"});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    stock = stock + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            //TODO 结束年 结束月前面所有数据
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from income where year=? and month<?  and type=?",
                    new String[]{String.valueOf(year2), String.valueOf(month2), "股票"});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    stock = stock + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            //TODO 结束月数据
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from income where year=? and month=?  and type=? and day<?",
                    new String[]{String.valueOf(year2), String.valueOf(month2), "股票", String.valueOf(day2 + 1)});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    stock = stock + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }

            //TODO 红包
            //TODO 开始年 开始月
            if (month1 == 1 || month1 == 3 || month1 == 5 || month1 == 7 || month1 == 8 || month1 == 10 || month1 == 12) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "红包", String.valueOf(32), String.valueOf(day1-1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        red_packet = red_packet + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            } else if (month1 == 4 || month1 == 6 || month1 == 9 || month1 == 11) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "红包", String.valueOf(31), String.valueOf(day1-1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        red_packet = red_packet + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            } else if (month1 == 2) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "红包", String.valueOf(29), String.valueOf(day1-1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        red_packet = red_packet + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            }
            //TODO 开始年 中间到结束月
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from income where year=? and month<? and month>? and type=?",
                    new String[]{String.valueOf(year1), String.valueOf(13), String.valueOf(month1), "红包"});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    red_packet = red_packet + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            //TODO 中间年
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from income where year<? and year>? and type=?",
                    new String[]{String.valueOf(year2), String.valueOf(year1), "红包"});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    red_packet = red_packet + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            //TODO 结束年 结束月前面所有数据
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from income where year=? and month<?  and type=?",
                    new String[]{String.valueOf(year2), String.valueOf(month2), "红包"});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    red_packet = red_packet + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            //TODO 结束月数据
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from income where year=? and month=?  and type=? and day<?",
                    new String[]{String.valueOf(year2), String.valueOf(month2), "红包", String.valueOf(day2 + 1)});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    red_packet = red_packet + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }

            //TODO 兼职
            //TODO 开始年 开始月
            if (month1 == 1 || month1 == 3 || month1 == 5 || month1 == 7 || month1 == 8 || month1 == 10 || month1 == 12) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "兼职", String.valueOf(32), String.valueOf(day1-1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        part_time = part_time + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            } else if (month1 == 4 || month1 == 6 || month1 == 9 || month1 == 11) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "兼职", String.valueOf(31), String.valueOf(day1-1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        part_time = part_time + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            } else if (month1 == 2) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "兼职", String.valueOf(29), String.valueOf(day1-1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        part_time = part_time + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            }
            //TODO 开始年 中间到结束月
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from income where year=? and month<? and month>? and type=?",
                    new String[]{String.valueOf(year1), String.valueOf(13), String.valueOf(month1), "兼职"});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    part_time = part_time + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            //TODO 中间年
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from income where year<? and year>? and type=?",
                    new String[]{String.valueOf(year2), String.valueOf(year1), "兼职"});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    part_time = part_time + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            //TODO 结束年 结束月前面所有数据
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from income where year=? and month<?  and type=?",
                    new String[]{String.valueOf(year2), String.valueOf(month2), "兼职"});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    part_time = part_time + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            //TODO 结束月数据
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from income where year=? and month=?  and type=? and day<?",
                    new String[]{String.valueOf(year2), String.valueOf(month2), "兼职", String.valueOf(day2 + 1)});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    part_time = part_time + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }

            //TODO 奖金
            //TODO 开始年 开始月
            if (month1 == 1 || month1 == 3 || month1 == 5 || month1 == 7 || month1 == 8 || month1 == 10 || month1 == 12) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "奖金", String.valueOf(32), String.valueOf(day1-1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        bonus = bonus + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            } else if (month1 == 4 || month1 == 6 || month1 == 9 || month1 == 11) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "奖金", String.valueOf(31), String.valueOf(day1-1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        bonus = bonus + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            } else if (month1 == 2) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "奖金", String.valueOf(29), String.valueOf(day1-1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        bonus = bonus + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            }
            //TODO 开始年 中间到结束月
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from income where year=? and month<? and month>? and type=?",
                    new String[]{String.valueOf(year1), String.valueOf(13), String.valueOf(month1), "奖金"});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    bonus = bonus + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            //TODO 中间年
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from income where year<? and year>? and type=?",
                    new String[]{String.valueOf(year2), String.valueOf(year1), "奖金"});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    bonus = bonus + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            //TODO 结束年 结束月前面所有数据
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from income where year=? and month<?  and type=?",
                    new String[]{String.valueOf(year2), String.valueOf(month2), "奖金"});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    bonus = bonus + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            //TODO 结束月数据
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from income where year=? and month=?  and type=? and day<?",
                    new String[]{String.valueOf(year2), String.valueOf(month2), "奖金", String.valueOf(day2 + 1)});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    bonus = bonus + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }

            //TODO 分红
            //TODO 开始年 开始月
            if (month1 == 1 || month1 == 3 || month1 == 5 || month1 == 7 || month1 == 8 || month1 == 10 || month1 == 12) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "分红", String.valueOf(32), String.valueOf(day1-1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        divide = divide + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            } else if (month1 == 4 || month1 == 6 || month1 == 9 || month1 == 11) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "分红", String.valueOf(31), String.valueOf(day1-1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        divide = divide + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            } else if (month1 == 2) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
                                "day<? and day>?",
                        new String[]{String.valueOf(year1), String.valueOf(month1), "分红", String.valueOf(29), String.valueOf(day1-1)});
                if (cursor != null && cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        divide = divide + cursor.getDouble(cursor.getColumnIndex("money"));
                    }
                    cursor.close();
                }
            }
            //TODO 开始年 中间到结束月
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from income where year=? and month<? and month>? and type=?",
                    new String[]{String.valueOf(year1), String.valueOf(13), String.valueOf(month1), "分红"});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    divide = divide + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            //TODO 中间年
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from income where year<? and year>? and type=?",
                    new String[]{String.valueOf(year2), String.valueOf(year1), "分红"});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    divide = divide + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            //TODO 结束年 结束月前面所有数据
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from income where year=? and month<?  and type=?",
                    new String[]{String.valueOf(year2), String.valueOf(month2), "分红"});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    divide = divide + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            //TODO 结束月数据
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from income where year=? and month=?  and type=? and day<?",
                    new String[]{String.valueOf(year2), String.valueOf(month2), "分红", String.valueOf(day2 + 1)});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    divide = divide + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }

            //TODO 其他
            //TODO 开始年 开始月
            if (month1 == 1 || month1 == 3 || month1 == 5 || month1 == 7 || month1 == 8 || month1 == 10 || month1 == 12) {
                cursor = database.rawQuery(
                        "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
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
                        "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
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
                        "select _id,money,year,month,day,type from income where year=? and month=? and type=? and " +
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
                    "select _id,money,year,month,day,type from income where year=? and month<? and month>? and type=?",
                    new String[]{String.valueOf(year1), String.valueOf(13), String.valueOf(month1), "其他"});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    other = other + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            //TODO 中间年
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from income where year<? and year>? and type=?",
                    new String[]{String.valueOf(year2), String.valueOf(year1), "其他"});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    other = other + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            //TODO 结束年 结束月前面所有数据
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from income where year=? and month<?  and type=?",
                    new String[]{String.valueOf(year2), String.valueOf(month2), "其他"});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    other = other + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }
            //TODO 结束月数据
            cursor = database.rawQuery(
                    "select _id,money,year,month,day,type from income where year=? and month=?  and type=? and day<?",
                    new String[]{String.valueOf(year2), String.valueOf(month2), "其他", String.valueOf(day2 + 1)});
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    other = other + cursor.getDouble(cursor.getColumnIndex("money"));
                }
                cursor.close();
            }

        }

        if (salary != 0) {
            BaseMessage mes = new BaseMessage();
            mes.percent = (float) salary;
            mes.content = "工资";
            mes.color = Color.parseColor("#0ff000");
            mList.add(mes);
        }

        if (stock != 0) {
            BaseMessage message = new BaseMessage();
            message.percent = (float) stock;
            message.content = "股票";
            message.color = Color.parseColor("#fff000");
            mList.add(message);
        }

        if (red_packet != 0) {
            BaseMessage mes0 = new BaseMessage();
            mes0.percent = (float) red_packet;
            mes0.content = "红包";
            mes0.color = Color.parseColor("#ff00ff");
            mList.add(mes0);
        }

        if (part_time != 0) {
            BaseMessage mes1 = new BaseMessage();
            mes1.percent = (float) part_time;
            mes1.content = "兼职";
            mes1.color = Color.parseColor("#00ffff");
            mList.add(mes1);
        }
        if (bonus != 0) {
            BaseMessage mes2 = new BaseMessage();
            mes2.percent = (float) bonus;
            mes2.content = "奖金";
            mes2.color = Color.parseColor("#0099ff");
            mList.add(mes2);
        }
        if (divide != 0) {
            BaseMessage mes3 = new BaseMessage();
            mes3.percent = (float) divide;
            mes3.content = "分红";
            mes3.color = Color.parseColor("#0099dd");
            mList.add(mes3);
        }
        if (other != 0) {
            BaseMessage mes4 = new BaseMessage();
            mes4.percent = (float) other;
            mes4.content = "其他";
            mes4.color = Color.parseColor("#009933");
            mList.add(mes4);
        }
        sum = salary + stock + red_packet + part_time + bonus + divide + other;
        db.close();
        return mList;
    }


}