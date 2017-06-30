package com.example.finance;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.SystemClock;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.GridLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.finance.FourModule.ChartAnalysis;
import com.example.finance.FourModule.FinancialInformation;
import com.example.finance.FourModule.HomeAccounting;
import com.example.finance.FourModule.StockMarket;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.Calendar;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    RadioGroup Tabs;//底部栏按钮组
    GridLayout main_content;//中间页面布局
    Toolbar toolbar;//顶层栏
    TextView toolbar_title;//顶层栏标题
    int NAV_ABOUT=3;
    private long selectTime;
    private  long systemTime;
    private long firstTime;
    private Dialog dialog;
    private java.util.Calendar c;
    private int themes;
    boolean OK;
    HomeAccounting home;
    ChartAnalysis chart;
    FinancialInformation manage;
    StockMarket stock;
    int x=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemBarTintManager tintManager=new SystemBarTintManager(this);
        tintManager.setStatusBarTintResource(R.color.colorPrimaryDark);
        tintManager.setStatusBarTintEnabled(true);
        themes = new SkinSettingManager(this).getCurrentSkinRes();
        this.setTheme(themes);
        setContentView(R.layout.activity_main);//设置主程序界面
        if(savedInstanceState==null){
            getFragmentManager().beginTransaction().add(R.id.main_content,new HomeAccounting()).commitAllowingStateLoss();
        }
        Tabs=(RadioGroup)findViewById(R.id.tab_menu);//获取按钮组
        toolbar_title=(TextView)findViewById(R.id.toolbar_title);//获取顶层栏标题
        toolbar = (Toolbar) findViewById(R.id.toolbar_main);//获取顶层栏
        toolbar.setTitle("");//设置标题为空
        home=new HomeAccounting();
        chart=new ChartAnalysis();
        manage=new FinancialInformation();
        //stock=new StockMarket();
        main_content=(GridLayout)findViewById(R.id.main_content);//获取中间页面
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toolbar.setNavigationIcon(R.mipmap.person);//更改主界面的左上角的图标
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //获取侧拉框
        navigationView.setNavigationItemSelectedListener(this);//为侧拉框添加Item选择监听事件
        //为底部栏按钮组设置选择改变监听事件
        Tabs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.home_id:
                        //main_content.removeAllViews();

                        toolbar_title.setText(R.string.home_name);
                        //HomeAccounting home=new HomeAccounting();
                        FragmentTransaction home_ft=getFragmentManager().beginTransaction();
                        home_ft.replace(R.id.main_content,home);
                        home_ft.commit();
                        break;
                    case R.id.chart_id:
                        //main_content.removeAllViews();
                        toolbar_title.setText(R.string.chart_name);
                        //ChartAnalysis chart=new ChartAnalysis();
                        FragmentTransaction chart_ft=getFragmentManager().beginTransaction();
                        chart_ft.replace(R.id.main_content,chart);
                        chart_ft.commit();
                        break;
                    case R.id.manage_id:
                        //main_content.removeAllViews();
                        toolbar_title.setText(R.string.manage_name);
                        //FinancialInformation manage=new FinancialInformation();
                        FragmentTransaction manage_ft=getFragmentManager().beginTransaction();
                        manage_ft.replace(R.id.main_content,manage);
                        manage_ft.commit();
                        break;
                    case R.id.stock_id:
                        //main_content.removeAllViews();
                        toolbar_title.setText(R.string.stock_name);
                        stock=new StockMarket();
                        FragmentTransaction stock_ft=getFragmentManager().beginTransaction();
                        stock_ft.replace(R.id.main_content,stock);
                        stock_ft.commit();
                        break;
                    default:
                        break;
                }
            }
        });
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        int id=item.getItemId();
        switch (id)
        {
            case R.id.nav_gallery:
                themes = new SkinSettingManager(this).toggleSkins();
                this.setTheme(themes);
                //setContentView(R.layout.activity_main);
                recreate();
                break;//设置背景
            case R.id.nav_slideshow:
                setReminder(true);
                Intent intent = new Intent(MainActivity.this, LongRunningService.class);
                //开启关闭Service
                intent.putExtra("time",firstTime);
                startService(intent);

                //设置一个Toast来提醒使用者提醒的功能已经开始
              //  Toast.makeText(MainActivity.this,"提醒的功能已经开启",Toast.LENGTH_LONG).show();
                break;//时间提示
            case R.id.nav_manage:
                startActivityForResult(new Intent(this,
                        About.class),NAV_ABOUT);
                break;//设置

        }
        return true;
    }

    private void setReminder(boolean b) {

        // AlarmManager am= (AlarmManager) getSystemService(ALARM_SERVICE);
        if(b){
            firstTime = SystemClock.elapsedRealtime();	// 开机之后到现在的运行时间(包括睡眠时间)
            systemTime = System.currentTimeMillis();
            c= Calendar.getInstance();
            c.setTimeInMillis(System.currentTimeMillis());
// 这里时区需要设置一下，不然会有8个小时的时间差
            c.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            int hour=c.get(Calendar.HOUR_OF_DAY);
            int minute=c.get(Calendar.MINUTE);

            dialog=new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int i, int i1) {
                    c.set(Calendar.MINUTE, i);
                    c.set(Calendar.HOUR_OF_DAY,i1);
                    c.set(Calendar.SECOND, 0);
                    c.set(Calendar.MILLISECOND, 0);
// 选择的定时时间
                    selectTime = c.getTimeInMillis();
// 如果当前时间大于设置的时间，那么就从第二天的设定时间开始
                    if(systemTime > selectTime) {
                     //   Toast.makeText(MainActivity.this,"设置的时间小于当前时间", Toast.LENGTH_SHORT).show();
                        c.add(Calendar.DAY_OF_MONTH, 1);
                        selectTime = c.getTimeInMillis();
                    }
// 计算现在时间到设定时间的时间差
                    long time = selectTime - systemTime;
                    firstTime += time;


                }
            },hour,minute,true);
            dialog.show();



        }

    }

}
