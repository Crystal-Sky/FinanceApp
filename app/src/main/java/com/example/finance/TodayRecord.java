package com.example.finance;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.example.finance.AboutListView.ListViewAdapter;
import com.example.finance.AboutListView.TListItem;
import com.example.finance.DBA.InDB;
import com.example.finance.DBA.InObject;
import com.example.finance.DBA.OutDB;
import com.example.finance.DBA.OutObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
public class TodayRecord extends AppCompatActivity {
    //添加
    private Context mContext;
    private InDB indb;
    private OutDB outdb;
    private InObject inobj;
    private OutObject outobj;
    private SQLiteOpenHelper helper=null;
    int year;
    int month;
    int day;
    private int themes;
    Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        themes = new SkinSettingManager(this).getCurrentSkinRes();
        this.setTheme(themes);
        setContentView(R.layout.today_record);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_today);
        mToolbar.setTitle("");
        mToolbar.setTitleTextColor(Color.YELLOW);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        indb=new InDB(this);
        outdb=new OutDB(this);
        TextView balanceText=(TextView)findViewById(R.id.balance_num);
        TextView incomeText=(TextView)findViewById(R.id.income_num);
        TextView outputText=(TextView)findViewById(R.id.pay_num);
        double InSum=indb.InSum();
        double OutSum=outdb.OutSum();
        incomeText.setText(String.valueOf(InSum));
        outputText.setText(String.valueOf(OutSum));
        balanceText.setText(String.valueOf(InSum-OutSum));

        ListView listView = (ListView) findViewById(R.id.today_record_listview);
        ListViewAdapter listAdapter = new ListViewAdapter(this);
        listView.setAdapter(listAdapter);
        //Log.d("qwe", "qwe----position:value -");
        List<TListItem> List=new ArrayList<>();
        List<InObject> list=indb.queryToday();
        List<OutObject> listOut=outdb.queryToday();

        for (InObject tb_inaccount : list) {// 遍历List泛型集合
            // 将收入相关信息组合成一个字符串，存储到字符串数组的相应位置

            if("工资".equals(tb_inaccount.getType())){
                TListItem item=new TListItem(0,R.mipmap.work,tb_inaccount.getType(),tb_inaccount.getName(),
                        tb_inaccount.getNote(),tb_inaccount.getYear(),tb_inaccount.getMonth(),
                        tb_inaccount.getDay(),tb_inaccount.getMoney(),"收入");
                List.add(item);
            }
            else if("股票".equals(tb_inaccount.getType())){
                TListItem item=new TListItem(0,R.mipmap.gupiao,tb_inaccount.getType(),tb_inaccount.getName(),
                        tb_inaccount.getNote(),tb_inaccount.getYear(),tb_inaccount.getMonth(),
                        tb_inaccount.getDay(),tb_inaccount.getMoney(),"收入");
                List.add(item);

            }
            else if("红包".equals(tb_inaccount.getType())){
                TListItem item=new TListItem(0,R.mipmap.hongbao,tb_inaccount.getType(),tb_inaccount.getName(),
                        tb_inaccount.getNote(),tb_inaccount.getYear(),tb_inaccount.getMonth(),
                        tb_inaccount.getDay(),tb_inaccount.getMoney(),"收入");
                List.add(item);

            }
            else if("兼职".equals(tb_inaccount.getType())){
                TListItem item=new TListItem(0,R.mipmap.jianzhi,tb_inaccount.getType(),tb_inaccount.getName(),
                        tb_inaccount.getNote(),tb_inaccount.getYear(),tb_inaccount.getMonth(),
                        tb_inaccount.getDay(),tb_inaccount.getMoney(),"收入");
                List.add(item);

            }
            else if("奖金".equals(tb_inaccount.getType())){
                TListItem item=new TListItem(0,R.mipmap.jiangjin,tb_inaccount.getType(),tb_inaccount.getName(),
                        tb_inaccount.getNote(),tb_inaccount.getYear(),tb_inaccount.getMonth(),
                        tb_inaccount.getDay(),tb_inaccount.getMoney(),"收入");
                List.add(item);

            }
            else if("分红".equals(tb_inaccount.getType())){
                TListItem item=new TListItem(0,R.mipmap.fenhong,tb_inaccount.getType(),tb_inaccount.getName(),
                        tb_inaccount.getNote(),tb_inaccount.getYear(),tb_inaccount.getMonth(),
                        tb_inaccount.getDay(),tb_inaccount.getMoney(),"收入");
                List.add(item);

            }
            else if("其他".equals(tb_inaccount.getType())){
                TListItem item=new TListItem(0,R.mipmap.qita,tb_inaccount.getType(),tb_inaccount.getName(),
                        tb_inaccount.getNote(),tb_inaccount.getYear(),tb_inaccount.getMonth(),
                        tb_inaccount.getDay(),tb_inaccount.getMoney(),"收入");
                List.add(item);
            }
        }

        for (OutObject tb_outaccount : listOut) {// 遍历List泛型集合
            // 将收入相关信息组合成一个字符串，存储到字符串数组的相应位置

            if("衣服".equals(tb_outaccount.getType())){
                TListItem item=new TListItem(0,R.mipmap.clothes,tb_outaccount.getType(),tb_outaccount.getName(),
                        tb_outaccount.getNote(),tb_outaccount.getYear(),tb_outaccount.getMonth(),
                        tb_outaccount.getDay(),tb_outaccount.getMoney(),"支出");
                List.add(item);
            }
            else if("饮食".equals(tb_outaccount.getType())){
                TListItem item=new TListItem(0,R.mipmap.food,tb_outaccount.getType(),tb_outaccount.getName(),
                        tb_outaccount.getNote(),tb_outaccount.getYear(),tb_outaccount.getMonth(),
                        tb_outaccount.getDay(),tb_outaccount.getMoney(),"支出");
                List.add(item);

            }
            else if("住宿".equals(tb_outaccount.getType())){
                TListItem item=new TListItem(0,R.mipmap.home,tb_outaccount.getType(),tb_outaccount.getName(),
                        tb_outaccount.getNote(),tb_outaccount.getYear(),tb_outaccount.getMonth(),
                        tb_outaccount.getDay(),tb_outaccount.getMoney(),"支出");
                List.add(item);

            }
            else if("出行".equals(tb_outaccount.getType())){
                TListItem item=new TListItem(0,R.mipmap.car,tb_outaccount.getType(),tb_outaccount.getName(),
                        tb_outaccount.getNote(),tb_outaccount.getYear(),tb_outaccount.getMonth(),
                        tb_outaccount.getDay(),tb_outaccount.getMoney(),"支出");
                List.add(item);

            }
            else if("其他".equals(tb_outaccount.getType())){
                TListItem item=new TListItem(0,R.mipmap.other,tb_outaccount.getType(),tb_outaccount.getName(),
                        tb_outaccount.getNote(),tb_outaccount.getYear(),tb_outaccount.getMonth(),
                        tb_outaccount.getDay(),tb_outaccount.getMoney(),"支出");
                List.add(item);
            }
        }
        listAdapter.setList(List);
        listAdapter.notifyDataSetChanged();
    }
}
