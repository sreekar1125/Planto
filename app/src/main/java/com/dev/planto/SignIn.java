package com.dev.planto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.planto.admin.Requests;
import com.dev.planto.user.Home;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignIn extends AppCompatActivity {
    private EditText mEmail,mPass;
    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;
    private static boolean callAlready = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        TextView mSignUp =  findViewById(R.id.id_sign_up);
        TextView mFp =  findViewById(R.id.id_forgot_pass);
        Button mSignIn =  findViewById(R.id.btn_sign_in);
        mPass= findViewById(R.id.et_pass);
        mEmail= findViewById(R.id.et_email);

        if (!callAlready) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            callAlready =true;
        }

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAuth = FirebaseAuth.getInstance();

        final AdView mAdView;
        mAdView = findViewById(R.id.adView);
        final AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded(){
                mAdView.loadAd(adRequest);
            }
            @Override
            public void onAdFailedToLoad(int errorCode){
                Toast.makeText(SignIn.this, getString(errorCode), Toast.LENGTH_SHORT).show();
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

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getString(R.string.sign_in));
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.setCanceledOnTouchOutside(false);


       mSignUp.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               startActivity(new Intent(SignIn.this,SignUp.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
           }
       });


        mFp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignIn.this,ForgotPass.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });

        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();
                String pass = mPass.getText().toString().trim();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)){
                    Toast.makeText(SignIn.this,getString( R.string.enter_email_pass),Toast.LENGTH_SHORT).show();

                }else {
                    progressDialog.show();
                    if (email.startsWith("admin") || email.startsWith("Admin")){
                        signInAdmin(email, pass);
                    }else{
                        signInUser(email,pass);
                    }
                }
            }
        });

    }

    private void signInAdmin(String email, String pass) {
        mAuth.signInWithEmailAndPassword(email,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            progressDialog.dismiss();
                            startActivity(new Intent(SignIn.this, Requests.class));
                            finish();

                        }else{
                            progressDialog.hide();
                            Toast.makeText(SignIn.this, String.valueOf(task.getException()), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void signInUser(String email, String pass) {

        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            checkEmailVerification();
                        }else{
                            progressDialog.hide();
                            Toast.makeText(SignIn.this, getString(R.string.invalid_email_pass),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void checkEmailVerification(){

        FirebaseUser firebaseUser =FirebaseAuth.getInstance().getCurrentUser();
        boolean emailflag = false;
        if (firebaseUser != null) {
            emailflag = firebaseUser.isEmailVerified();
        }

        if(emailflag){
            startActivity(new Intent(SignIn.this, Home.class));
            finish();
        }
        else{
            Toast.makeText(SignIn.this, R.string.verify_email,Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onStart(){
        super.onStart();

        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        if ( (FirebaseAuth.getInstance().getCurrentUser() != null)  && !(Objects.requireNonNull(Objects.requireNonNull(mUser).getEmail()).startsWith("admin"))){
            startActivity(new Intent(this, Home.class));
            finish();
        }else if (mUser != null && (FirebaseAuth.getInstance().getCurrentUser() != null) && (Objects.requireNonNull(mUser.getEmail()).startsWith("admin"))) {
            startActivity(new Intent(this, Requests.class));
            finish();
        }
    }
}