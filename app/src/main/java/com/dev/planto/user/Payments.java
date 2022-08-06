package com.dev.planto.user;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

import java.util.Objects;


public class Payments extends AppCompatActivity {
    private TextView mMoney;
    private ProgressDialog pd;

   private DatabaseReference mDbRef;
    private static final int STD_MONEY = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);

        mMoney = findViewById(R.id.txt_money);
        Toolbar mToolBar = findViewById(R.id.payment_toolbar);
        LinearLayout layoutWallet = findViewById(R.id.layout_wallet);

        setSupportActionBar(mToolBar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.payment));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AdView adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.BannerAdUnitId));
        adView.setAdSize(AdSize.MEDIUM_RECTANGLE);
        adView = findViewById(R.id.adView_payments);
        adView.loadAd(new AdRequest.Builder().build());
        adView.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded(){
            }
            @Override
            public void onAdFailedToLoad(int errorCode){
                Toast.makeText(Payments.this, getString(errorCode), Toast.LENGTH_SHORT).show();
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


        String mRoot = "Users";
        String mUID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        mDbRef = FirebaseDatabase.getInstance().getReference().child(mRoot).child(mUID);

        pd=new ProgressDialog(this);
        pd.setTitle("Linking");
        pd.setMessage("Please wait..." );
        pd.setCancelable(false);
        Button mlink_button=findViewById((R.id.link_Btn));
        mlink_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                       startActivity(new Intent(Payments.this, LinkPaytm.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    }
                },2000);
            }
        });

        TextView mshowpassbook=findViewById(R.id.passbook);
        mshowpassbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(Payments.this, ShowPassBook.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });

        layoutWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkRequirements();
            }
        });
    }

    @SuppressLint({"ResourceType", "InflateParams"})
    private void checkRequirements() {

        Dialog dialog = new Dialog(this);
        Objects.requireNonNull(dialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialog.getLayoutInflater().inflate(R.layout.layout_popup_wallet,null));
        ImageView imageView = dialog.findViewById(R.id.wallet_redeem_img);
        TextView textView = dialog.findViewById(R.id.wallet_redeem_txt);
        Button btnRedeem = dialog.findViewById(R.id.wallet_btn_redeem);

        if (Integer.parseInt(mMoney.getText().toString()) >= STD_MONEY){
            imageView.setImageResource(R.raw.redeem);
            textView.setText(getString(R.string.redeem_money));
            btnRedeem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(Payments.this, "Redeeming...", Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            imageView.setImageResource(R.raw.delete);
            textView.setText(getString(R.string.not_enough_money));
            btnRedeem.setVisibility(View.GONE);
        }

        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_payment,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(Payments.this,Home.class));
        switch (item.getItemId()){
            case R.id.menu_help:
                startActivity(new Intent(Payments.this, HelpActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                break;
            case 0:
                break;
        }
        return true;
    }

    @Override
    public void onStart(){
        super.onStart();
        mDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mMoney.setText(String.valueOf(dataSnapshot.child("Money").getValue()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();

        startActivity(new Intent(this,Home.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }
}