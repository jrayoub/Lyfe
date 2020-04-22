package com.online.Lyfe.Offline.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;

public class datafave extends SQLiteAssetHelper {
    public static String name = "favorite.db";
    public static int version = 1;
    Context mcontext;

    public datafave(Context context) {
        super(context, name, null, version);
        this.mcontext = context;

    }

    public void insert(String name) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("myfavorite", name);
            db.insert("favorit", null, values);
        } catch (Exception e) {
            Toast.makeText(mcontext, "insert" + e, Toast.LENGTH_LONG).show();
        }
    }

    public ArrayList getfav() {
        try {
            ArrayList name = new ArrayList();
            if (name != null) {
                SQLiteDatabase db = getReadableDatabase();
                Cursor cursor = db.rawQuery("select * from favorit", null);
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    name.add(cursor.getString(cursor.getColumnIndex("myfavorite")));
                    cursor.moveToNext();
                }
                return name;
            } else {
                ArrayList ff = new ArrayList();
                ff.add("empty  ");
                return ff;
            }

        } catch (Exception e) {
            Toast.makeText(mcontext, "read" + e, Toast.LENGTH_LONG).show();
            ArrayList nothing = new ArrayList();
            nothing.add(" empty ");
            return nothing;
        }
    }

    public void delet(String id) {
        try {
            String a = "'";
            String d = "delete from favorit where myfavorite ='" + id + a;
            SQLiteDatabase db = getWritableDatabase();

            //db.execSQL(String.valueOf(d));
            db.delete("favorit", "myfavorite=?", new String[]{id});
        } catch (Exception e) {
            Toast.makeText(mcontext, "read" + e, Toast.LENGTH_LONG).show();

        }
    }

}
