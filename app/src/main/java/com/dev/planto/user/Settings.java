package com.dev.planto.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.dev.planto.bottomsheet.EditLangBottomSheet;
import com.dev.planto.help.HelpActivity;
import com.dev.planto.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class Settings extends AppCompatActivity{
    private ImageView dp;
    private TextView sett_email;

    private DatabaseReference mDbRef;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.tool_bar_settings);
        LinearLayout layout_about = findViewById(R.id.settings_about);
        sett_email = findViewById(R.id.txt_settings_email);
        LinearLayout layout_profile = findViewById(R.id.settings_layout_prof);
        dp = findViewById(R.id.sett_dp);
        LinearLayout layout_lang = findViewById(R.id.settings_layout_lang);

        adView = new AdView(this);
        adView = findViewById(R.id.adView_settings);
        adView.loadAd(new AdRequest.Builder().build());
        adView.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded(){
            }
            @Override
            public void onAdFailedToLoad(int errorCode){
                Toast.makeText(Settings.this, getString(errorCode), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onAdOpened(){
            }
            @Override
            public void onAdClicked(){
            }
            @Override
            public void onAdLeftApplication(){
            }
            @Override
            public void onAdClosed(){
            }
        });

        mDbRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.settings));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        layout_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.this,About.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });

        layout_lang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditLangBottomSheet editLangBottomSheet = new EditLangBottomSheet();
                editLangBottomSheet.show(getSupportFragmentManager(),"Language BottomSheet");
            }
        });

        layout_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.this,Profile.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });

        setLayout();
    }

    private void setLayout() {

        mDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sett_email.setText(String.valueOf(dataSnapshot.child("Email").getValue()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Settings.this,databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        mDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String dpLink = String.valueOf(dataSnapshot.child("DpLink").getValue());

                if (dpLink.isEmpty() || !(dataSnapshot.hasChild("DpLink"))){
                    dp.setImageDrawable(getResources().getDrawable(R.drawable.user));
                }else{
                    Picasso.get().load(dpLink).into(dp);
                    /*Glide.with(Home.this)
                            .load(dpLink)
                            .into(dp);*/
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_settings,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        startActivity(new Intent(Settings.this,Home.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));

        switch (item.getItemId()){
            case R.id.settings_help:
                startActivity(new Intent(Settings.this, HelpActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                break;

            case 0:
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        startActivity(new Intent(this,Home.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }
}
