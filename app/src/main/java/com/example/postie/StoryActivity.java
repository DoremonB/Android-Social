package com.example.postie;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.List;

public class StoryActivity extends AppCompatActivity {
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
}
