package com.example.flight_booking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Splash_screen extends AppCompatActivity {
   ImageView imageView;
    Animation topAnimation,middleAnimation,bottomanimation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        imageView=findViewById(R.id.imageView);
        topAnimation = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomanimation=AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        middleAnimation =AnimationUtils.loadAnimation(this,R.anim.middle_animation);


        imageView.setAnimation(middleAnimation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(Splash_screen.this,MainActivity.class));
            }
        },8000);
    }
}