package me.eb.klabapp;

/*
        This class starts the whole story
 */

import android.app.Application;
import android.content.Context;

public class KlabApp extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }


    public static Context getContext(){
        return mContext;
    }
}
