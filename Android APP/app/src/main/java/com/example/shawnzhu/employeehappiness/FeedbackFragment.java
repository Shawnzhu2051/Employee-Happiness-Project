package com.example.shawnzhu.employeehappiness;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shawnzhu.employeehappiness.R;
import com.microsoft.band.BandClientManager;
import com.microsoft.band.BandException;
import com.microsoft.band.BandInfo;
import com.microsoft.band.ConnectionState;
import com.microsoft.band.UserConsent;

import static com.example.shawnzhu.employeehappiness.RealtimeFragment.client;

public class FeedbackFragment extends Fragment {

    View mView;
    public Context mContext;
    public Button start_button;
    public Button Indicator;
    private CountDownTimer mTime;
    public Intent intent_to_question;
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.feedback,null);
            mContext = getActivity().getApplicationContext();
            final TextView showtime = (TextView) mView.findViewById(R.id.countdown);
            mTime = new CountDownTimer(3600000,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    long min = millisUntilFinished/60000;
                    long sec = (millisUntilFinished - min*60000)/1000;

                    if (sec < 10){
                        showtime.setText(min + "：" + "0" + sec);
                    }
                    else{
                        showtime.setText(min + "：" + sec);
                    }
                }

                @Override
                public void onFinish() {
                    showtime.setText("00 : 00");
                }
            };
            mTime.start();
        }

        start_button = (Button) mView.findViewById(R.id.start_feedback);
        start_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View mView){
                try {
                    if(checkConnectedBandClient()){
                        if (client.getSensorManager().getCurrentHeartRateConsent() == UserConsent.GRANTED) {
                            mTime.start();
                            intent_to_question = new Intent(getActivity(), QuestionActivity.class);
                            startActivity(intent_to_question);
                        }
                        else{
                            appendToUI("You have not given this application consent to access heart rate data yet."
                                    + " Please press the Heart Rate Consent button.");
                        }
                    }
                    else{
                        appendToUI("Band isn't connected. Please make sure bluetooth is on and the band is in range.");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BandException e) {
                    e.printStackTrace();
                }
            }
        });



        return mView;
    }

    private boolean checkConnectedBandClient() throws InterruptedException, BandException {
        if (client == null) {
            BandInfo[] devices = BandClientManager.getInstance().getPairedBands();
            if (devices.length == 0) {
                appendToUI("Band isn't paired with your phone.\n");
                return false;
            }
        }
        else if (ConnectionState.CONNECTED == client.getConnectionState()) {
            return true;
        }
        return false;
    }


    private void appendToUI(final String msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Toast toast = Toast.makeText(mContext, msg, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
                Looper.loop();
            }
        }).start();
    }
}