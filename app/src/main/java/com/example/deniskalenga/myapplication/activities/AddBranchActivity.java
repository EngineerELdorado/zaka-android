package com.example.deniskalenga.myapplication.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.deniskalenga.myapplication.R;
import com.example.deniskalenga.myapplication.http.VolleySingleton;
import com.example.deniskalenga.myapplication.utils.SessionManagement;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.example.deniskalenga.myapplication.Keys.BACKEND_URL;

public class AddBranchActivity extends AppCompatActivity {

    EditText name, email, phone, address;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_branch);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name = findViewById(R.id.branchName);
        email = findViewById(R.id.branchEmail);
        phone = findViewById(R.id.branchPhone);
        address = findViewById(R.id.branchAddress);

        submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFields()){
                    postBranch();
                }else {
                    Toast.makeText(getApplicationContext(), "Please provide all required info", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public boolean validateFields(){
        if (!name.getText().toString().equals("")
                && !email.getText().toString().equals("")
                && !phone.getText().toString().equals("")
                && !address.getText().toString().equals("")){
            return true;
        }
        else{
            return false;
        }
    }

    public void postBranch(){

        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        JSONObject object;
        Map<String, String>data = new HashMap<>();
        data.put("name", name.getText().toString());
        data.put("address", address.getText().toString());
        data.put("phone", phone.getText().toString());
        data.put("address", address.getText().toString());
        object = new JSONObject(data);
        Long businessId = Long.valueOf(new SessionManagement(getApplicationContext()).getBusinessId());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, BACKEND_URL+"/branches/add?businessId="+businessId+"&isMain=no", object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pDialog.dismiss();
                try {
                    new SweetAlertDialog(AddBranchActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Good job!")
                            .setContentText(response.getString("responseMessage"))
                            .show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                name.setText("");
                phone.setText("");
                email.setText("");
                address.setText("");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    new SweetAlertDialog(AddBranchActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error Occured")
                            .setContentText("Time Out Error Or No Connection")
                            .show();

                } else if (error instanceof AuthFailureError) {
                    new SweetAlertDialog(AddBranchActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error Occured")
                            .setContentText("You don't have the rights to perform this operation. " +
                                    "\n Please contact the Administrator")
                            .show();

                } else if (error instanceof ServerError) {
                    new SweetAlertDialog(AddBranchActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error Occured")
                            .setContentText("Server Error")
                            .show();

                } else if (error instanceof NetworkError) {
                    new SweetAlertDialog(AddBranchActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error Occured")
                            .setContentText("Network Error")
                            .show();

                } else if (error instanceof ParseError) {
                    new SweetAlertDialog(AddBranchActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error Occured")
                            .setContentText("Parse Error")
                            .show();

                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String>headers= new HashMap<>();
                headers.put("Authorization", "Bearer "+new SessionManagement(getApplicationContext()).getToken());
                return headers;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

}
