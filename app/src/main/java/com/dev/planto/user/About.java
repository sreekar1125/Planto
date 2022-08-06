package com.dev.planto.user;

import android.content.Intent;
import android.os.Bundle;

import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.dev.planto.R;

import java.util.Objects;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar;
        toolbar = findViewById(R.id.tool_bar_about);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){

        startActivity(new Intent(About.this,Settings.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
        return true;
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();

        startActivity(new Intent(this,Settings.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }
}
