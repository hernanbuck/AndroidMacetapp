package com.example.macetapp40;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Splash_activity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 5000;

    //Variables
    Animation topAnim, bottomAnim;
    ImageView splashImage;
    TextView title1, title2, slogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        //Animations
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        //Hooks
        splashImage = findViewById(R.id.splashImage);
        title1 = findViewById(R.id.title1);
        title2 = findViewById(R.id.title2);
        slogan = findViewById(R.id.slogan);

        //Set Animation
        splashImage.setAnimation(bottomAnim);
        title1.setAnimation(topAnim);
        title2.setAnimation(topAnim);
        slogan.setAnimation(topAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent (Splash_activity.this,Login.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_SCREEN);

    }
}