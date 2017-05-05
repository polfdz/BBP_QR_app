package com.bbparty.eroc.bbp_qr;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Pol on 01/11/2016.
 */
public class SharedPreferencesHelper {

    SharedPreferences preferences;
    public String getString(Context context, String key){
        preferences = context.getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        String result = preferences.getString(key, null);

        return result;
    }

    public void putString(Context context, String key, String value){
        preferences = context.getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

}
