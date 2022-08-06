package com.dev.planto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.dev.planto.adapter.AwarenessAdapter;
import com.dev.planto.admin.AddPost;
import com.dev.planto.admin.Requests;
import com.dev.planto.model.Model;
import com.dev.planto.user.Home;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class Awareness extends AppCompatActivity {
    RecyclerView mRecyclerView;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;
    ImageButton msharebtn;
    ImageView mImageView;
    String image;

    MenuItem menuItem;

    private ArrayList<Model> awarImgs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_awareness);

        Toolbar toolbar = findViewById(R.id.tool_bar_awareness);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.awareness));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mImageView=findViewById(R.id.rImageView);
        msharebtn=findViewById(R.id.sharebtn);

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference();

        awarImgs = new ArrayList<>();

    }


    protected void onStart() {
        super.onStart();

       mRef.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               awarImgs.clear();
               for (DataSnapshot dsp :dataSnapshot.child("Awareness").getChildren()){
                   Model model = dsp.getValue(Model.class);
                   awarImgs.add(model);
                   image = String.valueOf(dsp.getValue());
               }
               if (awarImgs.isEmpty()){
                   mRecyclerView.setVisibility(View.GONE);
               }

               AwarenessAdapter awarenessAdapter = new AwarenessAdapter(Awareness.this,awarImgs);
               mRecyclerView.setAdapter(awarenessAdapter);
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_awareness, menu);
        if (!(Objects.requireNonNull(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail()).startsWith("admin"))){
            menuItem = menu.findItem(R.id.action_add);
            menuItem.setVisible(false);
        }

        return super.onCreateOptionsMenu(menu);
    }


    public boolean onOptionsItemSelected(@NonNull MenuItem item){

        if (Objects.requireNonNull(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail()).startsWith("admin")){
            startActivity(new Intent(this, Requests.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
        }else{
            startActivity(new Intent(this, Home.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK ));
        }

        int id = item.getItemId();
        if(id==R.id.action_add){
            startActivity(new Intent(this, AddPost.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed(){
        super.onBackPressed();

        if (Objects.requireNonNull(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail()).startsWith("admin")){
            startActivity(new Intent(this, Requests.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK ));
        }else{
            startActivity(new Intent(this, Home.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
        }
    }

}