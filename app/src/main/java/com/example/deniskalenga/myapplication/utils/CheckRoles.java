package com.example.deniskalenga.myapplication.utils;

import android.content.Context;

import static com.example.deniskalenga.myapplication.Keys.ROLE_ADMIN;

public class CheckRoles {
    Context context;
    SessionManagement sessionManagement;
    public CheckRoles(Context context){
        this.context=context;
        sessionManagement= new SessionManagement(context);
    }

    public boolean isAdmin(){
        if(sessionManagement.getRoles().contains(ROLE_ADMIN)){
            return true;
        }
        else {
            return false;
        }
    }
}
