package com.example.android.nitkart;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toolbar;

import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

public class SplashScreen extends AppCompatActivity {
    ShimmerTextView tv;
    Shimmer shimmer;
    Thread myThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        tv = findViewById(R.id.shimmerText);
        shimmer = new Shimmer();
        shimmer.start(tv);


        myThread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(1800);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();
    }
}
