package com.dipansh.pg;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;

/**
 * Created by Dipansh on 8/30/2016.
 */
public class SplashScreen extends Activity {
    public static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashScreen.this,MainActivity.class);
                SplashScreen.this.startActivity(mainIntent);

                                /* Finish splash activity so user cant go back to it. */
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
                finish();
                SplashScreen.this.finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        },SPLASH_TIME_OUT);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        Intent i = new Intent(SplashScreen.this, MainActivity.class);
        startActivity(i);
        finish();
        return super.onTouchEvent(event);

    }
}
