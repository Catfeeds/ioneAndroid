package com.boatchina.imerit.app.view.custom;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ImeiDao {
    private Context context;
    private ImeiDBHelper dbHelper;

    public ImeiDao(Context context) {
        this.context = context;
        dbHelper = new ImeiDBHelper(context);
    }

    public boolean find(String imei) {
        boolean result = false;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery(
                    "select imei from imei where imei=?",
                    new String[]{imei});
            if (cursor.moveToNext()) {
                result = true;
            }
            cursor.close();
            db.close();
        }
        return result;
    }

    /**
     * 添加
     */
    public void add(String imei,String name) {
        if (find(imei)) {
            return;
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            if(name==null) {
                db.execSQL("insert into imei (imei,name) values (?,?)", new Object[]{imei,imei});
            }else {
                db.execSQL("insert into imei (imei,name) values (?,?)", new Object[]{imei,name});
            }
            db.close();
        }
    }


    public String getNameByImei(String imei) {
        String name = "";
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery(
                    "select name from imei where imei=?",
                    new String[]{imei});
            if (cursor.moveToNext()) {
                name = cursor.getString(cursor.getColumnIndex("name"));
            }
            cursor.close();
            db.close();
        }
        return name;
    }

    public void deleteImei(String imei) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete("imei","imei=?",new String[]{imei});
            db.close();
        }
    }

    public void updateName(String imei,String name) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.execSQL("update imei set name=? where imei=?",new Object[] { name, imei});
            db.close();
        }
    }
}
