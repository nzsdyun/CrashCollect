package com.example.sky.crashcollect;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void Click(View view) {
        int id = view.getId();
        if (id == R.id.create_crash) {
            throw new NullPointerException("this is null pointer exception");
        }

    }
}
