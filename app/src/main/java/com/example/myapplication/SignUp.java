package com.example.myapplication;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    private EditText mdisplayname,memail,mpass,mcpass;
    private Button msignup;
    private FirebaseDatabase nsu=FirebaseDatabase.getInstance();
    private DatabaseReference sname=nsu.getReference().push();
    private DatabaseReference suname=sname.child("Name");
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mdisplayname=(EditText) findViewById(R.id.Sup_name);
        memail=(EditText)findViewById(R.id.Sup_email);
        mpass=(EditText)findViewById(R.id.et_pass);
        msignup=(Button)findViewById(R.id.Sup_btn);
        mcpass=(EditText)findViewById(R.id.et_cpass);


        mAuth = FirebaseAuth.getInstance();

        msignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String name = mdisplayname.getText().toString().trim();
                suname.setValue(name);
                String email = memail.getText().toString().trim();
                String pass = mpass.getText().toString().trim();
                String cpass=mcpass.getText().toString().trim();


                if (TextUtils.isEmpty(name)|| TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)||TextUtils.isEmpty(cpass)){

                    Toast.makeText(SignUp.this,"please enter all the credentials",Toast.LENGTH_LONG).show();

                }else {

                    Signuup(name,email, pass,cpass);
                }
                //Signuup(name,email,pass);


            }



        });
    }



    private void Signuup(String name,String email,String pass,String cpass) {

        if (pass.equals(cpass)) {

            mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                   // Toast.makeText(SignUp.this,"Check Your Email For ",Toast.LENGTH_LONG).show();
                    if (task.isSuccessful()) {
                     //   Toast.makeText(SignUp.this,"Check Your Email",Toast.LENGTH_LONG).show();
                        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                           public void onComplete(@NonNull Task<Void> task) {
                              //  Toast.makeText(SignUp.this,"Check Your  For Verification",Toast.LENGTH_LONG).show();

                                if (task.isSuccessful()){
                                    //startActivity(new Intent(SignUp.this,Home.class));
                                    Toast.makeText(SignUp.this,"Check Your Email For Verification",Toast.LENGTH_LONG).show();
                                    finish();
                                }
                                else{
                                    Toast.makeText(SignUp.this,"Enter valid Email address only",Toast.LENGTH_LONG).show();
                                }


                            }
                        });

                   }

                    /*else {
                        Toast.makeText(SignUp.this, "Signup failed", Toast.LENGTH_LONG).show();
                         }*/

                }
            });


        }
        else {
               Toast.makeText(SignUp.this,"enter valid password",Toast.LENGTH_LONG).show();

    }

}}
