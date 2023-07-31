package com.example.childvaccinereminder;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.api.Http;

public class NavigationActivity extends AppCompatActivity {

    ViewPager slideViewPager;
    LinearLayout dotIndicator;
    ViewPagerAdapter viewPagerAdapter;
    Button backButton, skipButton, nextButton;
    TextView[] dots;

    ViewPager.OnPageChangeListener viewPageListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onPageSelected(int position) {

            setDotIndicator(position);
            if(position>0){
                backButton.setVisibility(View.VISIBLE);
            }else {
                backButton.setVisibility(View.INVISIBLE);
            }
            if(position==2){
                nextButton.setText("Finish");
            }else{
                nextButton.setText("Next");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        backButton = findViewById(R.id.backButton);
        nextButton = findViewById(R.id.nextButton);
        skipButton = findViewById(R.id.skipButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getItem(0) > 0){
                    slideViewPager.setCurrentItem(getItem(-1),true);
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getItem(0) < 2){
                    slideViewPager.setCurrentItem(getItem(1),true);
                }else{
                    Intent intent=new Intent(NavigationActivity.this,GetStarted.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(NavigationActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        slideViewPager=(ViewPager) findViewById(R.id.slideViewPager);
        dotIndicator=(LinearLayout) findViewById(R.id.dotIndicator);

        viewPagerAdapter = new ViewPagerAdapter(this);
        slideViewPager.setAdapter(viewPagerAdapter);

        setDotIndicator(0);
        slideViewPager.addOnPageChangeListener(viewPageListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setDotIndicator(int position){
        dots = new TextView[3];
        dotIndicator.removeAllViews();

        for(int i = 0 ;i < dots.length ; i++)
        {
            dots[i]=new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226",Html.FROM_HTML_MODE_LEGACY));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.grey,getApplicationContext().getTheme()));
            dotIndicator.addView(dots[i]);
        }
        dots[position].setTextColor(getResources().getColor(R.color.colorAccent,getApplicationContext().getTheme()));
    }
    private int getItem(int i){
        return slideViewPager.getCurrentItem()+i;
    }
}