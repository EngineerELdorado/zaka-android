package com.example.deniskalenga.myapplication.viewholders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.example.deniskalenga.myapplication.R;
import com.example.deniskalenga.myapplication.models.Branch;
import com.example.deniskalenga.myapplication.models.User;

public class UserViewHolder extends RecyclerView.ViewHolder {
    TextView name, etc,roles;
    Context context;
   public Switch toggleButton;
    public UserViewHolder(@NonNull View itemView, Context context) {
        super(itemView);

        name=(TextView)itemView.findViewById(R.id.name);
        etc=(TextView)itemView.findViewById(R.id.etc);
        roles=(TextView)itemView.findViewById(R.id.roles);
        this.context=context;
    }

    public void makeUI(User user){
        name.setText(user.getFullName());
        etc.setText(user.getUsername());
        roles.setText(user.getRoles().toLowerCase());


    }
}
