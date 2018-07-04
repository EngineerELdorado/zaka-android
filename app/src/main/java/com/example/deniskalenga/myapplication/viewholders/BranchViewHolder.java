package com.example.deniskalenga.myapplication.viewholders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.deniskalenga.myapplication.R;
import com.example.deniskalenga.myapplication.models.Branch;

public class BranchViewHolder extends RecyclerView.ViewHolder {
    TextView name, etc;
    Context context;
   public Switch toggleButton;
    Button viewBtn;
    public BranchViewHolder(@NonNull View itemView, Context context) {
        super(itemView);

        name=(TextView)itemView.findViewById(R.id.name);
        etc=(TextView)itemView.findViewById(R.id.etc);
        toggleButton=(Switch) itemView.findViewById(R.id.toggleButton);
        viewBtn =(Button)itemView.findViewById(R.id.viewBtn);
        this.toggleButton.setChecked(false);
        this.context=context;
    }

    public void makeUI(Branch branch){
        name.setText(branch.getBranchName());
        etc.setText(branch.getAddress());
        if(branch.isMain()){
            toggleButton.setChecked(true);

        }

    }
}
