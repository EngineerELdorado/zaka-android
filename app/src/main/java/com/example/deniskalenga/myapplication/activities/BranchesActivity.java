package com.example.deniskalenga.myapplication.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.deniskalenga.myapplication.R;
import com.example.deniskalenga.myapplication.adapters.BranchAdapter;
import com.example.deniskalenga.myapplication.http.VolleySingleton;
import com.example.deniskalenga.myapplication.models.Branch;
import com.example.deniskalenga.myapplication.utils.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.deniskalenga.myapplication.Keys.BACKEND_URL;

public class BranchesActivity extends AppCompatActivity {

    TextView businessName;
    RecyclerView recyclerView;
    SessionManagement sessionManagement;
    ArrayList<Branch> branches = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branches);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sessionManagement=new SessionManagement(getApplicationContext());
        businessName = (TextView)findViewById(R.id.businessName);
        recyclerView = (RecyclerView)findViewById(R.id.branchesRecyclerView);
        businessName.setText(sessionManagement.getBusinessName());
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        fetchBranches();


    }

    private void fetchBranches() {

        JsonArrayRequest request = new JsonArrayRequest(BACKEND_URL + "/branches/getByBusinessId/" + sessionManagement.getBusinessId(), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                branches.clear();
                for (int i=0; i<response.length(); i++){
                    try {
                        JSONObject branchObject = response.getJSONObject(i);
                        Branch branch = new Branch();
                        branch.setBranchId(branchObject.getString("id"));
                        branch.setAddress(branchObject.getString("address"));
                        branch.setBranchName(branchObject.getString("name"));
                        branch.setMain(branchObject.getBoolean("main"));
                        branch.setPhone(branchObject.getString("phone"));
                        branches.add(branch);
                        Log.d("WATSHIPAMBA SUCCESS", branchObject.getString("name"));

                    } catch (JSONException e) {
                        Log.d("WATSHIPAMBA SUCCESS", "ERROR HAPPENED");
                        e.printStackTrace();
                    }
                }
                Log.d("WATSHIPAMBA","reached here");
                BranchAdapter adapter = new BranchAdapter(branches, getApplicationContext());
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(adapter);


            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

}
