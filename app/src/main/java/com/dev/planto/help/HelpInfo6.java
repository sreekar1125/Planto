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

import java.util.Objects;

public class HelpInfo6 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpinfo6);
        Toolbar toolbar =  findViewById(R.id.tool_bar);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.help));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView mhtxt1 = findViewById(R.id.htxt_1);
        TextView mhtxt2 = findViewById(R.id.htxt_2);
        mhtxt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HelpInfo6.this, HelpInfo7.class));
            }
        });
        mhtxt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              startActivity(new Intent(HelpInfo6.this,HelpInfo8.class));
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        startActivity(new Intent(this, HelpActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));

        return true;
    }
}
