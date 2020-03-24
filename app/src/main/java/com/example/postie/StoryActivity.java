package com.example.postie;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class StoryActivity extends AppCompatActivity {
    private int progressCount=0;
    private int progressBarIndex=0;
    private Timer timer;
    private ImageView imageView;
    private LinearLayout progressContainer;
    public static List<String> images;
    private ProgressBar defaultProgressBar;
    private boolean timerON=true;
    private float upperLimit;
    private float lowerLimit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        init();
        Glide.with(this).load(images.get(progressBarIndex)).placeholder(R.drawable.default_profile_pic).into(imageView);
        setProgressBars();
        setControls();
        setTimer();

    }

    private void setControls() {

        float widthProportion=(getResources().getDisplayMetrics().widthPixels/100.0f)*25;

        upperLimit=getResources().getDisplayMetrics().widthPixels-widthProportion;
        lowerLimit=widthProportion;

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:

                        if(event.getX()<=lowerLimit && progressBarIndex>0){
                            ((ProgressBar) progressContainer.getChildAt(progressBarIndex))
                                    .setProgress(0);
                            progressBarIndex--;
                            ((ProgressBar) progressContainer.getChildAt(progressBarIndex))
                                    .setProgress(0);
                            progressCount=0;
                            Glide.with(getApplicationContext()).load(images.get(progressBarIndex)).into(imageView);
                        }
                        else if(event.getX()>=upperLimit && progressBarIndex<progressContainer.getChildCount()-1){
                            ((ProgressBar) progressContainer.getChildAt(progressBarIndex))
                                    .setProgress(100);
                            progressBarIndex++;
                            ((ProgressBar) progressContainer.getChildAt(progressBarIndex))
                                    .setProgress(0);
                            progressCount=0;
                            Glide.with(getApplicationContext()).load(images.get(progressBarIndex)).into(imageView);
                        }
                        else {
                            timerON = false;
                            return true;
                        }
                    case MotionEvent.ACTION_UP:
                        timerON=true;
                        return true;
                }

                return false;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        setTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
    }

    private void init() {
        imageView=findViewById(R.id.imageView);
        progressContainer=findViewById(R.id.progress_container);
        defaultProgressBar=((ProgressBar)progressContainer.getChildAt(0));
    }

    private void setProgressBars(){
        if(images!=null){
            for(int i=1;i<images.size();i++) {
                ProgressBar progressBar = new ProgressBar(this,null,android.R.attr.progressBarStyleHorizontal);
                progressBar.setLayoutParams(defaultProgressBar.getLayoutParams());
                progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.story_progress));
                progressContainer.addView(progressBar);
            }
        }

    }
    private void setTimer(){
        timer=new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
//                1000ms * 1 = 1s //will be called 1 time in 1 sec
//                100ms * 10 = 1s //will be called 10 times in 1 sec
//                50ms * 100 =5s
                    if (timerON){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (progressCount == 100) {
                                    progressBarIndex++;
                                    if (progressBarIndex < progressContainer.getChildCount()) {
                                        progressCount = 0;
                                        //Change Image Here
                                        Glide.with(getApplicationContext()).load(images.get(progressBarIndex)).into(imageView);
                                    } else {
                                        finish();
                                    }


                                } else {
                                    progressCount++;
                                    ((ProgressBar) progressContainer.getChildAt(progressBarIndex))
                                            .setProgress(progressCount);
                                }
                            }
                        });
                    }

            }
        },0,50);
    }
}
