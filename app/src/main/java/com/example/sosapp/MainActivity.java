package com.example.sosapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton sosBtn;
    FloatingActionButton menuBtn;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // check for runtime permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.SEND_SMS, Manifest.permission.READ_CONTACTS}, 100);
        }

        // this is a special permission required only by devices using
        // Android Q and above. The Access Background Permission is responsible
        // for populating the dialog with "ALLOW ALL THE TIME" option
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, 100);
        }

        DBHelper db = new DBHelper(this);

        sosBtn = findViewById(R.id.sos_btn);
        menuBtn = findViewById(R.id.menu_btn);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        sosBtn.setOnClickListener(view -> {
            // Send sos to contacts
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //get current location
                Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
                locationTask.addOnSuccessListener(location -> {
                    List<Contact> list = db.getContactList();
                    db.addCall(LocalDateTime.now());
                    SmsManager smsManager = SmsManager.getDefault();
                    for (Contact c : list) {
                        Toast.makeText(MainActivity.this, "Sent SOS to " + c.getName(), Toast.LENGTH_SHORT).show();
                        String coordinates = location != null ? "http://maps.google.com/?q=" + location.getLatitude() + "," + location.getLongitude() : "";
                        String message = "Hey " + c.getName() + ", " + Settings.getSettings().getMessage() + "\n " + coordinates;
                        smsManager.sendTextMessage(c.getPhoneNumber(), null, message, null, null);
                    }
                });
                locationTask.addOnFailureListener(e -> Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show());
            }
        });

        // Open dashboard view
        menuBtn.setOnClickListener(this::moveToDashboard);
    }

    public void moveToDashboard(View view) {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }

}