package com.online.Lyfe.Offline.databases;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;


public class database extends SQLiteAssetHelper {
    public static String name = "msgDb_13533.db";
    public static int version = 1;
    private Context mcontext;

    public database(Context context) {
        super(context, name, null, version);
        this.mcontext = context;
    }

    public ArrayList gettitle() {
        try {
            SQLiteDatabase db = getReadableDatabase();
            ArrayList arrayList = new ArrayList();
            Cursor cursor = db.rawQuery("select * from MSG_CAT", null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                arrayList.add(cursor.getString(cursor.getColumnIndex("NAME")));
                cursor.moveToNext();
            }
            return arrayList;
        } catch (Exception e) {
            Toast.makeText(mcontext, String.valueOf(e), Toast.LENGTH_SHORT).show();
            return null;
        }

    }

    public ArrayList gettext(int id) {
        try {

            SQLiteDatabase db = getReadableDatabase();
            ArrayList arrayList = new ArrayList<>();
            @SuppressLint("Recycle") Cursor cursor = db.rawQuery("select * from MESSAGES where MSG_CAT ='" + id + "'", null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                arrayList.add(cursor.getString(cursor.getColumnIndex("MESSAGE")));
                cursor.moveToNext();
            }
            return arrayList;
        } catch (Exception e) {
            Toast.makeText(mcontext, String.valueOf(e), Toast.LENGTH_SHORT).show();
            return null;
        }

    }

    public ArrayList randome(int id) {
        try {

            SQLiteDatabase db = getReadableDatabase();
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < 100; i++) {


                Cursor cursor = db.rawQuery("select * from MESSAGES where ID ='" + id + "'", null);
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    arrayList.add(cursor.getString(cursor.getColumnIndex("MESSAGE")));
                    cursor.moveToNext();
                }
                id++;
            }
            return arrayList;
        } catch (Exception e) {
            Toast.makeText(mcontext, String.valueOf(e), Toast.LENGTH_SHORT).show();
            return null;
        }

    }


}
