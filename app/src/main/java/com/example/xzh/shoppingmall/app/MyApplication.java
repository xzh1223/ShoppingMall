package com.example.xzh.shoppingmall.app;

import android.app.Application;

import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by xzh on 2017/10/12.
 */

public class MyApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        initOkHttpClient();

    }

    /**
     *  初始化OkHttpUtils
     */
    private void initOkHttpClient() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .build();
        OkHttpUtils.initClient(okHttpClient);

    }


}
