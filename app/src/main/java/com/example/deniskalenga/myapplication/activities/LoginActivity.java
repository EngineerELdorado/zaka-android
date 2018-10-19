package com.example.deniskalenga.myapplication.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.auth0.android.jwt.JWT;
import com.example.deniskalenga.myapplication.R;
import com.example.deniskalenga.myapplication.http.VolleySingleton;
import com.example.deniskalenga.myapplication.models.User;
import com.example.deniskalenga.myapplication.utils.SessionManagement;
import com.example.deniskalenga.myapplication.utils.TinyDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.example.deniskalenga.myapplication.Keys.BACKEND_URL;
import static com.example.deniskalenga.myapplication.Keys.ROLES;

public class LoginActivity extends AppCompatActivity {

    EditText usernameTxt, passwordTxt;
    Button loginBtn;
    SessionManagement sessionManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        usernameTxt = (EditText)findViewById(R.id.username);
        passwordTxt = (EditText)findViewById(R.id.password);
        loginBtn= (Button)findViewById(R.id.loginBtn);
        sessionManagement = new SessionManagement(getApplicationContext());

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isInputValid()){
                    doLogin();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Please enter the login details", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void doLogin() {

        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        Map<String, String>details= new HashMap<>();
        details.put("username", usernameTxt.getText().toString());
        details.put("password", passwordTxt.getText().toString());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, BACKEND_URL + "/login", new JSONObject(details), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                //Toast.makeText(getApplicationContext(),"Login Successful", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                pDialog.dismiss();
                if (error instanceof NoConnectionError) {
                    error.fillInStackTrace();
                    error.printStackTrace();
                    new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error Occured")
                            .setContentText("No connection")
                            .show();

                }
                else if (error instanceof TimeoutError ){
                    new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error Occured")
                            .setContentText("Time out")
                            .show();
                }
                else if (error instanceof AuthFailureError) {
                    new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error Occured")
                            .setContentText("Wrong Username Or Pin")
                            .show();

                } else if (error instanceof ServerError) {
                    new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error Occured")
                            .setContentText("Server Error")
                            .show();

                } else if (error instanceof NetworkError) {
                    new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error Occured")
                            .setContentText("Network Error")
                            .show();

                } else if (error instanceof ParseError) {
                    new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error Occured")
                            .setContentText("Parse Error")
                            .show();

                }
            }


        }
        ){
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                String token = response.headers.get("authorization").substring(7);
                sessionManagement.setToken(token);
                JWT parsedJWT = new JWT(token);
                String subject = parsedJWT.getSubject();
                fetchTheUserDetails(subject);
                return null;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    public boolean isInputValid(){
        if(usernameTxt.getText().toString().equals("")|| passwordTxt.getText().toString().equals("")){
            return false;
        }
        else{
            return true;
        }
    }

    public void fetchTheUserDetails(final String userName){

        JsonObjectRequest request = new JsonObjectRequest(BACKEND_URL + "/users/username/" + userName, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                User user = new User();
                try {
                    user.setUserId(response.getString("id"));
                    user.setFullName(response.getString("fullName"));
                    user.setPhone(response.getString("phoneNumber"));
                    user.setEmail(response.getString("email"));
                    user.setUsername(response.getString("username"));
                    sessionManagement.createUserSession(user);


                    if (response.getJSONObject("business")!=null && response.getJSONObject("branch")!=null){
                        sessionManagement.setBusinessId(response.getJSONObject("business").getString("id"));
                        sessionManagement.setBusinessName(response.getJSONObject("business").getString("name"));
                        sessionManagement.setBranchId(response.getJSONObject("branch").getString("id"));
                        sessionManagement.setBranchName(response.getJSONObject("branch").getString("name"));
                    }
                    else{
                        sessionManagement.setBusinessName("Zaka");
                        sessionManagement.setBranchName("Official");
                    }
                    StringBuilder roles = new StringBuilder();
                    JSONArray rolesArray = response.getJSONArray("rolePermissions");
                    for (int i=0; i<rolesArray.length();i++){
                        JSONObject object = rolesArray.getJSONObject(i);
                        roles.append(object.getString("name")+" ");
                    }

                    Log.d("LOGGED_IN_USER", user.toString());

                    sessionManagement.setRoles(roles.toString());

                    Toast.makeText(getApplicationContext(),"Welcome "+user.getFullName(),Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                } catch (JSONException error) {
                    error.printStackTrace();


                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                if (error instanceof NoConnectionError) {
                    error.fillInStackTrace();
                    error.printStackTrace();
                    new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error Occured")
                            .setContentText("No connection")
                            .show();

                }
                else if (error instanceof TimeoutError ){
                    new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error Occured")
                            .setContentText("Time out")
                            .show();
                }
                else if (error instanceof AuthFailureError) {
                    new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error Occured")
                            .setContentText("Wrong Username Or Pin")
                            .show();

                } else if (error instanceof ServerError) {
                    new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error Occured")
                            .setContentText("Server Error")
                            .show();

                } else if (error instanceof NetworkError) {
                    new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error Occured")
                            .setContentText("Network Error")
                            .show();

                } else if (error instanceof ParseError) {
                    new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error Occured")
                            .setContentText("Parse Error")
                            .show();

                }
            }
        });
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }
}
