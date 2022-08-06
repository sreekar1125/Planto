package com.dev.planto.bottomsheet;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dev.planto.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class GenderBottomSheet  extends BottomSheetDialogFragment {

   // private BottomSheetListener mListener;
    private String gender;



    private DatabaseReference mDbRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_layout,container,false);

        mDbRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());

        TextView male,female,other;
         male = v.findViewById(R.id.male);
         female = v.findViewById(R.id.female);
         other = v.findViewById(R.id.other);

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gender = "Male";

                mDbRef.child("Gender").setValue(gender);

                Toast.makeText(getContext(),"Gender "+gender, Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gender = "Female";

                mDbRef.child("Gender").setValue(gender);
                Toast.makeText(getContext(),"Gender "+gender, Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gender = "Other";

                mDbRef.child("Gender").setValue(gender);
                Toast.makeText(getContext(),"Gender "+gender, Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
        return v;
    }

}