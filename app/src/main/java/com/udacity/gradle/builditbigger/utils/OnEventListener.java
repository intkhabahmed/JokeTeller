package com.udacity.gradle.builditbigger.utils;

public interface OnEventListener<T> {
    void onSuccess(T object);

    void onFailure(Exception e);
}
