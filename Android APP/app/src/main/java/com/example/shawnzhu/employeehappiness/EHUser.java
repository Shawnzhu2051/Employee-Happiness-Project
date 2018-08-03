package com.example.shawnzhu.employeehappiness;

import android.content.ContentValues;
import android.database.Cursor;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by shawnzhu on 2017/10/10.
 */

public class EHUser {
    private long mId;
    private String mName;
    private String mAge;
    private boolean mIsCurrentUser = false;
    private String mEmail;
    private String mPassword;
    private String mGender;
    private String mLocation;

    public EHUser() {
        super();
    }

    public EHUser(JSONObject json) throws JSONException {
        this();
        updateByJson(json);
    }

    public EHUser(String name, String pswd, String email, String age,  String gender, String location) {
        this();
        this.mName = name;
        this.mPassword = pswd;
        this.mEmail = email;
        this.mAge = age;
        this.mGender = gender;
        this.mLocation = location;
    }

    public void updateByJson(JSONObject json) throws JSONException {
        mName = json.getString("name");
        mPassword = json.getString("pswd");
        mEmail = json.getString("email");
        mAge = json.getString("age");
        mGender = json.getString("gender");
        mLocation = json.getString("location");
    }

    public void setIsCurrentUser(boolean isCurrentUser) {
        this.mIsCurrentUser = isCurrentUser;
    }

    public void setPassword(String password) {

    }

    public long getID() {
        return mId;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getPassword() {
        return mPassword;
    }

    public String getName() {
        return mName;
    }

    public String getLocation() {
        return mLocation;
    }

    public String getAge() {
        return mAge;
    }

    public String getGender() {
        return mGender;
    }

    public HashMap<String, String> getData() {
        HashMap<String, String> data = new HashMap<>();
        data.put("email", this.mEmail);
        data.put("password", this.mPassword);
        data.put("firstname", this.mName);
        data.put("lastname", this.mLocation);
        data.put("age", String.valueOf(this.mAge));
        data.put("gender", this.mGender);
        return data;
    }
}
