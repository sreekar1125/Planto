package com.dev.planto.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.planto.adapter.VerifListAdapter;
import com.dev.planto.help.HelpActivity;
import com.dev.planto.model.VerifList;
import com.dev.planto.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


public class Verification extends AppCompatActivity {

    private RecyclerView verifItemsList;
    private ArrayList<VerifList> verifList;

    private DatabaseReference mDbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        //Firebase Initialization
        //Firebase Reference variables
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase mDb = FirebaseDatabase.getInstance();
        String cUid = mAuth.getUid();
        String rootNode = "Users";
        if (cUid != null) {
            mDbRef = mDb.getReference().child(rootNode).child(cUid);
        }

        //Views initialization
        //Views Reference variables
        Toolbar toolbar = findViewById(R.id.tool_bar_verif);
        verifItemsList =  findViewById(R.id.verif_list);

        verifItemsList.setHasFixedSize(true);
        verifItemsList.setLayoutManager(new LinearLayoutManager(this));
        verifList = new ArrayList<>();

        //ToolBar setup
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.verification));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // Inflate or Show Options Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
       getMenuInflater().inflate(R.menu.menu_verification,menu);
       return true;
    }

    //Action from selected Options in Menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        startActivity(new Intent(Verification.this,Home.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
        switch (item.getItemId()){
            //Help option
            case R.id.menu_help:
                startActivity(new Intent(this, HelpActivity.class));
                break;
            case 0:
                break;

        }
        return true;
    }

    //  Automatic Operation when the Activity onStarted
    @Override
    public void onStart(){
        super.onStart();

        mDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               verifList(dataSnapshot);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Verification.this,databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Verification","Error");
            }
        });
    }
            private void verifList(DataSnapshot dataSnapshot) {
                verifList.clear();
                String verifNode = "VerifStatus";
                for (DataSnapshot dsp : dataSnapshot.child(verifNode).getChildren()) {
                        VerifList list = dsp.getValue(VerifList.class);
                        verifList.add(list);
                }
                VerifListAdapter verifListAdapter = new VerifListAdapter(verifList,this);
                verifItemsList.setAdapter(verifListAdapter);
                verifItemsList.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
            }

    @Override
    public void onBackPressed(){
        super.onBackPressed();

        startActivity(new Intent(this,Home.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }
}