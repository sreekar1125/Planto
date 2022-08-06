package com.dev.planto.admin;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.dev.planto.R;
import com.dev.planto.user.Verification;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class RequestedUser extends AppCompatActivity {

    private DatabaseReference mDbRef,mDbRef2;
    private StorageReference mStrRef;
    private VideoView video;

    private String uId,uName;

    private static final int stdPts = 20;
    private static final String CHANNEL_ID = "PlantoNotifications",CHANNEL_NAME="PlantoNotifs";
    private final int NOTIFICATION_ID = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requested_user);

        Toolbar toolbar = findViewById(R.id.tool_bar_reqested_user);
        Button btnAccept = findViewById(R.id.btn_accept);
        Button btnReject = findViewById(R.id.btn_reject);
        video =  findViewById(R.id.req_vid);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        uName = getIntent().getStringExtra("userName");
        uId = getIntent().getStringExtra("uId");
        getSupportActionBar().setTitle(String.valueOf(uName));

        mDbRef = FirebaseDatabase.getInstance().getReference().child("Admins").child("Requests").child(uId);
        mDbRef2 = FirebaseDatabase.getInstance().getReference().child("Users").child(uId);
        mStrRef = FirebaseStorage.getInstance().getReference().child("Requests");

        mDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                video.setVideoURI(Uri.parse(String.valueOf(dataSnapshot.child("vidLink").getValue())));
                video.setMediaController(new MediaController(RequestedUser.this));
                video.requestFocus();
                video.start();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final int[] pts = new int[1];
        mDbRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pts[0] = Integer.parseInt(String.valueOf(dataSnapshot.child("Points").getValue()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDbRef2.child("VerifStatus").child(Objects.requireNonNull(mDbRef2.push().getKey())).child("Status").setValue("Verified");
                pts[0] = pts[0] + stdPts;
                mDbRef2.child("Points").setValue(String.valueOf(pts[0]));
                startActivity(new Intent(RequestedUser.this,Requests.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                mDbRef.removeValue();
                setNotifChannel();
                sendSuccessNotification();
            }
        });
        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDbRef2.child("VerifStatus").child(Objects.requireNonNull(mDbRef2.push().getKey())).child("Status").setValue("Failed");
                startActivity(new Intent(RequestedUser.this,Requests.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                mDbRef.removeValue();
                setNotifChannel();
                setFailedNotification();
            }
        });


    }

    private void setFailedNotification() {
        Intent intent = new Intent(this, Verification.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.raw.planto)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Verification Failed. Make new Upload after Few Hours")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(NOTIFICATION_ID,builder.build());
    }

    private void setNotifChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME,NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        startActivity(new Intent(this,Requests.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
        return true;
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        startActivity(new Intent(this,Requests.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }

    private void sendSuccessNotification(){

        Intent intent = new Intent(this, Verification.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.raw.planto)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Verified Successfully. Check your Rewards")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(NOTIFICATION_ID,builder.build());
    }


}