package com.example.deniskalenga.myapplication.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.deniskalenga.myapplication.R;
import com.example.deniskalenga.myapplication.http.VolleySingleton;
import com.example.deniskalenga.myapplication.utils.SessionManagement;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.deniskalenga.myapplication.Keys.BACKEND_URL;
import static com.example.deniskalenga.myapplication.Keys.ROLE_STAFF;

public class AddUserActivity extends AppCompatActivity {

    EditText fullNameTxt, phoneNumberTxt, emailTxt,usernameTxt;
    SessionManagement sessionManagement;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sessionManagement=new SessionManagement(getApplicationContext());
        fullNameTxt=(EditText)findViewById(R.id.fullName);
        phoneNumberTxt=(EditText)findViewById(R.id.phoneNumber);
        emailTxt=(EditText)findViewById(R.id.email);
        submit = (Button)findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validateInputs()){
                    Toast.makeText(getApplicationContext(),"Please Provide all required data", Toast.LENGTH_SHORT).show();
                }
                else{
                    addUser();
                }
            }
        });
    }

    public void addUser(){
        Map<String, Object> userData=new HashMap<>();
        userData.put("fullName", fullNameTxt.getText().toString());
        userData.put("email", emailTxt.getText().toString());
        userData.put("phoneNumber", phoneNumberTxt.getText().toString());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, BACKEND_URL + "/users/add?auto=yes&role="+ROLE_STAFF+"&businessId="+
                sessionManagement.getBusinessId()+"&branchId="+sessionManagement.getBranchId()
                , new JSONObject(userData),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        fullNameTxt.setText("");
                        emailTxt.setText("");
                        phoneNumberTxt.setText("");
                        Toast.makeText(getApplicationContext(),"User Created", Toast.LENGTH_SHORT).show();
                        Log.d("CREATED_USER", response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

    }

    public boolean validateInputs()
    {
        if(fullNameTxt.getText().toString().equals("")||
                emailTxt.getText().toString().equals("")||
                phoneNumberTxt.getText().toString().equals("")){
            return false;
        }
        else {
            return true;
        }
    }

}
