package com.dev.planto.user;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;

import android.text.TextUtils;
import android.text.method.TextKeyListener;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.dev.planto.Awareness;
import com.dev.planto.R;
import com.dev.planto.SignIn;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class Home extends AppCompatActivity {
    private ImageView tb_plant;
    private ProgressBar progressBar;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle t;

    private int doubleBack = 1;
    private  String dpLink;

    private int[] images = new int[4];
    private int currentIndex;
    private int startIndex;
    private int endIndex;

    private DatabaseReference mDbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar =  findViewById(R.id.tool_bar_home);
        Button openCam =  findViewById(R.id.btn_open_cam);
        tb_plant = findViewById(R.id.plant_thumbnail);
        progressBar = findViewById(R.id.points_home);

        drawerLayout = findViewById(R.id.home_drawer);
        navigationView = findViewById(R.id.home_nav);

        images[0] = R.raw.plant1;
        images[1] = R.raw.plant2;
        images[2] = R.raw.plant3;
        images[3] = R.raw.plant4;

        startIndex = 0;
        endIndex = 3;
        nextImage();

        mDbRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());

         t = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(t);
        t.syncState();

        navigationView.setItemIconTintList(null);
        setNavigation();


        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.app_name));



        openCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this,UpdateStatus.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                //drawerLayout.closeDrawers();

                switch (item.getItemId()){
                    case R.id.menu_home:

                        break;

                    case R.id.menu_payment:
                        startActivity(new Intent(Home.this,Payments.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        break;
                    case R.id.menu_verification:
                        startActivity(new Intent(Home.this,Verification.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        break;
                    case R.id.menu_videos:
                        startActivity(new Intent(Home.this, Awareness.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        break;
                    case R.id.menu_settings:
                        startActivity(new Intent(Home.this,Settings.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        break;

                    case  R.id.menu_invite:
                        inviteUser();
                        break;
                    case R.id.menu_sign_out:
                       signOUt();
                       break;
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });

    }

    private void setNavigation() {
        View navHeader = navigationView.getHeaderView(0);
        final TextView uName = navHeader.findViewById(R.id.home_nav_uname);
        final TextView uEmail = navHeader.findViewById(R.id.home_nav_uemail);

        final ImageView dp = navHeader.findViewById(R.id.home_nav_dp);


        mDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                uName.setText(String.valueOf(dataSnapshot.child("Name").getValue()));
                uEmail.setText(String.valueOf(dataSnapshot.child("Email").getValue()));
                 progressBar.setProgress(Integer.parseInt(String.valueOf(dataSnapshot.child("Points").getValue())));

                dpLink = String.valueOf(dataSnapshot.child("DpLink").getValue());

                if (dpLink.isEmpty() || !(dataSnapshot.hasChild("DpLink"))){
                    dp.setImageDrawable(getResources().getDrawable(R.drawable.user));
                }else{
                    Picasso.get().load(dpLink).into(dp);
                    /*Glide.with(Home.this)
                            .load(dpLink)
                            .into(dp);*/
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Home.this,databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        navHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(GravityCompat.START);
                startActivity(new Intent(Home.this,Profile.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });
    }

    private void inviteUser() {

        Intent sendIntent  = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "https://planto-jointhegreenside.000webhostapp.com/");
        sendIntent.setType("text/*");
        startActivity(Intent.createChooser(sendIntent,getString(R.string.invite)));
    }

    private void signOUt() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(getString(R.string.sign_out_dialog_txt));
        builder.setTitle(getString(R.string.sign_out)+"?");
        builder.setPositiveButton(getString(R.string.sign_out), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseAuth.getInstance().signOut();

                if (FirebaseAuth.getInstance().getCurrentUser() == null){
                    startActivity(new Intent(Home.this, SignIn.class));
                    finish();
                }

            }
        });
        builder.setNegativeButton(getString(R.string.not_now), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.setIcon(R.drawable.logout);

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void nextImage() {
        tb_plant.setImageResource(images[currentIndex]);
        currentIndex++;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(currentIndex>endIndex){
                    currentIndex--;
                    previousImage();
                }else{
                    nextImage();
                }

            }
        },2000);
    }

    private void previousImage() {
            tb_plant.setImageResource(images[currentIndex]);
            currentIndex--;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(currentIndex<startIndex){
                        currentIndex++;
                        nextImage();
                    }else{
                        previousImage(); // here 1000(1 second) interval to change from current  to previous image
                    }
                }
            },2000);

        }

    @Override
    public void onStart(){
        super.onStart();
        requestPermissions();

    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED  || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED  || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION);
            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA);
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},1);

        }

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if (t.onOptionsItemSelected(item)){
            return true;
        }
        return true;
    }

    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            if (doubleBack == 2){
                super.onBackPressed();

            }else{
                doubleBack++;
                Toast.makeText(this, getString(R.string.exit_toast), Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBack =1;
                    }
                },2000);
            }
        }
    }
}