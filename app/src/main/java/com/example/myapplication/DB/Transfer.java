package com.example.myapplication.DB;

import android.database.sqlite.SQLiteDatabase;

public class Transfer {
    private static SQLiteDatabase db;
    public static String TB_NAME = "Transfer";
    static String SQL_CREATE = String.format("CREATE TABLE %s(" +
            "stnNm TEXT NOT NULL," +
            "startLineNm TEXT NOT NULL," +
            "endLineNm TEXT NOT NULL," +
            "primary key(stnNm, startLineNm, endLineNm));", TB_NAME);
    static String SQL_DROP = String.format("DROP TABLE IF EXISTS %s", TB_NAME);
    static String SQL_DELETE_ALL = String.format("DELETE FROM %s", TB_NAME);


    /* Database */
    private static void insert(String stnNm, String... lineNms) {
        for (int i = 0; i < lineNms.length; i+=2) {
            String sql = String.format(
                    "INSERT INTO %s VALUES('%s','%s','%s');",
                    TB_NAME, stnNm, lineNms[i], lineNms[i+1]);
            db.execSQL(sql);
        }
    }

    static void setDatabase(SQLiteDatabase db) {
        Transfer.db = db;
    }

    static void initDatabase() {
        // A호선
        insert("고기","A호선", "B호선","A호선","C호선");
        insert("시진", "A호선", "B호선");
        insert("무진", "A호선", "D호선");
        insert("무주", "A호선", "G호선");
        // B호선
        insert("고기","B호선","A호선", "B호선", "C호선");
        insert("설진","B호선","C호선");
        insert("시진","B호선","A호선");
        insert("홍사","B호선","F호선","B호선","G호선");
        // C호선
        insert("고기","C호선","A호선","C호선","B호선");
        insert("설진","C호선","B호선");
        insert("군자시","C호선","D호선");
        // D호선
        insert("군자시","D호선","C호선");
        insert("북악","D호선","E호선");
        insert("무진","D호선","A호선");
        // E호선
        insert("북악","E호선","D호선");
        insert("의정","D호선","G호선");
        // F호선
        insert("기중","F호선","A호선");
        insert("홍사","F호선","B호선","F호선","G호선");
        // G호선
        insert("홍사","G호선","B호선","G호선","F호선");
        insert("무주","G호선","A호선");
        insert("의정","G호선","E호선");
    }
}
