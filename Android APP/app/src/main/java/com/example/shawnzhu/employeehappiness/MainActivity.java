package com.example.shawnzhu.employeehappiness;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.microsoft.band.ConnectionState;

import java.util.ArrayList;
import java.util.List;

import static com.example.shawnzhu.employeehappiness.RealtimeFragment.client;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity.TAG";
    TextView titleTextView;
    public LinearLayout firstLinearLayout;
    public LinearLayout secondLinearLayout;
    public LinearLayout threeLinearLayout;
    ViewPager mViewPager;
    ViewPagerFragmentAdapter mViewPagerFragmentAdapter;
    FragmentManager mFragmentManager;
    public static ImageView connect;

    String[] titleName = new String[] {"Feedback","Real-Time Data","About Me"};
    List<Fragment> mFragmentList = new ArrayList<Fragment>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mFragmentManager = getSupportFragmentManager();
        setContentView(R.layout.activity_main);
        initFragmetList();
        mViewPagerFragmentAdapter = new ViewPagerFragmentAdapter(mFragmentManager,mFragmentList);
        initView();
        initViewPager();
        connect = (ImageView) findViewById(R.id.connect);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void initViewPager() {
        mViewPager.addOnPageChangeListener(new ViewPagetOnPagerChangedLisenter());
        mViewPager.setAdapter(mViewPagerFragmentAdapter);
        mViewPager.setCurrentItem(0);
        titleTextView.setText(titleName[0]);

        updateBottomLinearLayoutSelect(true,false,false);
    }

    public void initFragmetList() {
        Fragment feedback = new FeedbackFragment();
        Fragment rtdata = new RealtimeFragment();
        Fragment about = new AboutFragment();
        mFragmentList.add(feedback);
        mFragmentList.add(rtdata);
        mFragmentList.add(about);
    }

    public void initView() {
        titleTextView = (TextView) findViewById(R.id.ViewTitle);
        mViewPager = (ViewPager) findViewById(R.id.ViewPagerLayout);
        firstLinearLayout = (LinearLayout) findViewById(R.id.firstLinearLayout);
        firstLinearLayout.setOnClickListener(this);
        secondLinearLayout = (LinearLayout) findViewById(R.id.secondLinearLayout);
        secondLinearLayout.setOnClickListener(this);
        threeLinearLayout = (LinearLayout) findViewById(R.id.threeLinearLayout);
        threeLinearLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.firstLinearLayout:
                mViewPager.setCurrentItem(0);
                updateBottomLinearLayoutSelect(true,false,false);
                break;
            case R.id.secondLinearLayout:
                mViewPager.setCurrentItem(1);
                updateBottomLinearLayoutSelect(false,true,false);
                break;
            case R.id.threeLinearLayout:
                mViewPager.setCurrentItem(2);
                updateBottomLinearLayoutSelect(false,false,true);
                break;
            default:
                break;
        }
    }
    private void updateBottomLinearLayoutSelect(boolean f, boolean s, boolean t) {
        firstLinearLayout.setSelected(f);
        secondLinearLayout.setSelected(s);
        threeLinearLayout.setSelected(t);
        if(f & !s & !t){
            firstLinearLayout.setBackgroundColor(Color.argb(0xff,0,191,255));
            firstLinearLayout.setElevation(10);
            secondLinearLayout.setBackgroundColor(Color.argb(0xff,240,240,240));
            secondLinearLayout.setElevation(5);
            threeLinearLayout.setBackgroundColor(Color.argb(0xff,240,240,240));
            threeLinearLayout.setElevation(5);
        }
        else if(!f & s & !t){
            firstLinearLayout.setBackgroundColor(Color.argb(0xff,240,240,240));
            firstLinearLayout.setElevation(5);
            secondLinearLayout.setBackgroundColor(Color.argb(0xff,0,191,255));
            secondLinearLayout.setElevation(10);
            threeLinearLayout.setBackgroundColor(Color.argb(0xff,240,240,240));
            threeLinearLayout.setElevation(5);
        }
        else{
            firstLinearLayout.setBackgroundColor(Color.argb(0xff,240,240,240));
            firstLinearLayout.setElevation(5);
            secondLinearLayout.setBackgroundColor(Color.argb(0xff,240,240,240));
            secondLinearLayout.setElevation(5);
            threeLinearLayout.setBackgroundColor(Color.argb(0xff,0,191,255));
            threeLinearLayout.setElevation(10);
        }
    }
    class ViewPagetOnPagerChangedLisenter implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            Log.d(TAG,"onPageScrooled");
        }
        @Override
        public void onPageSelected(int position) {
            Log.d(TAG,"onPageSelected");
            boolean[] state = new boolean[titleName.length];
            state[position] = true;
            titleTextView.setText(titleName[position]);
            updateBottomLinearLayoutSelect(state[0],state[1],state[2]);
        }
        @Override
        public void onPageScrollStateChanged(int state) {
            Log.d(TAG,"onPageScrollStateChanged");
        }
    }
}