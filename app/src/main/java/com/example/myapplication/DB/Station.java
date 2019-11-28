package com.example.myapplication.DB;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Station implements Parcelable {
    private static SQLiteDatabase db;
    public static String TB_NAME = "station";
    private int id;
    private String lineNm;
    private String name;
    private String door, callNum, toilet, escalator, elevator, wheelLift;
    private int X,Y;
    private double km,time;
    private int fee;
    static String SQL_CREATE = String.format("CREATE TABLE %s(" +
            "id INTEGER NOT NULL," +
            "lineNm TEXT NOT NULL," +
            "name TEXT NOT NULL," +
            "X INTEGER," +
            "Y INTEGER," +
            "km REAL," +
            "time REAL," +
            "fee INTEGER," +
            "door TEXT," +
            "callNum TEXT," +
            "toilet TEXT,"+
            "elevator TEXT,"+
            "escalator TEXT,"+
            "wheelLift TEXT,"+
            "primary key(id, lineNm));", TB_NAME);
    static String SQL_DROP = String.format("DROP TABLE IF EXISTS %s", TB_NAME);
    static String SQL_DELETE_ALL = String.format("DELETE FROM %s", TB_NAME);
    static void setDatabase(SQLiteDatabase db) {
        Station.db = db;
    }

    public Station(int id, String lineNm, String name, int x, int y, double km, double time, int fee, String door, String callNum, String toilet, String elevator, String escalator, String wheelLift ) {
        this.id = id;
        this.lineNm = lineNm;
        this.name = name;
        X = x;
        Y = y;
        this.km = km;
        this.time = time;
        this.fee = fee;
        this.door=door;
        this.callNum=callNum;
        this.toilet=toilet;
        this.elevator=elevator;
        this.escalator=escalator;
        this.wheelLift=wheelLift;
    }
    private static void insert(int id, String lineNm, String name,int x, int y, double km, double time, int fee,  String door, String callNum, String toilet, String elevator, String escalator, String wheelLift) {
        //DB에 입력한 값으로 행 추가
        String sql = String.format(
                "INSERT INTO %s VALUES(%d, '%s', '%s', %d, %d, %f, %f, %d, '%s', '%s', '%s','%s', '%s', '%s');",
                TB_NAME, id, lineNm, name, x, y, km, time, fee, door, callNum, toilet,elevator, escalator,wheelLift
        );
        db.execSQL(sql);
    }

    public static ArrayList<String> getLines(String lineNm, String stnNm) {
        ArrayList<String> lines = new ArrayList<>();
        Cursor cursor = db.rawQuery(String.format("SELECT pointx, pointy FROM %s WHERE lineNm='%s' AND stnNm='%s'", TB_NAME, lineNm, stnNm), null);
        if (cursor.moveToNext()) {
            int pointx = cursor.getInt(0);
            int pointy = cursor.getInt(1);

            cursor.close();
            cursor = db.rawQuery(String.format("SELECT lineNm FROM %s WHERE pointx=%d AND pointy=%d", TB_NAME, pointx, pointy), null);
            while (cursor.moveToNext())
                lines.add(cursor.getString(0));
        }
        cursor.close();
        return lines;
    }
    /* parcelable */
    protected Station(Parcel in) {
        lineNm = in.readString();
        id = in.readInt();
        name = in.readString();
        km = in.readDouble();
        door = in.readString();
        callNum = in.readString();
        toilet = in.readString();
        elevator = in.readString();
        escalator = in.readString();
        wheelLift = in.readString();
    }

    public static final Creator<Station> CREATOR = new Creator<Station>() {
        @Override
        public Station createFromParcel(Parcel in) {
            return new Station(in);
        }

        @Override
        public Station[] newArray(int size) {
            return new Station[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(lineNm);
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeDouble(km);
        dest.writeString(door);
        dest.writeString(callNum);
        dest.writeString(toilet);
        dest.writeString(elevator);
        dest.writeString(escalator);
        dest.writeString(wheelLift);
    }


    static void initDatabase() {
        insert(0,"A호선","성진",82,64, 0.0, 0, 0,"왼쪽","010-1111-1111","외부","Y","Y","N");
        insert(1,"A호선","성중",176,64,2.0,2.0,100,"왼쪽","010-1111-1111","외부","Y","Y","N");
        insert(2,"A호선","기중",342,64,4.6,3.0,130,"왼쪽","010-1111-1111","외부","Y","Y","N");
        insert(3,"A호선","고기",420,118,6.4,2.0,90,"왼쪽","010-1111-1111","외부","Y","Y","N");
        insert(4,"A호선","성단",487,272,9.6,3.2,150,"왼쪽","010-1111-1111","외부","Y","Y","N");
        insert(5,"A호선","시진",531,427,13.1,3.3,160,"왼쪽","010-1111-1111","외부","Y","Y","N");
        insert(6,"A호선","무진",655,510,16.1,2.8,120,"왼쪽","010-1111-1111","외부","Y","Y","N");
        insert(7,"A호선","무주",767,513,17.8,2.0,100,"왼쪽","010-1111-1111","외부","Y","Y","N");
        insert(8,"A호선","성남",863,512,19.1,1.0, 60,"왼쪽","010-1111-1111","외부","Y","Y","N");

        insert(0,"B호선","홍원",249,134, 22.8, 2.0, 60,"왼쪽","010-1111-1111","외부","Y","Y","N");
        insert(1,"B호선","고기",420,118,2.5,2.7,70,"왼쪽","010-1111-1111","외부","Y","Y","Y");
        insert(2,"B호선","성반",528,147,4.8,2.2,60,"왼쪽","010-1111-1111","외부","Y","Y","N");
        insert(3,"B호선","홍삼",618,212,7.0,2.0,50,"왼쪽","010-1111-1111","외부","Y","Y","N");
        insert(4,"B호선","김진도",681,165,8.6,1.5,50,"왼쪽","010-1111-1111","외부","Y","Y","N");
        insert(5,"B호선","홍사",742,116,9.1,1.3,40,"왼쪽","010-1111-1111","외부","Y","Y","Y");
        insert(6,"B호선","무리",629,339,9.5,2.3,70,"왼쪽","010-1111-1111","외부","Y","Y","N");
        insert(7,"B호선","시진",531,427,12.3,2.5,70,"왼쪽","010-1111-1111","외부","Y","Y","N");
        insert(8,"B호선","북진",380,459,15.2,2.6, 90,"왼쪽","010-1111-1111","외부","Y","Y","N");
        insert(9,"B호선","설진",203,420,18.2,3.0, 100,"왼쪽","010-1111-1111","외부","Y","Y","N");
        insert(10,"B호선","고지",121,345,19.6,1.0, 40,"왼쪽","010-1111-1111","외부","Y","Y","N");
        insert(11,"B호선","충기",123,222,20.8,1.5, 40,"왼쪽","010-1111-1111","외부","Y","Y","Y");

        insert(0,"C호선","고기",420,118,0.0,0.0, 0,"왼쪽","010-1111-1111","외부","Y","Y","N");
        insert(1,"C호선","김천입구",397,221,4.0,1.7, 210,"왼쪽","010-1111-1111","외부","Y","Y","N");
        insert(2,"C호선","김가네",310,314,8.2,2.0, 220,"왼쪽","010-1111-1111","외부","Y","Y","N");
        insert(3,"C호선","설진",203,419,11.8,1.5, 200,"왼쪽","010-1111-1111","외부","Y","Y","Y");
        insert(4,"C호선","군자시",184,518,15.1,1.0, 200,"왼쪽","010-1111-1111","외부","Y","Y","N");

        insert(0,"D호선","잠수",55,298,0.0,0.0, 0,"왼쪽","010-1111-1111","외부","Y","Y","N");
        insert(1,"D호선","덕담",55,518,3.0,2.6, 120,"왼쪽","010-1111-1111","외부","Y","Y","N");
        insert(2,"D호선","군자시",184,518,5.0,2.0, 90,"왼쪽","010-1111-1111","외부","Y","Y","N");
        insert(3,"D호선","북악",282,518,6.4,1.0, 80,"왼쪽","010-1111-1111","외부","Y","Y","N");
        insert(4,"D호선","둔기",438,518,8.4,1.6, 100,"왼쪽","010-1111-1111","외부","Y","Y","N");
        insert(5,"D호선","무진",655,518,11.0,3.0, 150,"왼쪽","010-1111-1111","외부","Y","Y","N");

        insert(0,"E호선","북악",282,518,0.0,0.0, 0,"왼쪽","010-1111-1111","외부","Y","Y","Y");
        insert(1,"E호선","까치",360,574,3.0,2.6, 120,"왼쪽","010-1111-1111","외부","Y","Y","N");
        insert(2,"E호선","남앙",499,596,6.4,3.0, 130,"왼쪽","010-1111-1111","외부","Y","Y","Y");
        insert(3,"E호선","일견",639,603,9.9,4.0, 150,"왼쪽","010-1111-1111","외부","Y","Y","Y");
        insert(4,"E호선","의정",800,592,12.9,3.0, 120,"왼쪽","010-1111-1111","외부","Y","Y","Y");
        insert(5,"E호선","역사",933,583,14.8,2.0, 130,"왼쪽","010-1111-1111","외부","Y","Y","N");

        insert(0,"F호선","기중",342,64,0.0,0.0, 0,"왼쪽","010-1111-1111","외부","Y","Y","N");
        insert(1,"F호선","촉기",509,64,4.0,4.0, 150,"왼쪽","010-1111-1111","외부","Y","Y","N");
        insert(2,"F호선","소래",684,64,7.7,4.0, 140,"왼쪽","010-1111-1111","외부","Y","Y","N");
        insert(3,"F호선","홍사",742,115,9.7,2.0, 100,"왼쪽","010-1111-1111","외부","Y","Y","N");
        insert(4,"F호선","냉수",862,115,12.7,2.5, 120,"왼쪽","010-1111-1111","외부","Y","Y","N");
        insert(5,"F호선","감산",988,115,15.6,2.5, 120,"왼쪽","010-1111-1111","외부","Y","Y","N");
        insert(6,"F호선","개화",984,352,19.8,4.0, 160,"왼쪽","010-1111-1111","외부","Y","Y","Y");

        insert(0,"G호선","율곡",738,15,0.0,0.0, 0,"왼쪽","010-1111-1111","외부","Y","Y","N");
        insert(1,"G호선","홍사",742,115,2.3,2.0, 150,"왼쪽","010-1111-1111","외부","Y","Y","N");
        insert(2,"G호선","원홍",738,338,6.0,2.9, 220,"왼쪽","010-1111-1111","외부","Y","Y","N");
        insert(3,"G호선","무주",767,513,9.8,2.7, 210,"왼쪽","010-1111-1111","외부","Y","Y","N");
        insert(4,"G호선","의정",798,590,11.8,2.0, 160,"왼쪽","010-1111-1111","외부","Y","Y","N");
        insert(5,"G호선","이리요",846,634,13.7,2.0, 130,"왼쪽","010-1111-1111","외부","Y","Y","Y");
        insert(6,"G호선","의자",1054,634,16.7,3.0, 190,"왼쪽","010-1111-1111","외부","Y","Y","N");

    }

    public int getId() {return id; }
    public void setId(int id) { this.id = id; }
    public String getLineNm() { return lineNm; }
    public void setLineNm(String lineNm) { this.lineNm = lineNm; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getX() { return X; }
    public void setX(int x) { X = x; }
    public int getY() { return Y; }
    public void setY(int y) { Y = y; }
    public double getKm() { return km; }
    public void setKm(float km) { this.km = km; }
    public double getTime() { return time; }
    public void setTime(float time) { this.time = time; }
    public int getFee() { return fee; }
    public void setFee(int fee) { this.fee = fee; }
    public String getDoor() {
        return door;
    }
    public String getCallNum() {
        return callNum;
    }
    public String getToilet() {
        return toilet;
    }
    public String getElevator() {return elevator;}
    public String getEscalator() {
        return escalator;
    }
    public String getWheelLift() {
        return wheelLift;
    }
}
