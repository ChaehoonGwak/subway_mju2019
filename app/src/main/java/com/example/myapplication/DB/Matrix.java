package com.example.myapplication.DB;

import android.database.sqlite.SQLiteDatabase;

public class Matrix {

    private static SQLiteDatabase db;
    public static String TB_NAME = "matrix";
    private int id;
    private String lineNm;
    private String stnNm1;
    private String stnNm2;
    private double weight;
    private double fee;
    static String SQL_CREATE = String.format("CREATE TABLE %s(" +
            "id INTEGER NOT NULL," +
            "lineNm TEXT NOT NULL," +
            "stnNm1 TEXT NOT NULL," +
            "stnNm2 TEXT NOT NULL," +
            "weight DOUBLE," +
            "fee DOUBLE," +
            "primary key(id, lineNm));", TB_NAME);
    static String SQL_DROP = String.format("DROP TABLE IF EXISTS %s", TB_NAME);
    static String SQL_DELETE_ALL = String.format("DELETE FROM %s", TB_NAME);
    static void setDatabase(SQLiteDatabase db) {
        Matrix.db = db;
    }
    public Matrix(int id, String lineNm, String stnNm1, String stnNm2, double weight, double fee) {
        this.id = id;
        this.lineNm = lineNm;
        this.stnNm1 = stnNm1;
        this.stnNm2 = stnNm2;
        this.weight = weight;
        this.fee = fee;
    }
    private static void insert(int id, String lineNm, String stnNm1, String stnNm2, double weight, double fee) {
        //DB에 입력한 값으로 행 추가
        String sql = String.format(
                "INSERT INTO %s VALUES(%d, '%s', '%s', '%s', %f, %f);",
                TB_NAME, id, lineNm, stnNm1, stnNm2, weight, fee
        );
        db.execSQL(sql);
    }


    static void initMatrix() {
        // 가중치(역간 소요시간) 입력
        // A호선
        insert(0,"A호선", "성진", "성중", 2,100.0);
        insert(1,"A호선", "성중", "기중", 3,130.0);
        insert(2,"A호선", "기중", "고기", 2,90.0);
        insert(3,"A호선", "고기", "성단", 3.2,150.0);
        insert(4,"A호선", "성단", "시진", 3.3,160.0);
        insert(5,"A호선", "시진", "무진", 2.8,120.0);
        insert(6,"A호선", "무진", "무주", 2,100.0);
        insert(7,"A호선", "무주", "성남", 1,60.0);

// B호선
        insert(0,"B호선", "홍원", "고기", 2.7,70.0);
        insert(1,"B호선", "고기", "성반", 2.2,60.0);
        insert(2,"B호선", "성반", "홍삼", 2,50.0);
        insert(3,"B호선", "홍삼", "김진도", 1.5,50.0);
        insert(4,"B호선", "김진도", "홍사", 1.3,40.0);
        insert(5,"B호선", "홍삼", "무리", 2.3,70.0);
        insert(6,"B호선", "무리", "시진", 2.5,70.0);
        insert(7,"B호선", "시진", "북진", 2.6,90.0);
        insert(8,"B호선", "북진", "설진", 3,100.0);
        insert(9,"B호선", "설진", "고지", 1,40.0);
        insert(10,"B호선", "고지", "충기", 1.5,40.0);
        insert(11,"B호선", "충기", "홍원", 2,60.0);

// C호선
        insert(0,"C호선", "고기", "김천입구", 1.7,210.0);
        insert(1,"C호선", "김천입구", "김가네", 2,220.0);
        insert(2,"C호선", "김가네", "설진", 1.5,200.0);
        insert(3,"C호선", "설진", "군자시", 1,200.0);

// D호선
        insert(0,"D호선", "잠수", "덕담", 2.6,120.0);
        insert(1,"D호선", "덕담", "군자시", 2,90.0);
        insert(2,"D호선", "군자시", "북악", 1,80.0);
        insert(3,"D호선", "북악", "둔기", 1.6,100.0);
        insert(4,"D호선", "둔기", "무진", 3,150.0);

// E호선
        insert(0,"E호선", "북악", "까치", 2.6,120.0);
        insert(1,"E호선", "까치", "남앙", 3,130.0);
        insert(2,"E호선", "남앙", "일견", 4,150.0);
        insert(3,"E호선", "일견", "의정", 3,120.0);
        insert(4,"E호선", "의정", "역사", 3,120.0);

// F호선
        insert(0,"F호선", "기중", "족기", 4,150.0);
        insert(1,"F호선", "족기", "소래", 4,140.0);
        insert(2,"F호선", "소래", "홍사", 2,100.0);
        insert(3,"F호선", "홍사", "냉수", 2.5,120.0);
        insert(4,"F호선", "냉수", "감산", 2.5,120.0);
        insert(5,"F호선", "감산", "개화", 4,160.0);

// G호선
        insert(0,"G호선", "율곡", "홍사", 2,150.0);
        insert(1,"G호선", "홍사", "원홍", 2.9,220.0);
        insert(2,"G호선", "원홍", "무주", 2.7,210.0);
        insert(3,"G호선", "무주", "의정", 2,160.0);
        insert(4,"G호선", "의정", "이리요", 2,130.0);
        insert(5,"G호선", "이리요", "의자", 3,190.0);

        /*// 환승역 설정
        // A호선
        setTrans("기중", 5, 3); //A-F
        setTrans("고기", 2, 2, 3, 4, 1, 1); //A-B, A-C, B-C
        setTrans("시진", 2, 2); //A-B
        setTrans("무진", 4, 4); //A-D
        setTrans("무주", 3, 3); //A-G

        // B호선
        setTrans("설진", 3, 3); //B-C
        setTrans("홍사", 3, 2,5,4,3,3); //B-F, B-G. G-F

        // C호선
        setTrans("군자시", 4, 4); //C-D

        // D호선
        setTrans("북악", 8, 8); //D-E

        // E호선
        setTrans("의정", 3, 3); //E-G

        //F호선

        // G호선*/

    }

}
