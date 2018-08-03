package com.example.shawnzhu.employeehappiness;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.shawnzhu.employeehappiness.EHUser;
import com.example.shawnzhu.employeehappiness.R;

public class AboutFragment extends Fragment {

    View mView;
    public Button logout;
    public Intent intent_to_login;
    public TextView username_info;
    public TextView gender_info;
    public TextView age_info;
    public TextView email_info;
    public TextView location_info;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.about_me,null);
            logout = (Button) mView.findViewById(R.id.Logout);
            SharedPreferences login_info = getActivity().getSharedPreferences("login_info", Context.MODE_PRIVATE);
            String name = login_info.getString("name","");
            String email = login_info.getString("email","");
            String age = login_info.getString("age","");
            String gender = login_info.getString("gender","");
            String location = login_info.getString("location","");

            username_info = (TextView) mView.findViewById(R.id.username_info);
            gender_info = (TextView) mView.findViewById(R.id.gender_info);
            age_info = (TextView) mView.findViewById(R.id.age_info);
            email_info = (TextView) mView.findViewById(R.id.email_info);
            location_info = (TextView) mView.findViewById(R.id.location_info);

            username_info.setText("Name: " + name);
            if (gender == "2"){gender_info.setText("Gender: male");}
            else{gender_info.setText("Gender: female");}
            age_info.setText("Age: "+ age);
            email_info.setText("email: "+ email);
            location_info.setText("location: "+location);

            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent_to_login = new Intent(getActivity(), LoginActivity.class);
                    //getActivity().finish();
                    startActivity(intent_to_login);

                }
            });
        }
        //((TextView)mView.findViewById(R.id.mTextView)).setText("聊天界面");
        return mView;
    }
}