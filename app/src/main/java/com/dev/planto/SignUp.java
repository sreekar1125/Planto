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
import android.widget.Toast;

import com.dev.planto.admin.Requests;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignUp extends AppCompatActivity {

    private EditText mDisplayName,mEmail,mPass,mCPass;
    private ProgressDialog progressDialog;

    private DatabaseReference mDbRef;
    private FirebaseAuth mAuth;

    private String name;
    private String email;
    private String pass;
    private String cpass;
    private String rootDb="Users";

    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mDisplayName =findViewById(R.id.Sup_name);
        mEmail=findViewById(R.id.Sup_email);
        mPass=findViewById(R.id.et_pass);
        Button msignup = findViewById(R.id.Sup_btn);
        mCPass=findViewById(R.id.et_cpass);

        FirebaseDatabase mDb = FirebaseDatabase.getInstance();
        mDbRef= mDb.getReference().child(rootDb);

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.InterstitialAdUnitId));
        interstitialAd.loadAd(new AdRequest.Builder().build());
        interstitialAd.show();

        interstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded(){
                interstitialAd.show();
            }
            @Override
            public void onAdFailedToLoad(int errorCode){
                Toast.makeText(SignUp.this, getString(errorCode), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdClosed(){
            }
            @Override
            public void onAdClicked(){}
            @Override
            public void onAdLeftApplication(){}
            @Override
            public void onAdOpened(){}
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getString(R.string.sign_up));
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.setCanceledOnTouchOutside(false);


        mAuth = FirebaseAuth.getInstance();

        msignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                 name = mDisplayName.getText().toString().trim();
                //suname.setValue(name);
                 email = mEmail.getText().toString().trim();
                 pass = mPass.getText().toString().trim();cpass=mCPass.getText().toString().trim();


                if (TextUtils.isEmpty(name)|| TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)||TextUtils.isEmpty(cpass)){

                    Toast.makeText(SignUp.this, getString(R.string.enter_all_creds),Toast.LENGTH_LONG).show();

                }else {
                    progressDialog.show();
                    if (email.startsWith("admin") || email.startsWith("Admin")){
                        rootDb = "Admins";
                        mDbRef = FirebaseDatabase.getInstance().getReference().child(rootDb);

                        if (pass.equals(cpass)){
                            signUpAdmin(name,email,pass);
                        }else{
                            Toast.makeText(SignUp.this, getString(R.string.pass_unmatched), Toast.LENGTH_SHORT).show();
                            progressDialog.hide();
                        }

                    }else {

                        signUpUser(name, email, pass, cpass);
                    }
                }
            }



        });
    }

    private void signUpAdmin(final String name, final String email, String pass) {

        mAuth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            progressDialog.dismiss();
                            mDbRef.child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).child("Name").setValue(name);
                            mDbRef.child(mAuth.getCurrentUser().getUid()).child("Email").setValue(email);
                            startActivity(new Intent(SignUp.this, Requests.class));
                            finish();
                        }else{
                            progressDialog.hide();
                            Toast.makeText(SignUp.this, (CharSequence) task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void signUpUser(final String name, final String email, String pass, String cpass) {

        if (pass.equals(cpass)) {

            mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Objects.requireNonNull(mAuth.getCurrentUser()).sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                           public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()){
                                    progressDialog.dismiss();
                                    Toast.makeText(SignUp.this, getString(R.string.check4_email_verif),Toast.LENGTH_LONG).show();

                                    mDbRef.child(mAuth.getCurrentUser().getUid()).child("Name").setValue(name);
                                    mDbRef.child(mAuth.getCurrentUser().getUid()).child("Email").setValue(email);
                                    mDbRef.child(mAuth.getCurrentUser().getUid()).child("DpLink").setValue("");
                                    mDbRef.child(mAuth.getCurrentUser().getUid()).child("DOB").setValue("");
                                    mDbRef.child(mAuth.getCurrentUser().getUid()).child("Gender").setValue("Gender");
                                    mDbRef.child(mAuth.getCurrentUser().getUid()).child("Points").setValue("0");
                                    mDbRef.child(mAuth.getCurrentUser().getUid()).child("Money").setValue("0");

                                    startActivity(new Intent(SignUp.this,SignIn.class));
                                    finish();
                                }
                                else{
                                    progressDialog.hide();
                                    Toast.makeText(SignUp.this,"Enter valid Email address only",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                   }
                }
            });

        }
        else {
               Toast.makeText(SignUp.this,getString(R.string.invalid_email_pass),Toast.LENGTH_LONG).show();

    }

}

    @Override
    public void onBackPressed(){
        super.onBackPressed();

        startActivity(new Intent(this,SignIn.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }
}
