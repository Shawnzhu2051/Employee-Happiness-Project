package com.example.shawnzhu.employeehappiness;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.microsoft.band.BandClient;
import com.microsoft.band.BandClientManager;
import com.microsoft.band.BandException;
import com.microsoft.band.BandInfo;
import com.microsoft.band.BandIOException;
import com.microsoft.band.ConnectionState;

import com.microsoft.band.sensors.BandAltimeterEvent;
import com.microsoft.band.sensors.BandAltimeterEventListener;

import com.microsoft.band.sensors.BandAccelerometerEvent;
import com.microsoft.band.sensors.BandAccelerometerEventListener;
import com.microsoft.band.sensors.SampleRate;

import com.microsoft.band.sensors.BandAmbientLightEvent;
import com.microsoft.band.sensors.BandAmbientLightEventListener;

import com.microsoft.band.sensors.BandBarometerEvent;
import com.microsoft.band.sensors.BandBarometerEventListener;

import com.microsoft.band.sensors.BandCaloriesEvent;
import com.microsoft.band.sensors.BandCaloriesEventListener;

import com.microsoft.band.sensors.BandDistanceEvent;
import com.microsoft.band.sensors.BandDistanceEventListener;

import com.microsoft.band.sensors.BandGsrEvent;
import com.microsoft.band.sensors.BandGsrEventListener;

import com.microsoft.band.sensors.BandGyroscopeEvent;
import com.microsoft.band.sensors.BandGyroscopeEventListener;

import com.microsoft.band.sensors.BandHeartRateEvent;
import com.microsoft.band.sensors.BandHeartRateEventListener;

import com.microsoft.band.sensors.BandPedometerEvent;
import com.microsoft.band.sensors.BandPedometerEventListener;

import com.microsoft.band.sensors.BandRRIntervalEvent;
import com.microsoft.band.sensors.BandRRIntervalEventListener;

import com.microsoft.band.sensors.BandSkinTemperatureEvent;
import com.microsoft.band.sensors.BandSkinTemperatureEventListener;

import com.microsoft.band.sensors.BandUVEvent;
import com.microsoft.band.sensors.BandUVEventListener;


import com.microsoft.band.sensors.HeartRateConsentListener;
import com.microsoft.band.UserConsent;

import com.example.shawnzhu.employeehappiness.R;

import org.w3c.dom.Text;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class RealtimeFragment extends Fragment {

    View mView;
    Context mContext;

    private CountDownTimer mTime1;
    private CountDownTimer mTime2;
    public TextView air_temp;
    public TextView heart_rate;
    public TextView skin_temp;
    public TextView air_pressure;
    public TextView brightness;

    static  public BandClient client = null;
    public float AccelerationX = 0;
    public float AccelerationY = 0;
    public float AccelerationZ = 0;
    public long TotalGain = 0;
    public long TotalLoss = 0;
    public long SteppingGain = 0;
    public long SteppingLoss = 0;
    public long StepsAscended = 0;
    public long StepsDescended = 0;
    public float Rate = 0;
    public long FlightsAscended = 0;
    public long FlightsDescended = 0;
    public long Brightness = 0;
    public double AirPressure = 0;
    public double Temperature = 0;
    public long Calories = 0;
    public String CurrentMotion = null;
    public float Pace = 0;
    public float Speed = 0;
    public long TotalDistance = 0;
    public float Resistance = 0;
    public double AngularVelocityX = 0;
    public double AngularVelocityY = 0;
    public double AngularVelocityZ = 0;
    public double HeartRate = 0;
    public long TotalSteps = 0;
    public double Interval = 0;
    public double SkinTemperature = 0;
    public String UVLevel = null;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.real_time,container,false);
            this.mContext = getActivity();
            air_temp =(TextView)mView.findViewById(R.id.airtemp_data);
            heart_rate = (TextView)mView.findViewById(R.id.heart_rate_data);
            skin_temp = (TextView)mView.findViewById(R.id.skintemp_data);
            air_pressure = (TextView)mView.findViewById(R.id.airpressure_data);
            brightness = (TextView)mView.findViewById(R.id.brightness_data);
            final WeakReference<Activity> reference = new WeakReference<Activity>(this.getActivity());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    new HeartRateConsentTask().execute(reference);
                }
            }).start();
            new SensorSubscriptionTask().execute();
        }

        MainActivity.connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if(checkConnectedBandClient()){
                                appendToUI("Band is connected.");
                            }
                            else{
                                new SensorSubscriptionTask().execute();
                                
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (BandException e) {
                            e.printStackTrace();}
                    }
                }).start();;
            }
        });

        return mView;
    }

    public void saveData(){
        mTime1 = new CountDownTimer(5000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }
            @Override
            public void onFinish() {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("realtime_data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
                editor.putString("AccelerationX",String.format("%.3f", AccelerationX));
                editor.putString("AccelerationY",String.format("%.3f", AccelerationY));
                editor.putString("AccelerationZ",String.format("%.3f", AccelerationZ));
                editor.putString("FlightsAscended",String.format("%d", FlightsAscended));
                editor.putString("FlightsDescended",String.format("%d", FlightsDescended));
                editor.putString("Rate",String.format("%.3f", Rate));
                editor.putString("SteppingGain",String.format("%d", SteppingGain));
                editor.putString("SteppingLoss",String.format("%d", SteppingLoss));
                editor.putString("StepsAscended",String.format("%d", StepsAscended));
                editor.putString("StepsDescended",String.format("%d", StepsDescended));
                editor.putString("TotalGain",String.format("%d", TotalGain));
                editor.putString("TotalLoss",String.format("%d", TotalLoss));
                editor.putString("Brightness",String.format("%d", Brightness));
                editor.putString("AirPressure",String.format("%f", AirPressure));
                editor.putString("Temperature",String.format("%f", Temperature));
                editor.putString("Calories",String.format("%d", Calories));
                editor.putString("CurrentMotion",String.format("%s", CurrentMotion));
                editor.putString("Pace",String.format("%.3f", Pace));
                editor.putString("Speed",String.format("%.3f", Speed));
                editor.putString("TotalDistance",String.format("%d", TotalDistance));
                editor.putString("Resistance",String.format("%f", Resistance));
                editor.putString("AngularVelocityX",String.format("%f", AngularVelocityX));
                editor.putString("AngularVelocityY",String.format("%f", AngularVelocityY));
                editor.putString("AngularVelocityZ",String.format("%f", AngularVelocityZ));
                editor.putString("HeartRate",String.format("%f", HeartRate));
                editor.putString("TotalSteps",String.format("%d", TotalSteps));
                editor.putString("RRInterval",String.format("%f", Interval));
                editor.putString("SkinTemperature",String.format("%f", SkinTemperature));
                editor.putString("IndexLevel",String.format("%s", UVLevel));
                editor.commit();

                air_temp.setText(String.format("%.2f degree", Temperature));
                heart_rate.setText(String.format("%.1f /min", HeartRate));
                skin_temp.setText(String.format("%.2f degree", SkinTemperature));
                air_pressure.setText(String.format("%.2f hpa", AirPressure));
                brightness.setText(String.format("%d lux", Brightness));

                mTime1.start();
                Log.d("REALTIME","Finished");
            }
        };
        mTime1.start();
    }

    public void sendData(){
        mTime2 = new CountDownTimer(600000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }
            @Override
            public void onFinish() {
                NetUtil.sendRealTimeData(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
                mTime2.start();
            }
        };
        mTime2.start();
    }


    public BandAccelerometerEventListener mAccelerometerEventListener = new BandAccelerometerEventListener() {
        @Override
        public void onBandAccelerometerChanged(final BandAccelerometerEvent event) {
            if (event != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AccelerationX = event.getAccelerationX();
                        AccelerationY = event.getAccelerationY();
                        AccelerationZ = event.getAccelerationZ();
                        //
                    }
                });
            }
        }
    };

    public BandAltimeterEventListener mAltimeterEventListener = new BandAltimeterEventListener(){
        @Override
        public void onBandAltimeterChanged(final BandAltimeterEvent event) {
            if (event != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TotalGain = event.getTotalGain();
                        TotalLoss = event.getTotalLoss();
                        SteppingGain = event.getSteppingGain();
                        SteppingLoss = event.getSteppingLoss();
                        StepsAscended = event.getStepsAscended();
                        StepsDescended = event.getStepsDescended();
                        Rate = event.getRate();
                        FlightsAscended = event.getFlightsAscended();
                        FlightsDescended = event.getFlightsDescended();
                    }
                });
            }
        }
    };


    public BandAmbientLightEventListener mAmbientLightEventListener = new BandAmbientLightEventListener(){
        @Override
        public void onBandAmbientLightChanged(final BandAmbientLightEvent event) {
            if (event != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Brightness = event.getBrightness();
                    }
                });
            }
        }
    };

    public BandBarometerEventListener mBarometerEventListener = new BandBarometerEventListener() {
        @Override
        public void onBandBarometerChanged(final BandBarometerEvent event) {
            if (event != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AirPressure = event.getAirPressure();
                        Temperature = event.getTemperature();
                    }
                });
            }
        }
    };

    public BandCaloriesEventListener mCaloriesEventListener = new BandCaloriesEventListener() {
        @Override
        public void onBandCaloriesChanged(final BandCaloriesEvent event) {
            if (event != null){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Calories = event.getCalories();
                    }
                });
            }
        }
    };

    public BandDistanceEventListener mDistanceEventListener = new BandDistanceEventListener() {
        @Override
        public void onBandDistanceChanged(final BandDistanceEvent event) {
            if(event != null){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CurrentMotion = event.getMotionType().toString();
                        Pace = event.getPace();
                        Speed = event.getSpeed();
                        TotalDistance = event.getTotalDistance();
                    }
                });
            }
        }
    };

    public BandGsrEventListener mGsrEventListener = new BandGsrEventListener() {
        @Override
        public void onBandGsrChanged(final BandGsrEvent event) {
            if (event != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Resistance = event.getResistance();
                    }
                });
            }
        }
    };

    public BandGyroscopeEventListener mGyroscopeEventListener = new BandGyroscopeEventListener() {
        @Override
        public void onBandGyroscopeChanged(final BandGyroscopeEvent event) {
            if (event != null){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AngularVelocityX = event.getAngularVelocityX();
                        AngularVelocityY = event.getAngularVelocityY();
                        AngularVelocityZ = event.getAngularVelocityZ();
                    }
                });
            }
        }
    };

    public BandHeartRateEventListener mHeartRateEventListener = new BandHeartRateEventListener() {
        @Override
        public void onBandHeartRateChanged(final BandHeartRateEvent event) {
            if (event != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        HeartRate = event.getHeartRate();
                    }
                });
            }
        }
    };

    public BandPedometerEventListener mPedometerEventListener = new BandPedometerEventListener() {
        @Override
        public void onBandPedometerChanged(final BandPedometerEvent event) {
            if(event != null){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TotalSteps = event.getTotalSteps();
                    }
                });
            }
        }
    };

    public BandRRIntervalEventListener mRRIntervalEventListener = new BandRRIntervalEventListener() {
        @Override
        public void onBandRRIntervalChanged(final BandRRIntervalEvent event) {
            if (event != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Interval = event.getInterval();
                    }
                });
            }
        }
    };

    public BandSkinTemperatureEventListener mBandSkinTemperatureEventListener = new BandSkinTemperatureEventListener() {
        @Override
        public void onBandSkinTemperatureChanged(final BandSkinTemperatureEvent event) {
            if(event != null){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SkinTemperature = event.getTemperature();
                    }
                });
            }
        }
    };

    public BandUVEventListener mBandUVEventListener = new BandUVEventListener() {
        @Override
        public void onBandUVChanged(final BandUVEvent event) {
            if(event != null){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        UVLevel = event.getUVIndexLevel().toString();
                    }
                });
            }
        }
    };

    private class SensorSubscriptionTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                if (getConnectedBandClient()) {
                    appendToUI("Band is connected.");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            saveData();
                            sendData();
                        }
                    });
                    client.getSensorManager().registerAccelerometerEventListener(mAccelerometerEventListener, SampleRate.MS128);
                    client.getSensorManager().registerAltimeterEventListener(mAltimeterEventListener);
                    client.getSensorManager().registerAmbientLightEventListener(mAmbientLightEventListener);
                    client.getSensorManager().registerBarometerEventListener(mBarometerEventListener);
                    client.getSensorManager().registerCaloriesEventListener(mCaloriesEventListener);
                    client.getSensorManager().registerDistanceEventListener(mDistanceEventListener);
                    client.getSensorManager().registerGsrEventListener(mGsrEventListener);
                    client.getSensorManager().registerPedometerEventListener(mPedometerEventListener);
                    client.getSensorManager().registerSkinTemperatureEventListener(mBandSkinTemperatureEventListener);
                    client.getSensorManager().registerUVEventListener(mBandUVEventListener);
                    if (client.getSensorManager().getCurrentHeartRateConsent() == UserConsent.GRANTED) {
                        client.getSensorManager().registerHeartRateEventListener(mHeartRateEventListener);
                        client.getSensorManager().registerRRIntervalEventListener(mRRIntervalEventListener);
                    } else {
                        appendToUI("You have not given this application consent to access heart rate data yet."
                                + " Please press the Heart Rate Consent button.");
                    }
                } else {
                    appendToUI("Band isn't connected. Please make sure bluetooth is on and the band is in range.");
                }
            } catch (BandException e) {
                String exceptionMessage="";
                switch (e.getErrorType()) {
                    case UNSUPPORTED_SDK_VERSION_ERROR:
                        exceptionMessage = "Microsoft Health BandService doesn't support your SDK Version. Please update to latest SDK.";
                        break;
                    case SERVICE_ERROR:
                        exceptionMessage = "Microsoft Health BandService is not available. Please make sure Microsoft Health is installed and that you have the correct permissions.";
                        break;
                    default:
                        exceptionMessage = "Unknown error occured: " + e.getMessage();
                        break;
                }
                appendToUI(exceptionMessage);

            } catch (Exception e) {
                appendToUI(e.getMessage());
            }
            return null;
        }
    }

    private void appendToUI(final String msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Toast toast = Toast.makeText(mContext, msg, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
                Log.d("BAND LOG:", msg);
                Looper.loop();
            }
        }).start();
    }

    private boolean getConnectedBandClient() throws InterruptedException, BandException {
        if (client == null) {
            BandInfo[] devices = BandClientManager.getInstance().getPairedBands();
            if (devices.length == 0) {
                appendToUI("Band isn't paired with your phone.");
                return false;
            }
            client = BandClientManager.getInstance().create(getActivity(), devices[0]);
        } else if (ConnectionState.CONNECTED == client.getConnectionState()) {
            return true;
        }
        appendToUI("Band is connecting...");
        return ConnectionState.CONNECTED == client.connect().await();
    }

    private boolean checkConnectedBandClient() throws InterruptedException, BandException {
        if (client == null) {
            BandInfo[] devices = BandClientManager.getInstance().getPairedBands();
            if (devices.length == 0) {
                appendToUI("Band isn't paired with your phone.");
                return false;
            }
            client = BandClientManager.getInstance().create(getActivity(), devices[0]);
        } else if (ConnectionState.CONNECTED == client.getConnectionState()) {
            return true;
        }
        return false;
    }


    private class HeartRateConsentTask extends AsyncTask<WeakReference<Activity>, Void, Void> {
        @Override
        protected Void doInBackground(WeakReference<Activity>... params) {
            try {
                if (getConnectedBandClient()) {
                    if (params[0].get() != null) {
                        client.getSensorManager().requestHeartRateConsent(params[0].get(), new HeartRateConsentListener() {
                            @Override
                            public void userAccepted(boolean consentGiven) {
                            }
                        });
                    }
                } else {
                    appendToUI("Band isn't connected. Please make sure bluetooth is on and the band is in range.");
                }
            } catch (BandException e) {
                String exceptionMessage="";
                switch (e.getErrorType()) {
                    case UNSUPPORTED_SDK_VERSION_ERROR:
                        exceptionMessage = "Microsoft Health BandService doesn't support your SDK Version. Please update to latest SDK.";
                        break;
                    case SERVICE_ERROR:
                        exceptionMessage = "Microsoft Health BandService is not available. Please make sure Microsoft Health is installed and that you have the correct permissions.";
                        break;
                    default:
                        exceptionMessage = "Unknown error occured: " + e.getMessage();
                        break;
                }
                appendToUI(exceptionMessage);
            } catch (Exception e) {
                appendToUI(e.getMessage());
            }
            return null;
        }
    }
}