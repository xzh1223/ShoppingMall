package com.example.xzh.shoppingmall.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.xzh.shoppingmall.R;

/**
 *  启动页面
 */
public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // 2s 进入主界面
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 执行主线程，启动主界面
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                // 关闭当前页面
                finish();
            }
        },2000);

    }
}
