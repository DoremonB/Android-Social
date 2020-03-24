package com.example.postie;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        init();
        setProgressBars();

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

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(progressCount == 100){
                            progressBarIndex++;
                            if(progressBarIndex<progressContainer.getChildCount()){
                                progressCount=0;
                                //Change Image Here
                            }
                            else{
                                finish();
                            }



                        }else {
                            progressCount++;
                            ((ProgressBar) progressContainer.getChildAt(progressBarIndex))
                                    .setProgress(progressCount);
                        }
                    }
                });

            }
        },0,50);
    }
}
