package com.dev.planto.bottomsheet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dev.planto.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class EditDPBottomSheet extends BottomSheetDialogFragment {


    private DatabaseReference mDbRef;
    private StorageReference mStrRef;
    private static int CAM_REQ = 111,GALLERY_PICK =222;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_dp_select,container,false);

        LinearLayout gallery,camera,removeDp;
        gallery = v.findViewById(R.id.layout_gallery);
        camera = v.findViewById(R.id.layout_camera);
        removeDp = v.findViewById(R.id.layout_remove_dp);

        mDbRef = FirebaseDatabase.getInstance().getReference().child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        mStrRef = FirebaseStorage.getInstance().getReference().child("Dp/");

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                galleryIntent.setType("image/*");
                startActivityForResult(Intent.createChooser(galleryIntent,"Select Image"),GALLERY_PICK);

                dismiss();
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camIntent,CAM_REQ);

                dismiss();
            }
        });

        removeDp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final StorageReference storageReference = mStrRef.child(mDbRef.child("Name").toString());
                Uri uri = Uri.parse(String.valueOf(getResources().getDrawable(R.drawable.user)));
                storageReference.putFile(uri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        mDbRef.child("DpLink").setValue(uri.toString());
                                    }
                                });
                            }
                        });
                dismiss();
            }
        });


        return v;
    }

    @Override
    public void onActivityResult(int resultCode, int requestCode, Intent data){

        super.onActivityResult(requestCode,resultCode,data);

            Uri filePath = data.getData();
        Toast.makeText(getContext(), "dkbfsdk", Toast.LENGTH_SHORT).show();
            if (filePath != null){

                final StorageReference storageReference = mStrRef.child(mDbRef.child("Name").toString());
                storageReference.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                mDbRef.child("DpLink").setValue(uri.toString());
                            }
                        });
                    }
                });
            }

    }
}
