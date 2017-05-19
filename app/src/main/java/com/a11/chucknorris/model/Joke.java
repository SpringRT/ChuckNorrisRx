package com.a11.chucknorris.model;

import com.google.gson.annotations.SerializedName;

public class Joke {
    @SerializedName("id")
    private int id;
    @SerializedName("joke")
    private String text;

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return text;
    }
}
