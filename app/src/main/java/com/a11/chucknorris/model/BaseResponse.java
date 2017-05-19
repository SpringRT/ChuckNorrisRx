package com.a11.chucknorris.model;

import com.google.gson.annotations.SerializedName;

public class BaseResponse<T>  {
    @SerializedName("type")
    private String type;
    @SerializedName("value")
    private T value;

    public String getType() {
        return type;
    }

    public T getValue() {
        return value;
    }

    public static BaseResponse<Joke> getFailedJokeResponse() {
        BaseResponse response = new BaseResponse();
        response.type = "error";
        response.value = new Joke();
        return response;
    }
}

