package com.example.shawnzhu.employeehappiness;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.dpizarro.uipicker.library.picker.PickerUI;
import com.dpizarro.uipicker.library.picker.PickerUISettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


import com.microsoft.band.BandClient;
import com.microsoft.band.BandClientManager;
import com.microsoft.band.BandException;
import com.microsoft.band.BandInfo;
import com.microsoft.band.BandIOException;
import com.microsoft.band.ConnectionState;

import com.microsoft.band.sensors.BandAccelerometerEvent;
import com.microsoft.band.sensors.BandAccelerometerEventListener;
import com.microsoft.band.sensors.BandAltimeterEvent;
import com.microsoft.band.sensors.BandAltimeterEventListener;
import com.microsoft.band.sensors.SampleRate;

import org.jsoup.nodes.Entities;

/**
 * Created by shawnzhu on 2017/9/19.
 */

public class QuestionActivity extends Activity {
    public Context mContext;
    public Button submit_button;
    public ImageView back;
    public TextView question_btn1;
    public TextView answer1;
    public PickerUI picker1;
    public TextView question_btn2;
    public TextView answer2;
    public PickerUI picker2;
    public TextView question_btn3;
    public TextView answer3;
    public PickerUI picker3;
    public TextView question_btn4;
    public TextView answer4;
    public PickerUI picker4;
    public TextView question_btn5;
    public TextView answer5;
    public PickerUI picker5;
    public TextView question_btn6;
    public TextView answer6;
    public PickerUI picker6;
    public TextView question_btn7;
    public TextView answer7;
    public PickerUI picker7;
    public TextView question_btn8;
    public TextView answer8;
    public PickerUI picker8;

    String HappinessLevel = null;
    String WorkingContent = null;
    String ThermalComfort = null;
    String Healthy = null;
    String Satisfactory = null;
    String EmotionType = null;
    String Conversation = null;
    String SleepHour = null;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.question);
        btn_init();
        submit_button = (Button) findViewById(R.id.submit);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        send_data();
                    }
                });
                SharedPreferences feedback_Preferences = getSharedPreferences("last_feedback_data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = feedback_Preferences.edit();//获取编辑器
                editor.putString("HappinessLevel",String.format("%s", HappinessLevel));
                editor.putString("WorkingContent",String.format("%s", WorkingContent));
                editor.putString("ThermalComfort",String.format("%s", ThermalComfort));
                editor.putString("Healthy",String.format("%s", Healthy));
                editor.putString("Satisfactory",String.format("%s", Satisfactory));
                editor.putString("EmotionType",String.format("%s", EmotionType));
                editor.putString("Conversation",String.format("%s", Conversation));
                editor.putString("SleepHour",String.format("%s", SleepHour));
                editor.commit();
            }
        });
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void btn_init(){
        final SharedPreferences feedback_Preferences = getSharedPreferences("last_feedback_data", Context.MODE_PRIVATE);
        HappinessLevel = feedback_Preferences.getString("HappinessLevel","");
        WorkingContent = feedback_Preferences.getString("WorkingContent","");
        ThermalComfort = feedback_Preferences.getString("ThermalComfort","");
        Healthy = feedback_Preferences.getString("Healthy","");
        Satisfactory = feedback_Preferences.getString("Satisfactory","");
        EmotionType = feedback_Preferences.getString("EmotionType","");
        Conversation = feedback_Preferences.getString("Conversation","");
        SleepHour = feedback_Preferences.getString("SleepHour","");

        question_btn1 = (TextView) findViewById(R.id.question1);
        answer1 = (TextView)findViewById(R.id.answer1);
        answer1.setText(HappinessLevel);
        picker1 = (PickerUI)findViewById(R.id.picker1);
        List<String> options1 = Arrays.asList(getResources().getStringArray(R.array.question_option1));
        PickerUISettings pickerUISettings1 = new PickerUISettings.Builder()
                .withItems(options1)
                .withAutoDismiss(true)
                .withItemsClickables(true)
                .withUseBlur(false)
                .build();
        picker1.setSettings(pickerUISettings1);
        question_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker1.slide();
            }
        });
        picker1.setOnClickItemPickerUIListener(new PickerUI.PickerUIItemClickListener() {
            @Override
            public void onItemClickPickerUI(int which, int position, String valueResult) {
                answer1.setText(valueResult);
            }
        });

        question_btn2 = (TextView) findViewById(R.id.question2);
        answer2 = (TextView)findViewById(R.id.answer2);
        answer2.setText(WorkingContent);
        picker2 = (PickerUI)findViewById(R.id.picker2);
        List<String> options2 = Arrays.asList(getResources().getStringArray(R.array.question_option2));
        PickerUISettings pickerUISettings2 = new PickerUISettings.Builder()
                .withItems(options2)
                .withAutoDismiss(true)
                .withItemsClickables(true)
                .withUseBlur(false)
                .build();
        picker2.setSettings(pickerUISettings2);
        question_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker2.slide();
            }
        });
        picker2.setOnClickItemPickerUIListener(new PickerUI.PickerUIItemClickListener() {
            @Override
            public void onItemClickPickerUI(int which, int position, String valueResult) {
                answer2.setText(valueResult);
            }
        });

        question_btn3 = (TextView) findViewById(R.id.question3);
        answer3 = (TextView)findViewById(R.id.answer3);
        answer3.setText(ThermalComfort);
        picker3 = (PickerUI)findViewById(R.id.picker3);
        List<String> options3 = Arrays.asList(getResources().getStringArray(R.array.question_option3));
        PickerUISettings pickerUISettings3 = new PickerUISettings.Builder()
                .withItems(options3)
                .withAutoDismiss(true)
                .withItemsClickables(true)
                .withUseBlur(false)
                .build();
        picker3.setSettings(pickerUISettings3);
        question_btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker3.slide();
            }
        });
        picker3.setOnClickItemPickerUIListener(new PickerUI.PickerUIItemClickListener() {
            @Override
            public void onItemClickPickerUI(int which, int position, String valueResult) {
                answer3.setText(valueResult);
            }
        });

        question_btn4 = (TextView) findViewById(R.id.question4);
        answer4 = (TextView)findViewById(R.id.answer4);
        answer4.setText(Healthy);
        picker4 = (PickerUI)findViewById(R.id.picker4);
        List<String> options4 = Arrays.asList(getResources().getStringArray(R.array.question_option4));
        PickerUISettings pickerUISettings4 = new PickerUISettings.Builder()
                .withItems(options4)
                .withAutoDismiss(true)
                .withItemsClickables(true)
                .withUseBlur(false)
                .build();
        picker4.setSettings(pickerUISettings4);
        question_btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker4.slide();
            }
        });
        picker4.setOnClickItemPickerUIListener(new PickerUI.PickerUIItemClickListener() {
            @Override
            public void onItemClickPickerUI(int which, int position, String valueResult) {
                answer4.setText(valueResult);
            }
        });

        question_btn5 = (TextView) findViewById(R.id.question5);
        answer5 = (TextView)findViewById(R.id.answer5);
        answer5.setText(Satisfactory);
        picker5 = (PickerUI)findViewById(R.id.picker5);
        List<String> options5 = Arrays.asList(getResources().getStringArray(R.array.question_option5));
        PickerUISettings pickerUISettings5 = new PickerUISettings.Builder()
                .withItems(options5)
                .withAutoDismiss(true)
                .withItemsClickables(true)
                .withUseBlur(false)
                .build();
        picker5.setSettings(pickerUISettings5);
        question_btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker5.slide();
            }
        });
        picker5.setOnClickItemPickerUIListener(new PickerUI.PickerUIItemClickListener() {
            @Override
            public void onItemClickPickerUI(int which, int position, String valueResult) {
                answer5.setText(valueResult);
            }
        });

        question_btn6 = (TextView) findViewById(R.id.question6);
        answer6 = (TextView)findViewById(R.id.answer6);
        answer6.setText(EmotionType);
        picker6 = (PickerUI)findViewById(R.id.picker6);
        List<String> options6 = Arrays.asList(getResources().getStringArray(R.array.question_option6));
        PickerUISettings pickerUISettings6 = new PickerUISettings.Builder()
                .withItems(options6)
                .withAutoDismiss(true)
                .withItemsClickables(true)
                .withUseBlur(false)
                .build();
        picker6.setSettings(pickerUISettings6);
        question_btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker6.slide();
            }
        });
        picker6.setOnClickItemPickerUIListener(new PickerUI.PickerUIItemClickListener() {
            @Override
            public void onItemClickPickerUI(int which, int position, String valueResult) {
                answer6.setText(valueResult);
            }
        });

        question_btn7 = (TextView) findViewById(R.id.question7);
        answer7 = (TextView)findViewById(R.id.answer7);
        answer7.setText(Conversation);
        picker7 = (PickerUI)findViewById(R.id.picker7);
        List<String> options7 = Arrays.asList(getResources().getStringArray(R.array.question_option7));
        PickerUISettings pickerUISettings7 = new PickerUISettings.Builder()
                .withItems(options7)
                .withAutoDismiss(true)
                .withItemsClickables(true)
                .withUseBlur(false)
                .build();
        picker7.setSettings(pickerUISettings7);
        question_btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker7.slide();
            }
        });
        picker7.setOnClickItemPickerUIListener(new PickerUI.PickerUIItemClickListener() {
            @Override
            public void onItemClickPickerUI(int which, int position, String valueResult) {
                answer7.setText(valueResult);
            }
        });

        question_btn8 = (TextView) findViewById(R.id.question8);
        answer8 = (TextView)findViewById(R.id.answer8);
        answer8.setText(SleepHour);
        picker8 = (PickerUI)findViewById(R.id.picker8);
        List<String> options8 = Arrays.asList(getResources().getStringArray(R.array.question_option8));
        PickerUISettings pickerUISettings8 = new PickerUISettings.Builder()
                .withItems(options8)
                .withAutoDismiss(true)
                .withItemsClickables(true)
                .withUseBlur(false)
                .build();
        picker8.setSettings(pickerUISettings8);
        question_btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker8.slide();
            }
        });
        picker8.setOnClickItemPickerUIListener(new PickerUI.PickerUIItemClickListener() {
            @Override
            public void onItemClickPickerUI(int which, int position, String valueResult) {
                answer8.setText(valueResult);
            }
        });
    }

    public void send_data(){
        mContext = getApplicationContext();
        NetUtil.init(mContext);
        HappinessLevel = answer1.getText().toString();
        WorkingContent = answer2.getText().toString();
        ThermalComfort = answer3.getText().toString();
        Healthy = answer4.getText().toString();
        Satisfactory = answer5.getText().toString();
        EmotionType = answer6.getText().toString();
        Conversation = answer7.getText().toString();
        SleepHour = answer8.getText().toString();

        NetUtil.sendFeedbackData(HappinessLevel, WorkingContent, ThermalComfort, Healthy, Satisfactory, EmotionType,Conversation,SleepHour,
        new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                appendToUI();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
    }

    private void appendToUI() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Toast toast = Toast.makeText(mContext, "Data update complete!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
                Looper.loop();
            }
        }).start();
    }
}

