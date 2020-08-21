package com.application.recylerviewdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.application.recylerview_lib.RecylerViewDemoActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView jumpToRecylerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        jumpToRecylerView = findViewById(R.id.jumo_to_recylerview);
        jumpToRecylerView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Intent intent = new Intent();
        if (id == R.id.jumo_to_recylerview) {
            intent.setClass(this, RecylerViewDemoActivity.class);
            startActivity(intent);
        }
    }
}