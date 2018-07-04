package com.example.deniskalenga.myapplication.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.deniskalenga.myapplication.R;
import com.example.deniskalenga.myapplication.adapters.BranchAdapter;
import com.example.deniskalenga.myapplication.adapters.UserAdapter;
import com.example.deniskalenga.myapplication.http.VolleySingleton;
import com.example.deniskalenga.myapplication.models.Branch;
import com.example.deniskalenga.myapplication.models.User;
import com.example.deniskalenga.myapplication.utils.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.deniskalenga.myapplication.Keys.BACKEND_URL;

public class UsersActivity extends AppCompatActivity {

    ArrayList<User>users = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    SessionManagement sessionManagement;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView=(RecyclerView)findViewById(R.id.usersRecyclerView);
        swipeRefreshLayout =(SwipeRefreshLayout)findViewById(R.id.swipeToRefresh);
        sessionManagement = new SessionManagement(getApplicationContext());
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        setSupportActionBar(toolbar);
        fetchUsers();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchUsers();
            }
        });

    }

   public void fetchUsers() {

           JsonArrayRequest request = new JsonArrayRequest(BACKEND_URL + "/users/getByBranchId/" + sessionManagement.getBranchId(), new Response.Listener<JSONArray>() {
               @Override
               public void onResponse(JSONArray response) {
                   users.clear();
                   if(swipeRefreshLayout.isRefreshing()){
                       swipeRefreshLayout.setRefreshing(false);
                   }
                   StringBuilder builder=new StringBuilder();
                   for (int i=0; i<response.length(); i++){
                       try {
                           JSONObject userObject = response.getJSONObject(i);
                           User user = new User();
                           user.setUserId(userObject.getString("id"));
                           user.setFullName(userObject.getString("fullName"));
                           user.setEmail(userObject.getString("email"));
                           user.setPhone(userObject.getString("phoneNumber"));
                           user.setUsername(userObject.getString("userName"));

                           JSONArray rolesArray = userObject.getJSONArray("roles");
                           for (int j=0; j<rolesArray.length(); j++){
                               JSONObject roleObject= rolesArray.getJSONObject(j);
                               builder.append(roleObject.getString("name"));
                               user.setRoles(builder.toString()+" ");
                           }
                           builder.setLength(0);


                           users.add(user);
                           Log.d("WATSHIPAMBA", "SUCCESS");
                       } catch (JSONException e) {
                           if(swipeRefreshLayout.isRefreshing()){
                               swipeRefreshLayout.setRefreshing(false);
                           }
                           Log.d("WATSHIPAMBA ERROR", "ERROR HAPPENED");
                           e.printStackTrace();
                       }
                   }
                   Log.d("WATSHIPAMBA","reached here");
                   UserAdapter adapter = new UserAdapter(users, getApplicationContext());
                   recyclerView.setLayoutManager(linearLayoutManager);
                   recyclerView.setAdapter(adapter);


               }
           },
                   new Response.ErrorListener() {
                       @Override
                       public void onErrorResponse(VolleyError error) {
                           if(swipeRefreshLayout.isRefreshing()){
                               swipeRefreshLayout.setRefreshing(false);
                           }
                           error.printStackTrace();
                       }
                   });

           VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);


   }

}
