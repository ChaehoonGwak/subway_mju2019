package com.example.myapplication.DB;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

public class Elevator implements Parcelable {
    private static SQLiteDatabase db;
    public static String TB_NAME = "Elevator";
    static String SQL_CREATE = String.format("CREATE TABLE %s(" +
            "lineNm TEXT NOT NULL," +
            "stnNm TEXT NOT NULL," +
            "num TEXT NOT NULL," +
            "floor TEXT," +
            "location TEXT," +
            "primary key(lineNm, stnNm, num));", TB_NAME);
    static String SQL_DROP = String.format("DROP TABLE IF EXISTS %s", TB_NAME);
    static String SQL_DELETE_ALL = String.format("DELETE FROM %s", TB_NAME);

    private String num, floor, location;

    public Elevator(String num, String floor, String location) {
        this.num = num;
        this.floor = floor;
        this.location = location;
    }

    public String getNum() {
        return num;
    }

    public String getFloor() {
        return floor;
    }

    public String getLocation() {
        return location;
    }

    public static String getNearEvLocations(String lineNm, String stnNm, String nextStnNm) {
        // 5-1 B2(승강장)~B1(대합실), 3-2 B2(승강장)~B1(대합실)
        StringBuilder evLocations = new StringBuilder();

        // 상행하행 검색
        // boolean isUpward = DownLine.isUpward(lineNm, stnNm, nextStnNm);
        String keyward = "상";

        // 엘리베이터 정보 검색
        String sql = String.format("SELECT floor, location FROM %s WHERE lineNm='%s' AND stnNm='%s'", TB_NAME, lineNm, stnNm);
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            String location = cursor.getString(1);
            if (location.contains("섬") || location.contains(keyward)) {
                //int pos = location.indexOf('-'); //8
                //String simpleLocation = location.substring(pos - 1, pos + 2); //7, 9
                String floor = cursor.getString(0);
                evLocations.append(String.format("%s\t|\t%s\n", location, floor));
            }
        }
        cursor.close();

        if (evLocations.length() > 1) //evLocation에 내용이 있으면 맨마지막 \n 문자를 지운다
            return evLocations.substring(0, evLocations.length() - 1);
        else
            return null;
    }


    /* Parcelable */
    private Elevator(Parcel in) {
        num = in.readString();
        floor = in.readString();
        location = in.readString();
    }

    public static final Creator<Elevator> CREATOR = new Creator<Elevator>() {
        @Override
        public Elevator createFromParcel(Parcel in) {
            return new Elevator(in);
        }

        @Override
        public Elevator[] newArray(int size) {
            return new Elevator[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(num);
        dest.writeString(floor);
        dest.writeString(location);
    }

    /* Database */
    private static void insert(String lineNm, String stnNm, String num, String floor, String location) {
        //DB에 입력한 값으로 행 추가
        String sql = String.format(
                "INSERT INTO %s VALUES('%s','%s','%s','%s','%s');",
                TB_NAME, lineNm, stnNm, num, floor, location);
        db.execSQL(sql);
    }

    static void setDatabase(SQLiteDatabase database) {
        Elevator.db = database;
    }

    static void initDatabase() {

        //A호선
        insert("A호선", "성진", "내부#1", "B2 ~ B1", "5-1");
        insert("A호선", "기중", "내부#1", "B2 ~ B1", "2-3");
        insert("A호선", "고기", "내부#1", "B2 ~ B1", "1-7");
        insert("A호선", "성단", "내부#1", "B2 ~ B1", "5-1");
        insert("A호선", "시진", "내부#1", "B2 ~ B1", "6-2");
        insert("A호선", "무진", "내부#1", "B2 ~ B1", "3-1");
        insert("A호선", "무주", "내부#1", "B2 ~ B1", "8-2");
        insert("A호선", "성남", "내부#1", "B2 ~ B1", "5-1");

//B호선
        insert("B호선", "홍원", "내부#1", "B2 ~ B1", "3-7");
        insert("B호선", "고기", "내부#1", "B2 ~ B1", "1-5");
        insert("B호선", "성반", "내부#1", "B2 ~ B1", "3-1");
        insert("B호선", "홍삼", "내부#1", "B2 ~ B1", "2-3");
        insert("B호선", "김진도", "내부#1", "B2 ~ B1", "3-7");
        insert("B호선", "홍사", "내부#1", "B2 ~ B1", "3-7");
        insert("B호선", "무리", "내부#1", "B2 ~ B1", "3-2");
        insert("B호선", "시진", "내부#1", "B2 ~ B1", "3-1");
        insert("B호선", "북진", "내부#1", "B2 ~ B1", "5-4");
        insert("B호선", "설진", "내부#1", "B2 ~ B1", "3-7");
        insert("B호선", "고지", "내부#1", "B2 ~ B1", "3-5");
        insert("B호선", "충기", "내부#1", "B2 ~ B1", "4-5");

//C호선
        insert("C호선", "고기", "내부#1", "B2 ~ B1", "2-4");
        insert("C호선", "김천입구", "내부#1", "B2 ~ B1", "1-8");
        insert("C호선", "김가네", "내부#1", "B2 ~ B1", "2-4");
        insert("C호선", "설진", "내부#1", "B2 ~ B1", "1-6");
        insert("C호선", "군자시", "내부#1", "B2 ~ B1", "2-1");

//D호선
        insert("D호선", "잠수", "내부#1", "B2 ~ B1", "2-5");
        insert("D호선", "덕담", "내부#1", "B2 ~ B1", "1-5");
        insert("D호선", "군자시", "내부#1", "B2 ~ B1", "1-5");
        insert("D호선", "북악", "내부#1", "B2 ~ B1", "3-6");
        insert("D호선", "둔기", "내부#1", "B2 ~ B1", "2-7");
        insert("D호선", "무진", "내부#1", "B2 ~ B1", "1-5");

//E호선
        insert("E호선", "북악", "내부#1", "B2 ~ B1", "3-2");
        insert("E호선", "까치", "내부#1", "B2 ~ B1", "4-1");
        insert("E호선", "남양", "내부#1", "B2 ~ B1", "5-3");
        insert("E호선", "일견", "내부#1", "B2 ~ B1", "3-1");
        insert("E호선", "의정", "내부#1", "B2 ~ B1", "3-2");
        insert("E호선", "역사", "내부#1", "B2 ~ B1", "4-6");

//F호선
        insert("F호선", "기중", "내부#1", "B2 ~ B1", "8-1");
        insert("F호선", "촉기", "내부#1", "B2 ~ B1", "8-1");
        insert("F호선", "소래", "내부#1", "B2 ~ B1", "8-1");
        insert("F호선", "홍사", "내부#1", "B2 ~ B1", "8-1");
        insert("F호선", "냉수", "내부#1", "B2 ~ B1", "8-1");
        insert("F호선", "감산", "내부#1", "B2 ~ B1", "8-1");
        insert("F호선", "개화", "내부#1", "B2 ~ B1", "8-1");

        insert("G호선", "율곡", "내부#1", "B2 ~ B1", "6-3");
        insert("G호선", "홍사", "내부#1", "B2 ~ B1", "3-8");
        insert("G호선", "원홍", "내부#1", "B2 ~ B1", "3-1");
        insert("G호선", "무주", "내부#1", "B2 ~ B1", "1-6");
        insert("G호선", "의정", "내부#1", "B2 ~ B1", "6-3");
        insert("G호선", "이리요", "내부#1", "B2 ~ B1", "2-8");
        insert("G호선", "의자", "내부#1", "B2 ~ B1", "3-5");

    }
}
