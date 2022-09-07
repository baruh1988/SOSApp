package com.example.sosapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "SosDB.db";

    private static final String SQL_CREATE_CONTACT_ENTRIES = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT)", Contract.ContactsEntry.TABLE_NAME, Contract.ContactsEntry.COLUMN_NAME_ID, Contract.ContactsEntry.COLUMN_NAME_CONTACT, Contract.ContactsEntry.COLUMN_NAME_PHONE);
    private static final String SQL_DELETE_CONTACT_ENTRIES = String.format("DROP TABLE IF EXISTS %s", Contract.ContactsEntry.TABLE_NAME);

    private static final String SQL_CREATE_CALL_ENTRIES = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT)", Contract.CallEntry.TABLE_NAME, Contract.CallEntry.COLUMN_NAME_ID, Contract.CallEntry.COLUMN_NAME_DATE);
    private static final String SQL_DELETE_CALL_ENTRIES = String.format("DROP TABLE IF EXISTS %s", Contract.CallEntry.TABLE_NAME);

    private static final String SQL_CREATE_SETTINGS_ENTRY = String.format("CREATE TABLE %s (id INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s INTEGER, %s INTEGER)", Contract.SettingsEntry.TABLE_NAME, Contract.SettingsEntry.COLUMN_NAME_MESSAGE,  Contract.SettingsEntry.COLUMN_NAME_NOTIFY, Contract.SettingsEntry.COLUMN_NAME_AMOUNT);
    private static final String SQL_DELETE_SETTINGS_ENTRY = String.format("DROP TABLE IF EXISTS %s", Contract.CallEntry.TABLE_NAME);

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CONTACT_ENTRIES);
        db.execSQL(SQL_CREATE_CALL_ENTRIES);
        db.execSQL(SQL_CREATE_SETTINGS_ENTRY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_CONTACT_ENTRIES);
        db.execSQL(SQL_DELETE_CALL_ENTRIES);
        db.execSQL(SQL_DELETE_SETTINGS_ENTRY);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void addContact(Contact toAdd) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(Contract.ContactsEntry.COLUMN_NAME_CONTACT, toAdd.getName());
        c.put(Contract.ContactsEntry.COLUMN_NAME_PHONE, toAdd.getPhoneNumber());
        db.insert(Contract.ContactsEntry.TABLE_NAME, null, c);
        db.close();
    }

    public List<Contact> getContactList() {
        List<Contact> list = new ArrayList<>();
        String query = "SELECT * FROM " + Contract.ContactsEntry.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                list.add(new Contact(c.getInt(0), c.getString(1), c.getString(2)));
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return list;
    }

    public void deleteContact(Contact toDelete) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Contract.ContactsEntry.TABLE_NAME, Contract.ContactsEntry.COLUMN_NAME_ID + "=?", new String[]{String.valueOf(toDelete.getId())});
        db.close();
    }

    public void addCall(LocalDateTime toAdd) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        String date = toAdd.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        c.put(Contract.CallEntry.COLUMN_NAME_DATE, date);
        db.insert(Contract.CallEntry.TABLE_NAME, null, c);
        db.close();
    }

    public List<LocalDateTime> getCallDates() {
        List<LocalDateTime> list = new ArrayList<>();
        String query = "SELECT * FROM " + Contract.CallEntry.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                list.add(LocalDateTime.parse(c.getString(1), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        Collections.sort(list);
        return list;
    }

    public void deleteCalls() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(SQL_DELETE_CALL_ENTRIES);
        db.execSQL(SQL_CREATE_CALL_ENTRIES);
        db.close();
    }

    public HashMap<String, String> getSettings() {
        HashMap<String, String> s = new HashMap();
        String query = "SELECT * FROM " + Contract.SettingsEntry.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                s.put(c.getColumnName(1), c.getString(1));
                s.put(c.getColumnName(2), "" + c.getInt(2));
                s.put(c.getColumnName(3), "" + c.getInt(3));
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return s;
    }

    public void initialSettings() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(Contract.SettingsEntry.COLUMN_NAME_MESSAGE, "I am in DANGER, i need help. Please urgently reach me out. Here are my coordinates.");
        c.put(Contract.SettingsEntry.COLUMN_NAME_NOTIFY, 0);
        c.put(Contract.SettingsEntry.COLUMN_NAME_AMOUNT, 5);
        db.insert(Contract.SettingsEntry.TABLE_NAME, null, c);
        db.close();
    }

    public void updateSettings(String column, String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        switch (column) {
            case Contract.SettingsEntry.COLUMN_NAME_AMOUNT:
                c.put(column, Integer.valueOf(value));
                break;
            case Contract.SettingsEntry.COLUMN_NAME_NOTIFY:
                c.put(column, value.equals("1") ? 1 : 0);
                break;
            default:
                c.put(column, value);
                break;
        }
        db.update(Contract.SettingsEntry.TABLE_NAME, c, "id=" + 1, null);
        db.close();
    }
}
