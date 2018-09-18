package com.udacity.gradle.builditbigger.utils;

import android.support.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.Nullable;

public class JokeIdlingResource implements IdlingResource {
    @Nullable
    private volatile ResourceCallback mCallback;
    private AtomicBoolean mIsIdle = new AtomicBoolean(true);

    @Override
    public String getName() {
        return JokeIdlingResource.class.getSimpleName();
    }

    @Override
    public boolean isIdleNow() {
        return mIsIdle.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        mCallback = callback;
    }

    public void setIdleState(boolean isIdle) {
        mIsIdle.set(isIdle);
        if (mCallback != null && mIsIdle.get()) {
            mCallback.onTransitionToIdle();
        }
    }
}
