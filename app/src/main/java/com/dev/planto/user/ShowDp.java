package com.dev.planto.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.dev.planto.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class ShowDp extends AppCompatActivity {

    private DatabaseReference mDbRef;

    private String mUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_dp);

        Toolbar toolbar = findViewById(R.id.tool_bar_show_dp);
        ImageView dp = findViewById(R.id.user_dp);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mDbRef = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());


       /* mDbRef.child("DPLink")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                       *//*String link = dataSnapshot.getValue().toString();

                        Picasso.get().load(link).into(dp);*//*
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){



        getMenuInflater().inflate(R.menu.menu_show_dp,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        startActivity(new Intent(ShowDp.this,Profile.class)/*.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)*/);
        switch (item.getItemId()){
            case R.id.edit_dp:
                editDp();
                break;
            case 0:
                break;
        }


        return true;
    }

    private void editDp() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        int DP_PICK = 111;
        startActivityForResult(Intent.createChooser(intent,"Select Image"), DP_PICK);
    }


    @Override
    protected void onActivityResult(int resultCode, int requestCode, final Intent data) {
        // if (requestCode == DP_PICK && resultCode == RESULT_OK){

        super.onActivityResult(resultCode, requestCode, data);
        Uri filePath = data.getData();

        mDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUserName = String.valueOf(dataSnapshot.child("Name").getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        final StorageReference childRef = storageReference.child("Dp").child(mUserName + ".jpg");


            /*if (filePath != null){

                childRef.putFile(filePath);

                childRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        String dpLink = String.valueOf(uri);
                        mDbRef.child("DpLink").setValue(dpLink);

                        Toast t = Toast.makeText(ShowDp.this,"Profile Picture updated",Toast.LENGTH_SHORT);
                        t.setGravity(Gravity.CENTER,0,0);
                        t.show();


                    }
                });*/

       /* Task uploadTask = childRef.putFile(filePath);
        final String[] link = new String[1];
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return childRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();

                    link[0] = String.valueOf(downloadUri);

                    Picasso.get().load(String.valueOf(link)).into(dp);

                } else {
                    // Handle failures
                    // ...


                }
            }
        });*/


    }


    }

