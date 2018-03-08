package com.boatchina.imerit.app.view.custom;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by fflamingogo on 2016/8/9.
 */
public class ImeiDBHelper extends SQLiteOpenHelper {
    public ImeiDBHelper(Context context) {
        super(context, "imei.db", null, 1);
    }

    /**
     * 第一次创建数据库的时候执行 oncreate方法
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE imei (_id integer primary key autoincrement, imei varchar(20),name varchar(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


}


