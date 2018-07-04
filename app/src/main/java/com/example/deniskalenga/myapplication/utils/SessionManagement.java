package com.example.deniskalenga.myapplication.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.deniskalenga.myapplication.activities.MainActivity;
import com.example.deniskalenga.myapplication.activities.UnAuthenticatedActivity;
import com.example.deniskalenga.myapplication.models.User;

import java.util.HashMap;

import static com.example.deniskalenga.myapplication.Keys.BRANCH_ID;
import static com.example.deniskalenga.myapplication.Keys.BRANCH_NAME;
import static com.example.deniskalenga.myapplication.Keys.BUSINESS_ID;
import static com.example.deniskalenga.myapplication.Keys.BUSINESS_NAME;
import static com.example.deniskalenga.myapplication.Keys.EMAIL;
import static com.example.deniskalenga.myapplication.Keys.FULLNAME;
import static com.example.deniskalenga.myapplication.Keys.IS_LOGGEDIN;
import static com.example.deniskalenga.myapplication.Keys.JWT_TOKEN;
import static com.example.deniskalenga.myapplication.Keys.MY_PREFERENCES;
import static com.example.deniskalenga.myapplication.Keys.PHONE;
import static com.example.deniskalenga.myapplication.Keys.ROLES;
import static com.example.deniskalenga.myapplication.Keys.USERNAME;
import static com.example.deniskalenga.myapplication.Keys.USER_ID;

public class SessionManagement {
    Context context;
    SharedPreferences pref;
    SharedPreferences.Editor editor;


    public SessionManagement(Context context){
        this.context=context;
        pref = this.context.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE); // 0 - for private mode
        editor = pref.edit();
    }

    public void createUserSession(User user){

          editor.putBoolean(IS_LOGGEDIN,true);
          editor.putString(USER_ID, user.getUserId());
          editor.putString(FULLNAME, user.getFullName());
          editor.putString(PHONE, user.getPhone());
          editor.putString(EMAIL, user.getEmail());
          editor.putString(USERNAME, user.getUsername());
          editor.commit();
    }

    public void setToken(String token){
        editor.putString(JWT_TOKEN, token);
        editor.commit();
    }

    public String getToken(){
        return pref.getString(JWT_TOKEN,null);
    }

    public void setBusinessId(String businessId){
        editor.putString(BUSINESS_ID, businessId);
        editor.commit();
    }
    public String getBusinessId(){
        return pref.getString(BUSINESS_ID,null);
    }
    public void setBusinessName(String businessName){
        editor.putString(BUSINESS_NAME, businessName);
        editor.commit();
    }

    public void setBranchId(String branchId){
        editor.putString(BRANCH_ID, branchId);
        editor.commit();
    }
    public String getBranchId(){
        return pref.getString(BRANCH_ID, null);
    }

    public void setBranchName(String branchName){
        editor.putString(BRANCH_NAME, branchName);
        editor.commit();
    }

    public String getBranchName(){
        return pref.getString(BRANCH_NAME,null);
    }

    public String getBusinessName(){
        return pref.getString(BUSINESS_NAME,null);
    }


    public void setRoles(String roles){
        editor.putString(ROLES, roles);
        editor.commit();
    }

    public String getRoles(){
        return pref.getString(ROLES,null);
    }
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(USER_ID, pref.getString(USER_ID,null));
        user.put(FULLNAME, pref.getString(FULLNAME, null));
        user.put(PHONE, pref.getString(PHONE, null));
        user.put(EMAIL, pref.getString(EMAIL, null));
        user.put(USERNAME, pref.getString(USERNAME, null));
        user.put(BUSINESS_NAME, pref.getString(BUSINESS_NAME,null));
        return user;
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGGEDIN, false);
    }

    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(context, UnAuthenticatedActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        context.startActivity(i);
    }


}
