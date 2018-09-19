package com.liuh.learn.andserver;

import android.app.Application;
import android.content.Context;

/**
 * Date: 2018/9/19 14:43
 * Description:
 */
public class BaseApp extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Context getContext() {
        return context;
    }
}
