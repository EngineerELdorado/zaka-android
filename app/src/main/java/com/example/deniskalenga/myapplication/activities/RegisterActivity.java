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

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.deniskalenga.myapplication.R;
import com.example.deniskalenga.myapplication.http.VolleySingleton;
import com.example.deniskalenga.myapplication.models.Business;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

//import cn.pedant.SweetAlert.SweetAlertDialog;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.example.deniskalenga.myapplication.Keys.BACKEND_URL;

public class RegisterActivity extends AppCompatActivity {

    EditText businessNameTxt, adminFullnameTxt, adminEmailTxt, adminPhoneTxt, adminPasswordTxt;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        businessNameTxt =findViewById(R.id.businessName);
        adminEmailTxt = findViewById(R.id.email);
        adminFullnameTxt=findViewById(R.id.fullName);
        adminPhoneTxt = findViewById(R.id.phoneNumber);
        adminPasswordTxt= findViewById(R.id.pin);
        submit = findViewById(R.id.submit);

       submit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               registerBusiness();
           }
       });
    }


    public void registerBusiness(){
        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        JSONObject object;
        Map<String, String> map = new HashMap<>();
        map.put("name",businessNameTxt.getText().toString());
        map.put("adminEmail",adminEmailTxt.getText().toString());
        map.put("adminFullName",adminFullnameTxt.getText().toString());
        map.put("adminPhoneNumber",adminPhoneTxt.getText().toString());
        map.put("adminPassword",adminPasswordTxt.getText().toString());

        object = new JSONObject(map);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, BACKEND_URL+"/businesses/register", object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
               pDialog.dismiss();
                new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Good job!")
                        .setContentText("Your business has been created")
                        .setConfirmText("Login")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                finish();
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            }
                        })
                        .show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();

                error.printStackTrace();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error Occured")
                            .setContentText("Time Out Error Or No Connection")
                            .show();

                } else if (error instanceof AuthFailureError) {
                    new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error Occured")
                            .setContentText("You don't have the rights to perform this operation. " +
                                    "\n Please contact the Administrator")
                            .show();

                } else if (error instanceof ServerError) {
                    new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error Occured")
                            .setContentText("Server Error")
                            .show();

                } else if (error instanceof NetworkError) {
                    new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error Occured")
                            .setContentText("Network Error")
                            .show();

                } else if (error instanceof ParseError) {
                    new SweetAlertDialog(RegisterActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error Occured")
                            .setContentText("Parse Error")
                            .show();

                }
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

}
