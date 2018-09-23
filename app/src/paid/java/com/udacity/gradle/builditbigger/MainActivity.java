package com.udacity.gradle.builditbigger;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.displayjoke.JokeActivity;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;
import com.udacity.gradle.builditbigger.utils.JokeIdlingResource;

import java.io.IOException;

import javax.annotation.Nullable;


public class MainActivity extends AppCompatActivity {

    @Nullable
    private JokeIdlingResource mJokeIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        ImageView mouth = findViewById(R.id.mouth_iv);
        mouth.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.smile));
        findViewById(R.id.loading_pb).setVisibility(View.VISIBLE);
        new EndpointsAsyncTask().execute();

    }

    @SuppressLint("StaticFieldLeak")
    class EndpointsAsyncTask extends AsyncTask<Void, Void, String> {
        private MyApi myApiService = null;

        @Override
        protected final String doInBackground(Void... params) {
            if (mJokeIdlingResource != null) {
                mJokeIdlingResource.setIdleState(false);
            }
            if (myApiService == null) {  // Only do this once
                MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        // options for running against local devappserver
                        // - 10.0.2.2 is localhost's IP address in Android emulator
                        // - turn off compression when running against local devappserver
                        .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                            @Override
                            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) {
                                abstractGoogleClientRequest.setDisableGZipContent(true);
                            }
                        });
                // end options for devappserver

                myApiService = builder.build();
            }

            try {
                return myApiService.retrieveJoke().execute().getData();
            } catch (IOException e) {
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
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
