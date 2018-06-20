package com.hanschrs.latihan5_sessionloginlogout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ActivityLogin extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button btnLogin;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(getApplicationContext());

        etUsername = findViewById(R.id.editText_username);
        etPassword = findViewById(R.id.editText_password);
        btnLogin = findViewById(R.id.button_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etUsername.getText().toString().equals("") || etPassword.getText().toString().equals("")) {
                    Toast.makeText(ActivityLogin.this, "Username and Password cannot be empty.", Toast.LENGTH_SHORT).show();
                } else {
                    loginProcess();
                }
            }
        });
    }

    public void loginProcess() {
        String url = "http://hanschrs.com/sofco/login.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int statusCode = jsonObject.getInt("code");
                    String message = jsonObject.getString("message");
                    if (statusCode == 1) {
                        Toast.makeText(ActivityLogin.this, message, Toast.LENGTH_SHORT).show();

                        int userId = jsonObject.getInt("user_id");
                        String username = jsonObject.getString("username");
                        String name = jsonObject.getString("name");
                        int superiorId = jsonObject.getInt("superior_id");

                        sessionManager.createSession(userId, username, name, superiorId);
                        Intent i = new Intent(ActivityLogin.this, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ActivityLogin.this, "Database connection not established", Toast.LENGTH_SHORT).show();
                Log.e("error", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", etUsername.getText().toString());
                params.put("password", etPassword.getText().toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
