package mx.ipn.escom.agendaeventosapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

import mx.ipn.escom.agendaeventosapp.ui.MainActivity;

public class SplashActivity extends Activity {

    private long splashDelay = 5000;
    private Handler mTimerHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        startAnimations();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                mTimerHandler.post(new Runnable() {
                    public void run() {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, splashDelay);
    }

    private void startAnimations() {
//        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
//        anim.reset();
//        LinearLayout l = (LinearLayout) findViewById(R.id.lin_lay);
//        l.clearAnimation();
//        l.startAnimation(anim);
//        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
//        anim.reset();
//        ImageView iv = (ImageView) findViewById(R.id.logo_splash);
//        iv.clearAnimation();
//        iv.startAnimation(anim);
    }
}
