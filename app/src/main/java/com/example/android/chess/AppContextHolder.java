package com.example.android.chess;

import android.content.Context;

public class AppContextHolder {

    private static Context c;

    public static Context getC() {
        return c;
    }

    static void setC(Context c) {
        AppContextHolder.c = c;
    }
}
