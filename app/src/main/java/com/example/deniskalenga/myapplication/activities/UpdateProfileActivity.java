package com.example.deniskalenga.myapplication.activities;

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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.deniskalenga.myapplication.R;
import com.example.deniskalenga.myapplication.http.VolleySingleton;
import com.example.deniskalenga.myapplication.models.User;
import com.example.deniskalenga.myapplication.utils.SessionManagement;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static com.example.deniskalenga.myapplication.Keys.BACKEND_URL;
import static com.example.deniskalenga.myapplication.Keys.EMAIL;
import static com.example.deniskalenga.myapplication.Keys.FULLNAME;
import static com.example.deniskalenga.myapplication.Keys.PHONE;
import static com.example.deniskalenga.myapplication.Keys.USERNAME;
import static com.example.deniskalenga.myapplication.Keys.USER_ID;

public class UpdateProfileActivity extends AppCompatActivity {

    String userId;
    String fullName, phoneNumber,email, userName;
    EditText fullNameTxt, phoneNumberTxt, emailTxt,usernameTxt;
    SessionManagement sessionManagement;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.d("APP_EVENT","Creating");
        sessionManagement = new SessionManagement(getApplicationContext());
        userId = sessionManagement.getUserDetails().get(USER_ID);
        fullName = sessionManagement.getUserDetails().get(FULLNAME);
        phoneNumber = sessionManagement.getUserDetails().get(PHONE);
        email = sessionManagement.getUserDetails().get(EMAIL);
        userName = sessionManagement.getUserDetails().get(USERNAME);
        fullNameTxt=(EditText)findViewById(R.id.fullName);
        phoneNumberTxt=(EditText)findViewById(R.id.phoneNumber);
        emailTxt=(EditText)findViewById(R.id.email);
        usernameTxt=(EditText)findViewById(R.id.userName);
        submit = (Button)findViewById(R.id.submit);
        setLocalUserData();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validateInputs()){
                    Toast.makeText(getApplicationContext(),"Please Provide all required data", Toast.LENGTH_SHORT).show();
                }
                else{
                    updateProfile();
                }
            }
        });

    }

    private void setLocalUserData() {
        fullNameTxt.setText(fullName);
        phoneNumberTxt.setText(phoneNumber);
        emailTxt.setText(email);
        usernameTxt.setText(userName);
    }


    public void updateProfile(){
        Map<String, Object>userData=new HashMap<>();
        userData.put("fullName", fullNameTxt.getText().toString());
        userData.put("email", emailTxt.getText().toString());
        userData.put("phoneNumber", phoneNumberTxt.getText().toString());
        userData.put("userName", usernameTxt.getText().toString());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, BACKEND_URL + "/users/updateDetails/" + userId, new JSONObject(userData),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        User user = new User();
                        user.setUserId(sessionManagement.getUserDetails().get(USER_ID));
                        user.setFullName(fullNameTxt.getText().toString());
                        user.setPhone(phoneNumberTxt.getText().toString());
                        user.setEmail(emailTxt.getText().toString());
                        user.setUsername(usernameTxt.getText().toString());
                        new SessionManagement(getApplicationContext()).createUserSession(user);
                        Toast.makeText(getApplicationContext(),"Profile Updated", Toast.LENGTH_SHORT).show();
                        Log.d("UPDATED_USER", response.toString());
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
                phoneNumberTxt.getText().toString().equals("")||
                usernameTxt.getText().toString().equals("")){
            return false;
        }
        else {
            return true;
        }
    }


}
