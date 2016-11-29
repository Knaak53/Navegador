package com.multiplataforma.wtfkn.navegador;

import android.content.Context;
import android.database.sqlite.*;
/**
 * Created by wtfkn on 14/10/2016.
 */
public class AdminSqlLite extends SQLiteOpenHelper {
    public AdminSqlLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table urls(id int AUTO_INCREMENT primary key , url text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
