package com.example.sosapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DashboardActivity extends AppCompatActivity {

    CardView sosCard;
    CardView analyticsCard;
    CardView contactsCard;
    CardView settingsCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        sosCard = findViewById(R.id.card_sos);
        analyticsCard = findViewById(R.id.card_analytics);
        contactsCard = findViewById(R.id.card_contacts);
        settingsCard = findViewById(R.id.card_settings);

        // Go back to main activity
        sosCard.setOnClickListener(view -> finish());

        // Launch analytics view
        analyticsCard.setOnClickListener(this::moveToActivity);

        // Launch contacts view
        contactsCard.setOnClickListener(this::moveToActivity);

        // Launch settings view
        settingsCard.setOnClickListener(this::moveToActivity);
    }

    public void moveToActivity(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.card_contacts:
                intent.setClass(this, ContactsActivity.class);
                break;
            case R.id.card_analytics:
                intent.setClass(this, AnalyticsActivity.class);
                break;
            case R.id.card_settings:
                intent.setClass(this, SettingsActivity.class);
                break;
        }
        startActivity(intent);
    }
}