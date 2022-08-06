package com.dev.planto.bottomsheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dev.planto.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class EditEmailBottomSheet extends BottomSheetDialogFragment {

    private EditText email;

    private DatabaseReference mDbRef;
    private String newEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.edit_email_bottom_sheet,container,false);

        email = v.findViewById(R.id.bs_email);
        Button save = v.findViewById(R.id.save_email);

        mDbRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());

        mDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                email.setText(String.valueOf(dataSnapshot.child("Email").getValue()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    newEmail = email.getText().toString();
                    if (newEmail.contains("@")  && newEmail.contains(".")) {
                        mDbRef.child("Email").setValue(newEmail);
                        Toast.makeText(getContext(), newEmail + " Updated", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }else{
                        email.getError();
                    }
                }

        });
        return v;
    }
}
