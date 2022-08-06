package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {


    private TextView mSignUp, mFp;
    private Button mSignIn;
    private EditText mEmail,mPass;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mSignUp = (TextView) findViewById(R.id.id_sign_up);
        mFp= (TextView) findViewById(R.id.id_forgot_pass);
        mSignIn=(Button) findViewById(R.id.btn_sign_in);
        mPass=(EditText) findViewById(R.id.et_pass);
        mEmail=(EditText) findViewById(R.id.et_email);


        mAuth = FirebaseAuth.getInstance();


       mSignUp.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               startActivity(new Intent(LoginActivity.this,SignUp.class));
           }
       });


        mFp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,ForgotPass.class));
            }
        });

        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();
                String pass = mPass.getText().toString().trim();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)){
                    Toast.makeText(LoginActivity.this,"Please enter email/password",Toast.LENGTH_SHORT).show();

                }else {

                    signIn(email, pass);
                }



            }
        });


    }





    private void signIn(String email, String pass) {

        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                           // startActivity(new Intent(LoginActivity.this, Home.class));
                            checkEmailVerification();
                            finish();
                        }else{
                            Toast.makeText(LoginActivity.this,"Invalid Email/Password",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void checkEmailVerification(){

        FirebaseUser firebaseUser =FirebaseAuth.getInstance().getCurrentUser();
        Boolean emailflag=firebaseUser.isEmailVerified();

        if(emailflag){
            startActivity(new Intent(LoginActivity.this,Home.class));
        }
        else{
            Toast.makeText(LoginActivity.this,"Please verify your email",Toast.LENGTH_LONG).show();
            mAuth.signOut();
            // FirebaseAuth.signOut();
        }

    }

    @Override
    public void onStart(){
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity(new Intent(this,Home.class));
            finish();
        }
    }

}