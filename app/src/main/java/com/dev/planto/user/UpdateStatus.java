package com.dev.planto.user;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.dev.planto.bottomsheet.LocationMapsBS;
import com.dev.planto.help.HelpActivity;
import com.dev.planto.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class UpdateStatus extends AppCompatActivity {

    private Button camUpadteStat;
    private VideoView mVideo;
    private ProgressDialog progressDialog;
    private TextView usLocation;
    private VideoView gifView;

    private DatabaseReference mDbRef,mRef2;
    private StorageReference strRef;

    private static final int CAM_REQ_CODE = 1;
    private Uri filePath;

    private LocationMapsBS locationMapsBS;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location currentLocation;
    private static  final  int REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_status);

        Toolbar toolbar = findViewById(R.id.update_stat_tb);
        camUpadteStat = findViewById(R.id.btn_camera_update_status);
        mVideo = findViewById(R.id.id_video);
        CardView cvLocation = findViewById(R.id.cardv_location);
        usLocation = findViewById(R.id.txt_update_status_location);
        gifView = findViewById(R.id.gif_planting);

        String rootNode = "Admins";
        String reqNode = "Requests";
         mDbRef = FirebaseDatabase.getInstance().getReference().child(rootNode).child(reqNode);
        mRef2 = FirebaseDatabase.getInstance().getReference().child("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        strRef = FirebaseStorage.getInstance().getReference();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation();

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.update));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);


        camUpadteStat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicVid();
            }
        });
        cvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationMapsBS = new LocationMapsBS();
                locationMapsBS.show(getSupportFragmentManager(), "Location Maps");
            }
        });

        gifView.setVideoURI(Uri.parse("https://i.giphy.com/media/1AiqkWHM0b3jYa2QQC/giphy.mp4"));
        gifView.start();


    }

    private void takePicVid() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            Intent i = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

            startActivityForResult(i,CAM_REQ_CODE);
        }else{
            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA);
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},1);

            this.takePicVid();
        }

    }

    private void fetchLocation() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION);
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);

            this.fetchLocation();
            // return ;
        }else{
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null){
                                currentLocation = location;

                                Geocoder geocoder = new Geocoder(getApplicationContext());
                                List<Address> addresses;
                                try {
                                    addresses = geocoder.getFromLocation(currentLocation.getLatitude(),currentLocation.getLongitude(),1);

                                    String addr = addresses.get(0).getAddressLine(0);
                                    usLocation.setText(addr);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_update_status,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        startActivity(new Intent(this,Home.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));

        switch (item.getItemId()){
            case R.id.menu_help:
                startActivity(new Intent(this, HelpActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                break;
            case 0:
                break;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode , final Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAM_REQ_CODE && resultCode == RESULT_OK) {
            final String[] uName = new String[1];
            final String[] vidName = new String[1];
            final StorageReference[] childRef = new StorageReference[1];

            gifView.setVisibility(View.INVISIBLE);
            mVideo.setVisibility(View.VISIBLE);
            mVideo.start();
            mVideo.setMediaController(new MediaController(this));
            mVideo.requestFocus();
            mVideo.canPause();
            mVideo.canSeekBackward();
            mVideo.canSeekForward();
            mVideo.getDuration();
            mVideo.getCurrentPosition();
            mVideo.setVideoURI(data.getData());

            camUpadteStat.setText(R.string.submit);

            camUpadteStat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isConnected()){
                        progressDialog.setTitle("Submitting");
                        progressDialog.setMessage("Completed...");
                        progressDialog.show();
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.setCancelable(false);

                        filePath = data.getData();

                        mRef2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                uName[0] = String.valueOf(dataSnapshot.child("Name").getValue());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                        vidName[0] = uName[0] + ".mp4" ;
                        childRef[0] = strRef.child("Requests").child(vidName[0]);

                        childRef[0].putFile(filePath)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        progressDialog.dismiss();
                                        Toast.makeText(UpdateStatus.this, "Submitted", Toast.LENGTH_SHORT).show();

                                        childRef[0].getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                mDbRef.child(Objects.requireNonNull(mRef2.getKey())).child("Name").setValue(uName[0]);
                                                mDbRef.child(Objects.requireNonNull(mRef2.getKey())).child("vidLink").setValue(uri.toString());
                                            }
                                        });

                                        mVideo.setVisibility(View.GONE);
                                        camUpadteStat.setText("Start planting");
                                    }

                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.hide();
                                        Toast.makeText(UpdateStatus.this, "Submit Failed", Toast.LENGTH_SHORT).show();
                                    }
                                })

                                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                                        int progress = (int) ((100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
                                        progressDialog.setMessage("Uploading  " + progress + "%");
                                    }
                                });



                    }else{
                        Toast.makeText(UpdateStatus.this, "Please Connect to Wifi/Network", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    private boolean isConnected(){
        ConnectivityManager cm  = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            return ((cm.getActiveNetworkInfo() != null) && (cm.getActiveNetworkInfo().isConnectedOrConnecting()));
        }
        return true;
    }

    @SuppressLint("ResourceType")
    @Override
    public void onStart(){
        super.onStart();

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if( !Objects.requireNonNull(locationManager).isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.location)+"?")
                    .setIcon(R.raw.location)
                    .setMessage(getString(R.string.location_need))
                    .setPositiveButton(getString(R.string.settings), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            camUpadteStat.setText(R.string.start_planting);
                            camUpadteStat.setEnabled(true);
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton(getString(R.string.cancel), null)
                    .show();
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();

        startActivity(new Intent(this,Home.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }
}