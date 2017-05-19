package com.a11.chucknorris;

import com.a11.chucknorris.model.BaseResponse;
import com.a11.chucknorris.model.Joke;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

import static com.a11.chucknorris.ApiInterface.API_HOST;

interface ApiInterface {
    String API_HOST = "https://api.icndb.com/";

    @GET("jokes/random")
    Single<BaseResponse<Joke>> getRandomJoke();
}

public enum Api {
    get;

    private final ApiInterface service;
    private OkHttpClient mClient;
    private Retrofit mRetrofit;


    Api() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        mClient = new OkHttpClient.Builder().
                addInterceptor(interceptor)
                .build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(API_HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(mClient)
                .build();

        service = mRetrofit.create(ApiInterface.class);
    }

    public Single<String> getRandomJoke() {
        return service.getRandomJoke()
                .subscribeOn(Schedulers.io())
                .map(jokeBaseResponse -> jokeBaseResponse.getValue().getText())
                .onErrorReturn(pt -> "No joke for you, cause of: " + pt.getMessage());
    }

}
