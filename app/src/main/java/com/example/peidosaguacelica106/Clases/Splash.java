package com.example.peidosaguacelica106.Clases;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.peidosaguacelica106.R;

public class Splash extends Activity {

private AnimatorSet animatorSet;
ImageView loja,nube1,nube2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               Intent intent=new Intent(Splash.this,MainActivity.class);
               startActivity(intent);
            }
        },5000);


    }
}
