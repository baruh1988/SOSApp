package com.example.sosapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {

    DBHelper db;
    EditText messageInput;
    Button clearBtn;
    SwitchCompat notifySwitch;
    NumberPicker amountInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize settings
        db = new DBHelper(this);
        HashMap<String, String> tmp = db.getSettings();
        if (tmp.isEmpty()) {
            db.initialSettings();
        } else {
            Settings.getSettings().setMessage(tmp.get(Contract.SettingsEntry.COLUMN_NAME_MESSAGE));
            Settings.getSettings().setNotifyEnabled(tmp.get(Contract.SettingsEntry.COLUMN_NAME_NOTIFY).equals("1"));
            Settings.getSettings().setAmount(Integer.valueOf(tmp.get(Contract.SettingsEntry.COLUMN_NAME_AMOUNT)));
        }

        messageInput = (EditText) findViewById(R.id.message_input);
        clearBtn = (Button) findViewById(R.id.clear_btn);
        notifySwitch = (SwitchCompat) findViewById(R.id.toggle_notify);
        amountInput = (NumberPicker) findViewById(R.id.count_input);

        messageInput.setText(Settings.getSettings().getMessage());
        notifySwitch.setChecked(Settings.getSettings().isNotifyEnabled());
        amountInput.setMaxValue(20);
        amountInput.setMinValue(1);
        amountInput.setValue(Settings.getSettings().getAmount());

        clearBtn.setOnClickListener(view -> db.deleteCalls());

        messageInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Settings.getSettings().setMessage(messageInput.getText().toString());
                db.updateSettings(Contract.SettingsEntry.COLUMN_NAME_MESSAGE, messageInput.getText().toString());
            }
        });

        notifySwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            db.updateSettings(Contract.SettingsEntry.COLUMN_NAME_NOTIFY, notifySwitch.isChecked() ? "1" : "0");
            Settings.getSettings().setNotifyEnabled(notifySwitch.isChecked());
        });

        amountInput.setOnValueChangedListener((numberPicker, i, i1) -> {
            Settings.getSettings().setAmount(numberPicker.getValue());
            db.updateSettings(Contract.SettingsEntry.COLUMN_NAME_AMOUNT, "" + numberPicker.getValue());
        });
    }
}
