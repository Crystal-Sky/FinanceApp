package com.example.finance;

import android.support.v4.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.finance.AboutListView.OnDismissListener;
import com.example.finance.AboutListView.OnItemClickListener;

import java.util.ArrayList;


public class NewRecord extends  AppCompatActivity implements View.OnClickListener {
    Toolbar mToolbar;
    ViewPager viewpager;
    InputRecord f2=null;
    OutputRecord f1=null;
    RadioButton[] btnArray;
    MainFragmentPagerAdapter adapter;
    private int currentPageIndex = 0;
    private ArrayList<android.support.v4.app.Fragment> datas;
    private int themes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        themes = new SkinSettingManager(this).getCurrentSkinRes();
        this.setTheme(themes);
        setContentView(R.layout.newrecord_main);
        viewpager = (ViewPager)this.findViewById(R.id.viewPager);
        adapter = new MainFragmentPagerAdapter(this.getSupportFragmentManager(), null);
        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(currentPageIndex);
        datas = new ArrayList<android.support.v4.app.Fragment>();
        datas.add(f1 = new OutputRecord());
        datas.add(f2 = new InputRecord());
        adapter.setDatas(datas);
        adapter.notifyDataSetChanged();
        btnArray=new RadioButton[]{
                (RadioButton)findViewById(R.id.output_button),
                (RadioButton)findViewById(R.id.input_button),
        };
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int index) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int pageIndex) {
                currentPageIndex = pageIndex;
                for (int i = 0; i < btnArray.length; i++) {
                    if (i == currentPageIndex) {
                        btnArray[i].setTextColor(Color.WHITE);

                    } else {
                        btnArray[i].setTextColor(Color.BLACK);
                    }

                }
            }
        });

        for (RadioButton btn : btnArray) {
            btn.setOnClickListener(this);
        }
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
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
    }
    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.output_button:
                    currentPageIndex = 0;
                    break;
                case R.id.input_button:
                    currentPageIndex = 1;
                    break;
            }
            viewpager.setCurrentItem(currentPageIndex);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}