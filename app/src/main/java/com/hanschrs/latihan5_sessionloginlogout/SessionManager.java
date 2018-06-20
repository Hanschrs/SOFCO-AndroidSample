package com.hanschrs.latihan5_sessionloginlogout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    int mode = 0;

    private static final String pref_name = "loginPref";
    private static final String is_login = "isLogin";
    public static final String user_id = "userID";
    public static final String username = "username";
    public static final String nama = "nama";
    public static final String superior_id = "superiorID";


    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(pref_name, mode);
        editor = pref.edit();
    }

    public void createSession(int userIDParam, String usernameParam, String namaParam, int superiorIDParam) {
        editor.putBoolean(is_login, true);
        editor.putInt(user_id, userIDParam);
        editor.putString(username, usernameParam);
        editor.putString(nama, namaParam);
        editor.putInt(superior_id, superiorIDParam);
        editor.commit();
    }

    public void checkLogin(){
        if (!this.is_login()){
            Intent i = new Intent(context, ActivityLogin.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }else {
            Intent i = new Intent(context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }

    private boolean is_login(){
        return pref.getBoolean(is_login, false);
    }

    public void logout(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, ActivityLogin.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(pref_name, pref.getString(pref_name, null));
        user.put(user_id, String.valueOf(pref.getInt(user_id, 0)));
        user.put(username, pref.getString(username, null));
        user.put(nama, pref.getString(nama, null));
        user.put(superior_id, String.valueOf(pref.getInt(superior_id, 0)));
        return user;
    }
}
