package com.a11.chucknorris;

import android.app.Application;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;

public class MyApplication extends Application {
    public static Disposable api;
    public static Single<String> apiObservable;
}
