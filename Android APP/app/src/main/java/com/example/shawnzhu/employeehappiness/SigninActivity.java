package com.example.shawnzhu.employeehappiness;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.w3c.dom.Text;

/**
 * Created by shawnzhu on 2017/9/21.
 */

public class SigninActivity extends Activity {
    private static final String LOG_TAG = "SigninTest";
    public Intent intent_to_main;
    public Button confirm;
    public EditText signin_username;
    public EditText signin_pswd;
    public EditText signin_email;
    public EditText signin_age;
    public EditText signin_location;
    public RadioGroup signin_gender;
    public Context mContext;
    public String username;
    public String pswd;
    public String email;
    public String age;
    public String location;
    public String gender;
    
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.signin);
        confirm = (Button)findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            signin();
                        }
                    }).start();
                }
                catch (Exception e){
                }
            }
        });
    }

    public void signin(){

        signin_username = (EditText) findViewById(R.id.signin_username);
        signin_pswd = (EditText) findViewById(R.id.signin_pswd);
        signin_email = (EditText) findViewById(R.id.signin_email);
        signin_age = (EditText) findViewById(R.id.signin_age);
        signin_location = (EditText) findViewById(R.id.signin_location);
        signin_gender = (RadioGroup) findViewById(R.id.signin_gender);

        username = signin_username.getText().toString().trim();
        pswd = signin_pswd.getText().toString().trim();
        email = signin_email.getText().toString().trim();
        gender = Integer.toString(signin_gender.getCheckedRadioButtonId()).trim();
        age = signin_age.getText().toString().trim();
        location = signin_location.getText().toString().trim();
        mContext = getApplicationContext();

        boolean emailFlag = Validation.isEmail(email);
        boolean genderFlag = Validation.isgender(gender);
        boolean nameFlag = Validation.isname(username);
        boolean passwordFlag = Validation.ispassword(pswd);

        if(emailFlag != true){
            appendToUI("Email format error");
            return;
        }
        if(genderFlag != true){
            appendToUI("gender cannot be empty");
            return;
        }
        if(nameFlag != true){
            appendToUI("username cannot be empty");
            return;
        }
        if(passwordFlag != true){
            appendToUI("password cannot be empty");
            return;
        }

        NetUtil.init(mContext);
        NetUtil.signin(username, pswd, email, age, gender, location, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                NetUtil.getUserInfo(email, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        SharedPreferences sharedPreferences = getSharedPreferences("login_info", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
                        editor.putString("uid", response.getString("uid"));
                        editor.putString("name", response.getString("name"));
                        editor.putString("email", response.getString("email"));
                        editor.putString("age", response.getString("age"));
                        editor.putString("gender", response.getString("gender"));
                        editor.putString("location", response.getString("location"));
                        editor.commit();
                        intent_to_main = new Intent(SigninActivity.this, MainActivity.class);
                        startActivity(intent_to_main);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        appendToUI("Server Error, Plesase try again later");
                    }
                });

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                appendToUI("Server Error, Plesase try again later");
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