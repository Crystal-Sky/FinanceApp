package com.example.finance;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;


public class About extends AppCompatActivity {
    private int themes;
    Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        themes = new SkinSettingManager(this).getCurrentSkinRes();
        this.setTheme(themes);
        setContentView(R.layout.about);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_about);
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
}
