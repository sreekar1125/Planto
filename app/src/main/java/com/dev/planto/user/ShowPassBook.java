package com.dev.planto.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.planto.adapter.PassbookAdapter;
import com.dev.planto.model.Transaction;
import com.dev.planto.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class ShowPassBook extends AppCompatActivity {

        private RecyclerView recyclerView;

    private ArrayList<Transaction> transHistories;

        private DatabaseReference mDbRef;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_show_passbook);

            recyclerView = findViewById(R.id.passbook_recyclic);
            Toolbar mToolBar = findViewById(R.id.show_passbook_tb);
            setSupportActionBar(mToolBar);
            Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.passbook));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            String mUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
            mDbRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mUid);

            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            transHistories = new ArrayList<>();
        }

        @Override
        public void onStart(){
            super.onStart();

            mDbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    showHistory(dataSnapshot);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ShowPassBook.this,"Error occured"+databaseError.getMessage() , Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void showHistory(DataSnapshot dataSnapshot) {

            transHistories.clear();

            for(DataSnapshot dsp : dataSnapshot.child("Passbook").getChildren()){
                Transaction history = dsp.getValue(Transaction.class);
                transHistories.add(history);
            }

            PassbookAdapter passbookAdapter = new PassbookAdapter(transHistories,this);
            recyclerView.setAdapter(passbookAdapter);
            recyclerView.addItemDecoration(new DividerItemDecoration(this,1));
        }
        @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            startActivity(new Intent(ShowPassBook.this, Payments.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            return true;
        }

    @Override
    public void onBackPressed(){
        super.onBackPressed();

        startActivity(new Intent(this,Payments.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }
}