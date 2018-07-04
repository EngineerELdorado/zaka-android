package com.example.deniskalenga.myapplication.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.deniskalenga.myapplication.R;
import com.example.deniskalenga.myapplication.models.Branch;
import com.example.deniskalenga.myapplication.models.User;
import com.example.deniskalenga.myapplication.utils.SessionManagement;
import com.example.deniskalenga.myapplication.viewholders.BranchViewHolder;
import com.example.deniskalenga.myapplication.viewholders.UserViewHolder;

import java.util.ArrayList;


public class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {

    ArrayList<User> users;
    Context context;

    public UserAdapter(ArrayList<User>users, Context context){
        this.users =users;
        this.context=context;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_user,viewGroup,false);
        return new UserViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder userViewHolder, final int i) {

        userViewHolder.makeUI(users.get(i));

    }

    @Override
    public int getItemCount() {
        Log.d("USERS+FETCHED", String.valueOf(users.size()));
        return users.size();
    }
}
