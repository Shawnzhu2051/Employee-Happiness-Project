package com.example.shawnzhu.employeehappiness;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.alibaba.fastjson.JSON;
import com.android.volley.Response;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;


/**
 * Created by shawnzhu on 2017/9/20.
 */

public class LoginActivity extends Activity{

    private static final String LOG_TAG = "LoginTest";
    public Button login;
    public Button signin;
    public Intent intent_to_main;
    public Intent intent_to_signin;
    public EditText user_input;
    public EditText pswd_input;
    public String email;
    public String pswd;
    public Context mContext;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login);
        login = (Button) findViewById(R.id.Login);
        signin = (Button) findViewById(R.id.Signin);
        user_input = (EditText)findViewById(R.id.user_input);
        pswd_input = (EditText)findViewById(R.id.pswd_input);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            login();
                        }
                    }).start();
                    Log.d(LOG_TAG,"Start Login");
                }
                catch (Exception e){
                }
                //intent_to_main = new Intent(LoginActivity.this, MainActivity.class);
                //startActivity(intent_to_main);
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent_to_signin = new Intent(LoginActivity.this, SigninActivity.class);
                startActivity(intent_to_signin);
                finish();
            }
        });

    }

    public void login(){

        email = user_input.getText().toString().trim();
        pswd = pswd_input.getText().toString().trim();
        mContext = getApplicationContext();
        NetUtil.init(mContext);

        NetUtil.login(email, pswd, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(LOG_TAG,response.toString());
                if(Objects.equals(response.getString("code"), "1060")){
                    appendToUI(response.getString("msg"));
                }
                else if(Objects.equals(response.getString("code"), "1061")) {
                    appendToUI(response.getString("msg"));
                }
                else if(Objects.equals(response.getString("code"), "1062")) {
                    appendToUI(response.getString("msg"));
                }
                else{
                    NetUtil.getUserInfo(email, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(LOG_TAG,response.toString());
                            //EHUser user = new EHUser(response);
                            SharedPreferences sharedPreferences = getSharedPreferences("login_info", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
                            editor.putString("uid",response.getString("uid"));
                            editor.putString("name",response.getString("name"));
                            editor.putString("email",response.getString("email"));
                            editor.putString("age",response.getString("age"));
                            editor.putString("gender",response.getString("gender"));
                            editor.putString("location",response.getString("location"));
                            editor.commit();
                            intent_to_main = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent_to_main);
                            finish();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    });
                }
            }
        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e("VolleyError---", volleyError.getMessage(), volleyError);
                        appendToUI("Server Error, Plesase try again later");
                        return;
                    }
                });

    }

    private void appendToUI(final String msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Toast toast = Toast.makeText(mContext, msg, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
                Log.d(LOG_TAG, msg);
                Looper.loop();
            }
        }).start();
    }
}
