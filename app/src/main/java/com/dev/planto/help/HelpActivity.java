package com.dev.planto.help;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.dev.planto.R;
import com.dev.planto.user.Settings;

import java.util.Objects;

public class HelpActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        TextView mhtxt1,mhtxt2,mhtxt3,mhtxt4,mhtxt5,mhtxt6;

        Toolbar toolbar =  findViewById(R.id.tool_bar);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.help));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mhtxt1=findViewById(R.id.htxt_1);
        mhtxt2=findViewById(R.id.htxt_2);
        mhtxt3=findViewById(R.id.htxt_3);
        mhtxt4=findViewById(R.id.htxt_4);
        mhtxt5=findViewById(R.id.htxt_5);
        mhtxt6=findViewById(R.id.htxt_6);
        mhtxt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HelpActivity.this, HelpInfo1.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });

        mhtxt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HelpActivity.this, HelpInfo3.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });
        mhtxt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HelpActivity.this, HelpInfo6.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });
        mhtxt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HelpActivity.this, HelpInfo9.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });
        mhtxt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HelpActivity.this, HelpInfo11.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });
        mhtxt6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HelpActivity.this, HelpInfo13.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        startActivity(new Intent(this, Settings.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));

        return true;
    }
}