package com.example.shawnzhu.employeehappiness;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import java.sql.Time;
import java.util.HashMap;
import java.util.Calendar;
import java.util.TimeZone;

import static com.android.volley.Request.Method.POST;
import static com.android.volley.Request.Method.GET;

/**
 * Created by shawnzhu on 2017/10/3.
 */

public class NetUtil {
    private static final String LOG_TAG = "NETUTIL";
    private static RequestQueue mQueue = null;
    private static Context mContext = null;
    private static final String baseURL = "http://155.69.149.202:3000";
    private static final String loginURL = baseURL + "/users/logIn";
    private static final String signinURL = baseURL + "/users/signIn";
    private static final String addDataURL = baseURL + "/users/addData";
    private static final String addFeedbackURL = baseURL + "/users/addFeedback";
    private static final String getUserByEmail = baseURL + "/users/getUserByEmail";

    public static void init(Context context) {
        mContext = context;
        mQueue = Volley.newRequestQueue(mContext);
    }

    private static void addRequestToQueue(Request request) {
        try {
            getQueueInstance().add(request);
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.d(LOG_TAG, "QUEUE NOT AVAILABLE");
        }
    }

    private static RequestQueue getQueueInstance() {
        if (mQueue != null) return mQueue;
        else if (mContext != null) return mQueue = Volley.newRequestQueue(mContext);
        return null;
    }


    public static void signin(String name,
                              String pswd,
                              String email,
                              String age,
                              String gender,
                              String location,
                              Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        HashMap<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("pswd", pswd);
        params.put("email", email);
        params.put("age", age);
        params.put("location", location);
        params.put("gender", gender);
        EHJSONRequest request = new EHJSONRequest(POST, signinURL, params,
                listener, errorListener);
        addRequestToQueue(request);
    }

    public static void login(String email,
                             String pswd,
                             Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        HashMap<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("pswd", pswd);
        EHJSONRequest request = new EHJSONRequest(POST, loginURL, params,
                listener, errorListener);
        addRequestToQueue(request);
    }

    public static void getUserInfo(String email,
                                   Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        HashMap<String, String> params = new HashMap<>();
        params.put("email", email);
        EHJSONRequest request = new EHJSONRequest(GET, getUserByEmail+"?email="+email, params,
                listener, errorListener);
        addRequestToQueue(request);
    }

    public static void sendFeedbackData(String HappinessLevel,
                                        String WorkingContent,
                                        String ThermalComfort,
                                        String Healthy,
                                        String Satisfactory,
                                        String EmotionType,
                                        String Conversation,
                                        String SleepHour,
                                        Response.Listener<JSONObject> listener, Response.ErrorListener errorListener){
        HashMap<String, String> params = new HashMap<>();
        SharedPreferences info_Preferences = mContext.getSharedPreferences("login_info",mContext.MODE_PRIVATE);
        String uid = info_Preferences.getString("uid","");
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String year = String.valueOf(cal.get(Calendar.YEAR));
        String month = null;
        if (cal.get(Calendar.MONTH) + 1 < 10){
            month = "0"+String.valueOf(cal.get(Calendar.MONTH) + 1);
        }
        else{
            month = String.valueOf(cal.get(Calendar.MONTH) + 1);
        }
        String day = null;
        if (cal.get(Calendar.DATE) < 10){
            day = "0" + String.valueOf(cal.get(Calendar.DATE));
        }
        else{
            day = String.valueOf(cal.get(Calendar.DATE));
        }
        String hour = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
        String min = null;
        if (cal.get(Calendar.MINUTE) < 10){
            min = "0"+String.valueOf(cal.get(Calendar.MINUTE));
        }
        else{
            min = String.valueOf(cal.get(Calendar.MINUTE));
        }
        String sec = null;
        if (cal.get(Calendar.SECOND) < 10){
            sec = "0"+ String.valueOf(cal.get(Calendar.SECOND));
        }
        else {
            sec = String.valueOf(cal.get(Calendar.SECOND));
        }

        String time = year+month+day+hour+min+sec;

        BluetoothAdapter mBluetoothAdapter = null;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        String did = mBluetoothAdapter.getAddress();
        params.put("uid",uid);
        params.put("did",did);
        params.put("time",time);
        params.put("HappinessLevel",HappinessLevel);
        params.put("WorkingContent",WorkingContent);
        params.put("ThermalComfort",ThermalComfort);
        params.put("Healthy",Healthy);
        params.put("Satisfactory",Satisfactory);
        params.put("EmotionType",EmotionType);
        params.put("Conversation",Conversation);
        params.put("SleepHour",SleepHour);
        EHJSONRequest request = new EHJSONRequest(POST, addFeedbackURL, params,
                listener, errorListener);
        addRequestToQueue(request);
    }

    public static void sendRealTimeData(Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {

        SharedPreferences info_Preferences = mContext.getSharedPreferences("login_info",mContext.MODE_PRIVATE);
        String uid = info_Preferences.getString("uid","");
        SharedPreferences data_Preferences = mContext.getSharedPreferences("realtime_data",mContext.MODE_PRIVATE);
        String AccelerationX = data_Preferences.getString("AccelerationX","");
        String AccelerationY = data_Preferences.getString("AccelerationY","");
        String AccelerationZ = data_Preferences.getString("AccelerationZ","");
        String FlightsAscended = data_Preferences.getString("FlightsAscended","");
        String FlightsDescended = data_Preferences.getString("FlightsDescended","");
        String Rate = data_Preferences.getString("Rate","");
        String SteppingGain = data_Preferences.getString("SteppingGain","");
        String SteppingLoss = data_Preferences.getString("SteppingLoss","");
        String StepsAscended = data_Preferences.getString("StepsAscended","");
        String StepsDescended = data_Preferences.getString("StepsDescended","");
        String TotalGain = data_Preferences.getString("TotalGain","");
        String TotalLoss = data_Preferences.getString("TotalLoss","");
        String Brightness = data_Preferences.getString("Brightness","");
        String AirPressure = data_Preferences.getString("AirPressure","");
        String Temperature = data_Preferences.getString("Temperature","");
        String Calories = data_Preferences.getString("Calories","");
        String CurrentMotion = data_Preferences.getString("CurrentMotion","");
        String Pace = data_Preferences.getString("Pace","");
        String Speed = data_Preferences.getString("Speed","");
        String TotalDistance = data_Preferences.getString("TotalDistance","");
        String Resistance = data_Preferences.getString("Resistance","");
        String AngularVelocityX = data_Preferences.getString("AngularVelocityX","");
        String AngularVelocityY = data_Preferences.getString("AngularVelocityY","");
        String AngularVelocityZ = data_Preferences.getString("AngularVelocityZ","");
        String HeartRate = data_Preferences.getString("HeartRate","");
        String TotalSteps = data_Preferences.getString("TotalSteps","");
        String RRInterval = data_Preferences.getString("RRInterval","");
        String SkinTemperature = data_Preferences.getString("SkinTemperature","");
        String IndexLevel = data_Preferences.getString("IndexLevel","");

        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String year = String.valueOf(cal.get(Calendar.YEAR));
        String month = null;
        if (cal.get(Calendar.MONTH) + 1 < 10){
            month = "0"+String.valueOf(cal.get(Calendar.MONTH) + 1);
        }
        else{
            month = String.valueOf(cal.get(Calendar.MONTH) + 1);
        }
        String day = null;
        if (cal.get(Calendar.DATE) < 10){
            day = "0" + String.valueOf(cal.get(Calendar.DATE));
        }
        else{
            day = String.valueOf(cal.get(Calendar.DATE));
        }
        String hour = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
        String min = null;
        if (cal.get(Calendar.MINUTE) < 10){
            min = "0"+String.valueOf(cal.get(Calendar.MINUTE));
        }
        else{
            min = String.valueOf(cal.get(Calendar.MINUTE));
        }
        String sec = null;
        if (cal.get(Calendar.SECOND) < 10){
            sec = "0"+ String.valueOf(cal.get(Calendar.SECOND));
        }
        else {
            sec = String.valueOf(cal.get(Calendar.SECOND));
        }

        String time = year+month+day+hour+min+sec;

        BluetoothAdapter mBluetoothAdapter = null;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        String did = mBluetoothAdapter.getAddress();

        HashMap<String, String> params = new HashMap<>();
        params.put("uid",uid);
        params.put("did",did);
        params.put("time",time);
        params.put("AccelerationX",AccelerationX);
        params.put("AccelerationY",AccelerationY);
        params.put("AccelerationZ",AccelerationZ);
        params.put("FlightsAscended",FlightsAscended);
        params.put("FlightsDescended",FlightsDescended);
        params.put("Rate",Rate);
        params.put("SteppingGain",SteppingGain);
        params.put("SteppingLoss",SteppingLoss);
        params.put("StepsAscended",StepsAscended);
        params.put("StepsDescended",StepsDescended);
        params.put("TotalGain",TotalGain);
        params.put("TotalLoss",TotalLoss);
        params.put("Brightness",Brightness);
        params.put("AirPressure",AirPressure);
        params.put("Temperature",Temperature);
        params.put("Calories",Calories);
        params.put("CurrentMotion",CurrentMotion);
        params.put("Pace",Pace);
        params.put("Speed",Speed);
        params.put("TotalDistance",TotalDistance);
        params.put("Resistance",Resistance);
        params.put("AngularVelocityX",AngularVelocityX);
        params.put("AngularVelocityY",AngularVelocityY);
        params.put("AngularVelocityZ",AngularVelocityZ);
        params.put("HeartRate",HeartRate);
        params.put("TotalSteps",TotalSteps);
        params.put("RRInterval",RRInterval);
        params.put("SkinTemperature",SkinTemperature);
        params.put("IndexLevel",IndexLevel);

        EHJSONRequest request = new EHJSONRequest(POST, addDataURL, params,
                listener, errorListener);
        addRequestToQueue(request);
    }


}