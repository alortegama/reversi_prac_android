package com.udl.viladegut.ortega.reversi.BDSQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LogSQLiteHelper extends SQLiteOpenHelper {

    //Sentencia SQL para crear la taula Logs
    String sqlCreate = "CREATE TABLE Logs (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, alias TEXT NOT NULL, " +
            "data_hora TEXT NOT NULL, tabla INTEGER NOT NULL, num_negras INTEGER NOT NULL, num_blancas INTEGER NOT NULL," +
            "tiempo_total INTEGER, resultado TEXT NOT NULL)";


    public LogSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int prevVersion, int newVersion) {

        //Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS Logs");

        //Se crea la nueva versión de la tabla
        db.execSQL(sqlCreate);
    }
}
