package com.udacity.gradle.builditbigger;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.displayjoke.JokeActivity;
import com.udacity.gradle.builditbigger.utils.JokeAsyncTask;
import com.udacity.gradle.builditbigger.utils.JokeIdlingResource;
import com.udacity.gradle.builditbigger.utils.OnEventListener;

import javax.annotation.Nullable;


public class MainActivity extends AppCompatActivity {

    @Nullable
    private JokeIdlingResource mJokeIdlingResource;
    private ImageView mouth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mouth = findViewById(R.id.mouth_iv);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mouth.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.frown));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void tellJoke(View view) {
        mouth.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.smile));
        findViewById(R.id.loading_pb).setVisibility(View.VISIBLE);
        JokeAsyncTask jokeAsyncTask = new JokeAsyncTask(new OnEventListener<String>() {
            @Override
            public void onSuccess(String result) {
                if (mJokeIdlingResource != null) {
                    mJokeIdlingResource.setIdleState(true);
                }
                if (!result.isEmpty()) {
                    findViewById(R.id.loading_pb).setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(MainActivity.this, JokeActivity.class);
                    intent.putExtra(Intent.EXTRA_TEXT, result);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        if (mJokeIdlingResource != null) {
            mJokeIdlingResource.setIdleState(false);
        }
        jokeAsyncTask.execute();
    }

    @VisibleForTesting
    public void setTestRunning(boolean isTest) {
        //Nothing to do in Paid Version
    }

    @VisibleForTesting
    public IdlingResource getIdlingResource() {
        if (mJokeIdlingResource == null) {
            mJokeIdlingResource = new JokeIdlingResource();
        }
        return mJokeIdlingResource;
    }
}
