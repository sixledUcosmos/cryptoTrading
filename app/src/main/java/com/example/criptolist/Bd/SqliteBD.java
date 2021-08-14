package com.example.criptolist.Bd;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SqliteBD extends SQLiteOpenHelper {
  private static final int basededatos_version =10;
  private static final String database = "Criptolist.db";
  private final String sqlCreateUser = "CREATE TABLE usuario (id INTEGER PRIMARY KEY AUTOINCREMENT, money DOUBLE,generate DOUBLE,pay TEXT,install BOOLEAN )";
  private final String sqlCreateCripto = "CREATE TABLE activos (id INTEGER PRIMARY KEY AUTOINCREMENT, ref TEXT,name TEXT,symbol TEXT,image TEXT,amount DOUBLE,price DOUBLE)";
  private final String sqlCreatehystory = "CREATE TABLE history (id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,description TEXT,value TEXT)";
  public SqliteBD(@Nullable Context context) {
    super(context, database, null, basededatos_version);
  }


  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(sqlCreateUser);
    db.execSQL(sqlCreateCripto);
    db.execSQL(sqlCreatehystory);
    db.execSQL("INSERT INTO usuario (money,generate,pay,install) " + "VALUES ('0', '20.00','13/04/1992','"+true+"')");
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE  IF EXISTS usuario");
    db.execSQL("DROP TABLE  IF EXISTS activos");
    db.execSQL(sqlCreateUser);
    db.execSQL(sqlCreateCripto);
  }
}
