package com.example.myapplication.Route;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.DB.Station;

import java.util.ArrayList;

public class StationMatrix {
    public final static int INF = 99999; //Infinity (연결되지 않은 두 역 사이의 가중치)
    private int n;
    private ArrayList<Station> stnIdx; //행|열 번호에 해당하는 역이름
    private int[][] matrix; //가중치 인접행렬
    private int[][] transMatrix;
    private int numOfStns;

    public StationMatrix(SQLiteDatabase db) {
        stnIdx = new ArrayList<>();
        String sql = String.format("SELECT * FROM %s ORDER BY id, lineNm", Station.TB_NAME);
        Cursor cursor = db.rawQuery(sql, null);

        // stnIdx: 역 이름셋 작성
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String lineNm = cursor.getString(1);
            String name = cursor.getString(2);
            int x = cursor.getInt(3);
            int y = cursor.getInt(4);
            float km = cursor.getFloat(5);
            float time = cursor.getFloat(6);
            int fee = cursor.getInt(7);
            String door = cursor.getString(8);
            String callNum = cursor.getString(9);
            String toilet = cursor.getString(10);
            String elevator=cursor.getString(11);
            String escalator=cursor.getString(12);
            String wheelLift=cursor.getString(13);

            stnIdx.add(new Station(id, lineNm, name, x, y, km, time, fee, door, callNum, toilet, elevator,escalator, wheelLift));
        }
        cursor.close();
        numOfStns = stnIdx.size();
    }
    private ArrayList<Integer> getIndexes(String stnNm) {
        ArrayList<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < numOfStns; i++)
            if (stnNm.equals(stnIdx.get(i).getName()))
                indexes.add(i);
        return indexes;
    }

    public void setVirtualNodes(ArrayList<Integer> startLineIdxs, ArrayList<Integer> endLineIdxs) {
        // 초기화
        int start = n - 2, end = n - 1;
        for (int i = 0; i < n; i++) {
            matrix[i][start] = INF;
            matrix[start][i] = INF;
            transMatrix[i][start] = INF;
            transMatrix[start][i] = INF;
        }
        matrix[start][start] = 0;
        for (int i = 0; i < n; i++) {
            matrix[i][end] = INF;
            matrix[end][i] = INF;
            transMatrix[i][end] = INF;
            transMatrix[end][i] = INF;
        }
        matrix[end][end] = 0;
        // 가상시작점, 가상도착점 설정
        for (int i : startLineIdxs) {
            matrix[i][start] = 0;
            matrix[start][i] = 0;
            transMatrix[i][start] = 0;
            transMatrix[start][i] = 0;
        }
        for (int i : endLineIdxs) {
            matrix[i][end] = 0;
            matrix[end][i] = 0;
            transMatrix[i][end] = 0;
            transMatrix[end][i] = 0;
        }
    }
    public ArrayList<Station> getStnIdx() {
        return stnIdx;
    }
}
