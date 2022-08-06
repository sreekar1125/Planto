package com.dev.planto.user;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.dev.planto.R;

public class LinkPaytm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_paytm);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();

        startActivity(new Intent(this,Payments.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }
}
