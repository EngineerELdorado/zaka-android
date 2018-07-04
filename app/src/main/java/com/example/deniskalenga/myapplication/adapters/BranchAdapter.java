package com.example.deniskalenga.myapplication.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.deniskalenga.myapplication.R;
import com.example.deniskalenga.myapplication.models.Branch;
import com.example.deniskalenga.myapplication.utils.SessionManagement;
import com.example.deniskalenga.myapplication.viewholders.BranchViewHolder;

import java.util.ArrayList;


public class BranchAdapter extends RecyclerView.Adapter<BranchViewHolder> {

    ArrayList<Branch>branches;
    Context context;
    SessionManagement sessionManagement;

    public BranchAdapter(ArrayList<Branch>branches,Context context){
        this.branches=branches;
        this.context=context;
    }

    @NonNull
    @Override
    public BranchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_branch,viewGroup,false);
        return new BranchViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull BranchViewHolder branchViewHolder, final int i) {

        branchViewHolder.makeUI(branches.get(i));
        branchViewHolder.toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManagement= new SessionManagement(context);
                sessionManagement.setBranchId(branches.get(i).getBranchId());
                sessionManagement.setBranchName(branches.get(i).getBranchName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return branches.size();
    }
}
