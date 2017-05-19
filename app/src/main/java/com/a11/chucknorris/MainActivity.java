package com.a11.chucknorris;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity {

    private String joke;
    private String defaultJoke;
    private MyApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        application = (MyApplication) getApplication();
        defaultJoke = getResources().getString(R.string.default_joke);
        loadJoke();

        View loadIcon = findViewById(R.id.load_icon);
        loadIcon.setAlpha(0);
    }

    protected void onResume() {
        super.onResume();
        if (application.api != null) {
            animateLoad();
            application.api = application.apiObservable.subscribe(this::processJoke);
        }
        setScreen(joke);
    }

    protected void onPause() {
        saveJoke(joke);
        super.onPause();
    }

    public void setScreen(String joke) {
        joke = joke.replaceAll("&quot;", "\"");
        ((TextView) findViewById(R.id.textView)).setText(joke);
    }

    public void animateLoad() {
        RotateAnimation rotate;
        rotate = new RotateAnimation(0f, -360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(800);
        rotate.setRepeatMode(Animation.START_ON_FIRST_FRAME);
        rotate.setRepeatCount(Animation.INFINITE);
        rotate.setInterpolator(new AccelerateDecelerateInterpolator());
        View loadIcon = findViewById(R.id.load_icon);
        loadIcon.setAlpha(1);
        loadIcon.startAnimation(rotate);
    }

    private void stopLoad() {
        View loadIcon = findViewById(R.id.load_icon);
        loadIcon.setAlpha(0);
        loadIcon.clearAnimation();
    }

    public void onClick(View v) {
        if (application.api != null && !application.api.isDisposed()) {
            application.api.dispose();
        }

        animateLoad();
        application.apiObservable = Api.get.getRandomJoke()
                .cache()
                .delay(3, TimeUnit.SECONDS)
                .doOnSuccess(newJoke  -> Log.e("JOKE", newJoke))
                .observeOn(AndroidSchedulers.mainThread());

        application.api = application.apiObservable.subscribe(this::processJoke);
        Log.e("JOKE", "clicked");
    }

    private void processJoke(String pJoke) {
        joke = pJoke;
        saveJoke(pJoke);
        setScreen(pJoke);
        stopLoad();
        application.apiObservable = null;
        application.api = null;
    }

    public void saveJoke(String joke) {
        SharedPreferences sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString("joke", joke);
        editor.apply();
    }

    public void loadJoke() {
        SharedPreferences sPref = getPreferences(MODE_PRIVATE);
        try {
            joke = sPref.getString("joke", defaultJoke);
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }
    }
}
