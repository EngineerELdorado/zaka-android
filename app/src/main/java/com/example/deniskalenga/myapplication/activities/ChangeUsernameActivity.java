package com.example.deniskalenga.myapplication.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
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
import static com.example.deniskalenga.myapplication.Keys.USER_ID;

public class ChangeUsernameActivity extends AppCompatActivity {

    EditText usernameTxt, passwordTxt;
    Button submit;
    SessionManagement sessionManagement;
    TextView responseTxtV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_username);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        usernameTxt = (EditText)findViewById(R.id.username);
        passwordTxt = (EditText)findViewById(R.id.password);
        submit= (Button)findViewById(R.id.submit);
        responseTxtV = (TextView)findViewById(R.id.response);
        sessionManagement = new SessionManagement(getApplicationContext());

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isInputValid()){
                    changeUsername();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Please enter the login details", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void changeUsername() {


        JsonObjectRequest request = new JsonObjectRequest(BACKEND_URL + "/users/changeUsername/"+sessionManagement.getUserDetails().get(USER_ID)+"?username="+usernameTxt.getText().toString()+"&password="+passwordTxt.getText().toString(),
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //Toast.makeText(getApplicationContext(),"Login Successful", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                //Toast.makeText(getApplicationContext(),"Wrong username or password", Toast.LENGTH_SHORT).show();
            }


        }
        ){
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                String responseCode = response.headers.get("response_code");

                if(responseCode!=null && responseCode.equals("1")){
                    sessionManagement.logoutUser();
                }


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

}
