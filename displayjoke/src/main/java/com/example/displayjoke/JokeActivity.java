package com.example.displayjoke;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.displayjoke.databinding.ActivityJokeBinding;

public class JokeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityJokeBinding jokeBinding = DataBindingUtil.setContentView(this, R.layout.activity_joke);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            jokeBinding.jokeTv.setText(intent.getStringExtra(Intent.EXTRA_TEXT));
        }
    }
}
