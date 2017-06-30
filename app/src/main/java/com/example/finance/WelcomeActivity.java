package com.example.finance;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.view.Window;
import android.view.WindowManager;


public class WelcomeActivity extends Activity {
    private int themes;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        themes = new SkinSettingManager(this).getCurrentSkinRes();
        this.setTheme(themes);
        final Window window = getWindow();// 获取当前的窗体对象
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 隐藏了状态栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏了标题栏
        setContentView(R.layout.welcome);//设置页面布局
        welcomeUI();//运行线程
    }
    private void welcomeUI(){
        new Thread(new Runnable(){
            @Override
            public void run(){
                try{
                    Thread.sleep(2000);
                    Message message = new Message();
                    welHandler.sendMessage(message);
                    // 具体消息中包含什么东西并不重要，因为接收的函数不需要该参数
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
    Handler welHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            welcomeFunction();
        }
    };
    public void welcomeFunction(){
        Intent intent = new Intent();

        intent.setClass(WelcomeActivity.this, MainActivity.class);
        //运行主界面
        startActivity(intent);
        finish();
    }
}