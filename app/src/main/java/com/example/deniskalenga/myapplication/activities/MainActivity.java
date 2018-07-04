package com.example.deniskalenga.myapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.deniskalenga.myapplication.R;
import com.example.deniskalenga.myapplication.adapters.BranchAdapter;
import com.example.deniskalenga.myapplication.http.VolleySingleton;
import com.example.deniskalenga.myapplication.models.Branch;
import com.example.deniskalenga.myapplication.utils.CheckRoles;
import com.example.deniskalenga.myapplication.utils.SessionManagement;
import com.example.deniskalenga.myapplication.utils.TinyDB;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.deniskalenga.myapplication.Keys.BACKEND_URL;
import static com.example.deniskalenga.myapplication.Keys.FULLNAME;
import static com.example.deniskalenga.myapplication.Keys.PHONE;
import static com.example.deniskalenga.myapplication.Keys.ROLE_ADMIN;
import static com.example.deniskalenga.myapplication.Keys.USERNAME;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SessionManagement sessionManagement;
    View header;
    TextView userNameTxtV;
    TextView businessNameTxtV;
    ArrayList<String>roles;
    TinyDB tinyDB;
    TextView userBusinessName;
    TextView userBranchname;
    RecyclerView recyclerView;
    ArrayList<Branch>branches = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    Menu menu;
    MenuItem hrMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sessionManagement = new SessionManagement(getApplicationContext());
        ckeckIfLoggedIn();
        tinyDB= new TinyDB(getApplicationContext());
        userBusinessName = (TextView)findViewById(R.id.businessName);
        recyclerView=(RecyclerView)findViewById(R.id.branchesRecyclerView);
        userBusinessName.setText(sessionManagement.getBusinessName());
        userBranchname=(TextView)findViewById(R.id.branchName);
        userBranchname.setText(sessionManagement.getBranchName());
        Log.d("ROLES", sessionManagement.getRoles());
        if(new CheckRoles(getApplicationContext()).isAdmin()){
            userBranchname.setVisibility(View.GONE);
        }
        if(!new CheckRoles(getApplicationContext()).isAdmin()){
            recyclerView.setVisibility(View.GONE);
        }
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());

        fetchBranches();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        header = navigationView.getHeaderView(0);
        userNameTxtV =(TextView)header.findViewById(R.id.userName);
        businessNameTxtV=(TextView)header.findViewById(R.id.businessName);
        setLocalUserData();
        userNameTxtV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, UpdateProfileActivity.class));
            }
        });

        menu = navigationView.getMenu();
        hrMenu= menu.findItem(R.id.hr);

        if(!new CheckRoles(getApplicationContext()).isAdmin()){
            hrMenu.setVisible(false);
        }


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

    private void ckeckIfLoggedIn() {
        if (!sessionManagement.isLoggedIn()){
            Intent intent = new Intent(MainActivity.this, UnAuthenticatedActivity.class);
            finish();
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_users) {
            startActivity(new Intent(MainActivity.this, UsersActivity.class));
        } else if (id == R.id.nav_add_user) {
            startActivity(new Intent(MainActivity.this, AddUserActivity.class));
        }else if (id == R.id.nav_myProfile) {
            startActivity(new Intent(MainActivity.this, UpdateProfileActivity.class));

        } else if (id == R.id.changeUsername) {
            startActivity(new Intent(MainActivity.this, ChangeUsernameActivity.class));

        } else if (id == R.id.changePassword) {
            startActivity(new Intent(MainActivity.this, ChangePasswordActivity.class));

        } else if (id == R.id.logout) {
            sessionManagement.logoutUser();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setLocalUserData() {
        userNameTxtV.setText(sessionManagement.getUserDetails().get(FULLNAME)+" ("+
                sessionManagement.getUserDetails().get(USERNAME)+")\n"+sessionManagement.getUserDetails().get(PHONE));
        businessNameTxtV.setText(sessionManagement.getBusinessName() + " ("+((sessionManagement.getBranchName()==null)?"No Branch":sessionManagement.getBranchName())+")");
    }

    @Override
    protected void onResume() {
        super.onResume();
        setLocalUserData();
        Log.d("APP_EVENT","Resuming");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setLocalUserData();
        Log.d("APP_EVENT","Restarting");
    }



}
