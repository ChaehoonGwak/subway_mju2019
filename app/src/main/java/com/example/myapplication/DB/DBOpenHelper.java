package com.example.myapplication.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;


public class DBOpenHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static String DB_NAME = "subway.db";

    public DBOpenHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       dropTables(db);
        onCreate(db);
    }
    private void createTables(SQLiteDatabase db) {
        db.execSQL(Station.SQL_CREATE);
        db.execSQL(Matrix.SQL_CREATE);
        db.execSQL(Elevator.SQL_CREATE);
        db.execSQL(Transfer.SQL_CREATE);
    }

    private void dropTables(SQLiteDatabase db) {
        db.execSQL(Station.SQL_DROP);
        db.execSQL(Matrix.SQL_DROP);
        db.execSQL(Elevator.SQL_DROP);
        db.execSQL(Transfer.SQL_DROP);
    }
    public void initDatabase(SQLiteDatabase db, boolean retry) {
        try {
            // 데이터 삭제&재삽입
            db.execSQL(Station.SQL_DELETE_ALL);
            db.execSQL((Matrix.SQL_DELETE_ALL));
            db.execSQL(Elevator.SQL_DELETE_ALL);
            db.execSQL(Transfer.SQL_DELETE_ALL);
            Station.initDatabase();
            Matrix.initMatrix();
            Elevator.initDatabase();
            Transfer.initDatabase();
        } catch (SQLiteException e) {
            if (retry) {
                // 테이블 삭제&재생성
                // TODO 유저 데이터는 따로 백업이 필요하다...
                dropTables(db);
                createTables(db);
                initDatabase(db, false);
            } else {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
    public void setDatabase(SQLiteDatabase db) {
        Station.setDatabase(db);
        Matrix.setDatabase(db);
        Elevator.setDatabase(db);
        Transfer.setDatabase(db);

    }
}
