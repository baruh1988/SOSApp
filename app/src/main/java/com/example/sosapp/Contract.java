package com.example.sosapp;

import android.provider.BaseColumns;

public final class Contract {
    private Contract() {
    }

    public static class SettingsEntry implements BaseColumns {
        public static final String TABLE_NAME = "settings";
        public static final String COLUMN_NAME_MESSAGE = "message";
        public static final String COLUMN_NAME_NOTIFY = "notifyEnabled";
        public static final String COLUMN_NAME_AMOUNT = "notifyAmount";
    }

    public static class ContactsEntry implements BaseColumns {
        public static final String TABLE_NAME = "contacts";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_CONTACT = "contact";
        public static final String COLUMN_NAME_PHONE = "phone";
    }

    public static class CallEntry implements BaseColumns {
        public static final String TABLE_NAME = "calls";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_DATE = "date";
    }
}
