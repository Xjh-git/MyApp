package com.example.chat.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.chat.bean.Contacts;

import java.util.List;

public class ContactsDAO {
    ChatDatabase helper;

    SQLiteDatabase db;

    public ContactsDAO(Context context) {
        helper = new ChatDatabase(context);
    }

    public void addContacts(String userName, String contactsName) {

    }

    public void deleteContacts(String userName, String contactsName) {

    }

    public List<Contacts> getUserAllContacts(String userName){

        return null;
    }

}
