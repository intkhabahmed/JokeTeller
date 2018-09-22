package com.udacity.gradle.builditbigger;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.Espresso.onView;

@RunWith(AndroidJUnit4.class)
public class JokeLoadingTest {

    @Rule public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);
    private IdlingResource mIdlingResource;

    @Before
    public void setupResources() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
        if (BuildConfig.APPLICATION_ID.equals(mActivityTestRule.getActivity().getString(R.string.free_app_id))) {
            mActivityTestRule.getActivity().setTestRunning(true);
        }
    }

    @Test
    public void testJokeIsLoadedOrNot() {
        onView(withId(R.id.tell_joke_btn)).perform(click());
        onView(withId(R.id.joke_tv)).check(matches(isDisplayed()));
    }

    @After
    public void tearDownResources() {
        IdlingRegistry.getInstance().unregister(mIdlingResource);
        mIdlingResource = null;
    }
}
