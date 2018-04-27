package com.example.shelterconnect.util;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by daniel on 3/4/18.
 */

public class Functions {


    public static int getUserLevel(Context context){

        Context mContext = context;
        SharedPreferences shared = mContext.getSharedPreferences("userLevel", MODE_PRIVATE);

        String userLevel = (shared.getString("position", "-1"));

        int userPosition = Integer.parseInt(userLevel);

        return userPosition;
    }
    public void menuCheck(int userLevel) {

    }

}
