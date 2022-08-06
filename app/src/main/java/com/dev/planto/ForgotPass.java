package com.dev.planto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPass extends AppCompatActivity implements RewardedVideoAdListener {
    private RewardedVideoAd rewardedVideoAd;

    private EditText passemail;
    private FirebaseAuth Auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        passemail= findViewById(R.id.request_textview);
        Button mSendOTP =  findViewById(R.id.request_otp);
        Auth=FirebaseAuth.getInstance();

        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        rewardedVideoAd.setRewardedVideoAdListener(this);
        rewardedVideoAd.loadAd(getString(R.string.RewardedAdUnitId),new AdRequest.Builder().build());

        mSendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String useremail=passemail.getText().toString().trim();

                if(useremail.equals("")){

                    Toast.makeText(ForgotPass.this,getString(R.string.enter_all_creds),Toast.LENGTH_LONG).show();
                }
                else {
                    Auth.sendPasswordResetEmail(useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                Toast.makeText(ForgotPass.this, getString(R.string.reset_link_sent),Toast.LENGTH_LONG).show();
                                finish();
                                startActivity(new Intent(ForgotPass.this, SignIn.class));
                            }
                            else{
                                Toast.makeText(ForgotPass.this,"error sending reset password email",Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                }

            }
        });

    }

    @Override
    public void onRewardedVideoAdLoaded() {
            rewardedVideoAd.show();
    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {

    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        Toast.makeText(this, "onRewarded! currency: " + rewardItem.getType() + "  amount: " +
                rewardItem.getAmount(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    @Override
    public void onRewardedVideoCompleted() {

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();

        startActivity(new Intent(this,SignIn.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }
}