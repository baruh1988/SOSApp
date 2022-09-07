package com.example.sosapp;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<Contact> {

    Context context;
    List<Contact> contacts;

    public CustomAdapter(@NonNull Context context, List<Contact> contacts) {
        super(context, 0, contacts);
        this.contacts = contacts;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        DBHelper db = new DBHelper(context);
        Contact c = contacts.get(position);
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_contact, parent, false);
        LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.linear);
        TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
        TextView tvPhone = (TextView) convertView.findViewById(R.id.tv_phone);
        tvName.setText(c.getName());
        tvPhone.setText(c.getPhoneNumber());
        linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new MaterialAlertDialogBuilder(context).setTitle("Remove contact")
                        .setMessage("Are you sure you want to remove this contact?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                db.deleteContact(c);
                                contacts.remove(c);
                                notifyDataSetChanged();
                                Toast.makeText(context, "Contact removed!", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
                return false;
            }
        });
        return convertView;
    }

    public void refresh(List<Contact> list) {
        contacts.clear();
        contacts.addAll(list);
        notifyDataSetChanged();
    }

}
