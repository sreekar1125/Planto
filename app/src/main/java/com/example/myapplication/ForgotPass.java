package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.OnClickAction;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPass extends AppCompatActivity {
    private EditText passemail;
    private Button  mSendOTP;
    private FirebaseAuth Auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        passemail=(EditText) findViewById(R.id.request_textview);
        mSendOTP = (Button) findViewById(R.id.request_otp);
        Auth=FirebaseAuth.getInstance();
        mSendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String useremail=passemail.getText().toString().trim();


                if(useremail.equals("")){

                    Toast.makeText(ForgotPass.this,"Please enter your email ID",Toast.LENGTH_LONG).show();
                }
                else {
                    Auth.sendPasswordResetEmail(useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                Toast.makeText(ForgotPass.this,"Reset password email sent",Toast.LENGTH_LONG).show();
                                finish();
                                startActivity(new Intent(ForgotPass.this,LoginActivity.class));
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
}
