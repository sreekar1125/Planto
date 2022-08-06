package com.dev.planto.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.planto.adapter.RequestAdapter;
import com.dev.planto.Awareness;
import com.dev.planto.model.Requestuser;
import com.dev.planto.R;
import com.dev.planto.SignIn;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class Requests extends AppCompatActivity {

    private RecyclerView rvRequests;
    private ArrayList<Requestuser> r;
    private ArrayList<String> uId;

    private DatabaseReference mDbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        Toolbar toolbar = findViewById(R.id.tool_bar_requests);
        rvRequests = findViewById(R.id.rv_requests);
        FloatingActionButton fab = findViewById(R.id.admin_vid);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.requests));

        String mUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        mDbRef = FirebaseDatabase.getInstance().getReference().child("Admins");

        rvRequests.setHasFixedSize(true);
        rvRequests.setLayoutManager(new LinearLayoutManager(this));
        r = new ArrayList<>();
        uId = new ArrayList<>();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Requests.this, Awareness.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });

    }
    @Override
    public void onStart(){
        super.onStart();

        mDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                username(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Requests.this,"Error occured"+databaseError.getMessage() , Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void username(DataSnapshot dataSnapshot) {
        r.clear();
        for(DataSnapshot dsp : dataSnapshot.child("Requests").getChildren()){
            Requestuser username = dsp.getValue(Requestuser.class);
            uId.add(dsp.getKey());
            r.add(username);
        }

        if (r.isEmpty()){
            rvRequests.setVisibility(View.GONE);
        }else{
            rvRequests.setVisibility(View.VISIBLE);
        }

        RequestAdapter requestAdapter = new RequestAdapter(uId,r,this);
        rvRequests.setAdapter(requestAdapter);

    }


   @Override
   public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_requests,menu);
        return true;
   }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.req_sign_out:
                FirebaseAuth.getInstance().signOut();
                if (FirebaseAuth.getInstance().getCurrentUser() == null){
                    startActivity(new Intent(this, SignIn.class));
                    finish();
                }
            case 0:
                break;
        }

        return true;
    }
}
