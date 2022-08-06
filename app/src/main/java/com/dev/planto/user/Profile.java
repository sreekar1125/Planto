package com.dev.planto.user;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.dev.planto.bottomsheet.EditEmailBottomSheet;
import com.dev.planto.bottomsheet.EditNameBottomSheet;
import com.dev.planto.bottomsheet.GenderBottomSheet;
import com.dev.planto.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.Objects;

public class Profile extends AppCompatActivity {
    private ImageView mUserDp,badge;
    private TextView profEmail,profGender,profdob,profName;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private FirebaseUser mUser;
    private DatabaseReference mDbRef;
    private StorageReference storageReference;

    private String dpLink,mUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        LinearLayout gender,email,name;

        Toolbar toolbar = findViewById(R.id.tool_bar_profile);
        mUserDp = findViewById(R.id.user_dp);
        profEmail = findViewById(R.id.prof_email);
        gender = findViewById(R.id.layout_gender);
        email = findViewById(R.id.layout_email);
        name = findViewById(R.id.layout_name);
        profGender = findViewById(R.id.prof_gender);
        profdob=findViewById(R.id.prof_Dob);
        profName = findViewById(R.id.prof_name);
        badge = findViewById(R.id.profile_tb_badge);


        mDbRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        storageReference = FirebaseStorage.getInstance().getReference();

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        setToolBar();

        mUserDp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GenderBottomSheet bottomSheet = new GenderBottomSheet();
                bottomSheet.show(getSupportFragmentManager(),"GenderBottomSheet");
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditEmailBottomSheet emailBottomSheet = new EditEmailBottomSheet();
                emailBottomSheet.show(getSupportFragmentManager(),"EmailBottomSheet");
            }
        });

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditNameBottomSheet nameBottomSheet = new EditNameBottomSheet();
                nameBottomSheet.show(getSupportFragmentManager(),"NameBottomSheet");
            }
        });
        profdob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c= Calendar.getInstance();
                int year=c.get(Calendar.YEAR);
                final int month=c.get(Calendar.MONTH);
                final int day = c.get(Calendar.DAY_OF_MONTH);

                final DatePickerDialog dialog=new DatePickerDialog(Profile.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,mDateSetListener,year,month, day);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.show();

            }
        });
        mDateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month=month+1;
                String date=day+"/"+month+"/"+year;
                mDbRef.child("DOB").setValue(date);
                profdob.setText(date);
                Toast.makeText(Profile.this,date+ " updated", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void selectImage() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            int DP_PICK = 1111;
            startActivityForResult(Intent.createChooser(intent,"Select Image"), DP_PICK);
        }else{
            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);

            this.selectImage();
        }
    }


    @SuppressLint("ResourceType")
    @Override
    protected void onActivityResult(int resultCode, int requestCode, final Intent data){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIcon(R.raw.planto);
        progressDialog.setTitle("Profile Picture Update");
        progressDialog.setMessage("Updating");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(true);
        progressDialog.show();

        Uri filePath = data.getData();

            if (filePath != null){

                final StorageReference childRef = storageReference.child("Dp").child(mUserName + ".jpg");
                childRef.putFile(filePath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        progressDialog.dismiss();
                                        mDbRef.child("DpLink").setValue(uri.toString());
                                        Toast.makeText(Profile.this,"Profile picture updated", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(Profile.this,"Profile picture update failed", Toast.LENGTH_SHORT).show();
                            }
                        })

                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                                int progress = (int) ((100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
                                progressDialog.setMessage("Updating  " + progress + "%");
                            }
                        });


            }

        super.onActivityResult(requestCode,resultCode,data);
    }

    private void setToolBar() {

        mDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUser = FirebaseAuth.getInstance().getCurrentUser();
                if (mUser != null) {
                    if (mUser.isEmailVerified()) {
                        Objects.requireNonNull(getSupportActionBar()).setTitle(String.valueOf(dataSnapshot.child("Name").getValue()));
                    }else{
                        badge.setVisibility(View.INVISIBLE);
                        Objects.requireNonNull(getSupportActionBar()).setTitle(String.valueOf(dataSnapshot.child("Name").getValue()));
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Profile.this,databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        startActivity(new Intent(this,Settings.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));

        return  true;
    }

    @Override
    public void onStart(){
        super.onStart();

        mDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mUserName = String.valueOf(dataSnapshot.child("Name").getValue());
                dpLink = String.valueOf(dataSnapshot.child("DpLink").getValue());
                profEmail.setText(String.valueOf(dataSnapshot.child("Email").getValue()));
                profGender.setText(String.valueOf(dataSnapshot.child("Gender").getValue()));
                profdob.setText(String.valueOf(dataSnapshot.child("DOB").getValue()));
                profName.setText(String.valueOf(dataSnapshot.child("Name").getValue()));

                if (dpLink.isEmpty() || !(dataSnapshot.hasChild("DpLink") || TextUtils.equals(dpLink,"null"))){
                    mUserDp.setImageDrawable(getResources().getDrawable(R.drawable.user));
                }else{
                    Glide.with(Profile.this)
                            .load(dpLink)
                            .into(mUserDp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Profile.this,databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();

        startActivity(new Intent(this,Settings.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }
}