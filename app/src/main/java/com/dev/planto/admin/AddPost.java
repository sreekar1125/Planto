package com.dev.planto.admin;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dev.planto.Awareness;
import com.dev.planto.model.ImageUploadInfo;
import com.dev.planto.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;
import java.util.UUID;

public class AddPost extends AppCompatActivity {

    ImageView mPostIv;
    Button mUploadBtn;

    String mStoragePath="Awareness/";
    String mDatabasePath="Awareness";

    Uri mFilePathUri;

    StorageReference mStorageReference;
    DatabaseReference mDatabaseReference,mDbRef;

    ProgressDialog mProgressDialog;

    int IMAGE_REQUEST_CODE=5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        mPostIv = findViewById(R.id.pImageIv);
        mUploadBtn = findViewById(R.id.pUploadBtn);


        mPostIv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent=new Intent();

                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"select image"),IMAGE_REQUEST_CODE);

            }
        });
        mUploadBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UploadDataToFirebase();

            }
        });

        mStorageReference= FirebaseStorage.getInstance().getReference();
        mDbRef = FirebaseDatabase.getInstance().getReference();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());

        mProgressDialog=new ProgressDialog(AddPost.this);
    }

    private void UploadDataToFirebase(){

        if(mFilePathUri!=null){
            mProgressDialog.setTitle("Image is uploading");
            mProgressDialog.setMessage("Uploading");
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();

             final StorageReference storageReference2nd= mStorageReference.child(mStoragePath).child("Planto-"+UUID.randomUUID() + "." + getFileExtension(mFilePathUri));
            storageReference2nd.putFile(mFilePathUri)
            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){

                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot){
                    mProgressDialog.dismiss();

                    Toast.makeText(AddPost.this,"Image Uploaded...",Toast.LENGTH_SHORT).show();
                    storageReference2nd.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            mDbRef.child(mDatabasePath).child(Objects.requireNonNull(mDbRef.push().getKey())).child("image").setValue(uri.toString());
                        }
                    });

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mProgressDialog.dismiss();
                            Toast.makeText(AddPost.this,e.getMessage(),Toast.LENGTH_SHORT).show();


                        }
                    });
        }
        else {
            Toast.makeText(this,"Please select image",Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap= MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mFilePathUri=data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mFilePathUri);
                mPostIv.setImageBitmap(bitmap);
            }
            catch (Exception e){
                Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();

        startActivity(new Intent(this, Awareness.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }

}