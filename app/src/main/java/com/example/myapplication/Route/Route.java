package com.example.myapplication.Route;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.DB.Matrix;

import java.util.ArrayList;

public class Route {
    //각 역마다 갈 수 있는 모든 경로를 담은 배열리스트
    private ArrayList<Vertex[]> vertexList = new ArrayList<>();

    //모든 역 이름(환승역은 1개만)
    private String stn[]= {"성진", "성중", "기중", "고기", "성단", "시진", "무진", "무주", "성남"    //A호선:9개
            , "홍원", "성반", "홍삼", "김진도", "홍사", "무리", "북진", "설진", "고지", "충기"       //B호선:10개
            , "김천입구", "김가네", "군자시"      //C호선:3개
            , "잠수", "덕담", "북악", "둔기"        //D호선:4개
            , "까치", "남앙", "일견", "의정", "역사"      //E호선:5개
            , "족기", "소래", "냉수", "감산", "개화"      //F호선:5개
            , "율곡", "원홍", "이리요", "의자"       //G호선:4개
    };      //총 40개의 역

    //각 역마다 갈 수 있는 모든 경로 찾기, type 4: 시간, 5:요금
    public Route(SQLiteDatabase db, int type) {
        for(int i=0; i<stn.length;i++) {
            ArrayList<Vertex> route;
            route = findRoute(stn[i], db, type);

            Vertex[] vertexArr = new Vertex[route.size()];
            int size=0;

            for(Vertex tmp : route){
                vertexArr[size++]= tmp;
            }
            vertexList.add(vertexArr);
        }
    }

    //1개 역에서 갈 수 있는 모든 경로 찾아서 반환, type 4: 시간, 5:요금
    public ArrayList<Vertex> findRoute(String station, SQLiteDatabase db, int type) {
        //1개 역의 경로를 담을 route변수
        ArrayList<Vertex> route = new ArrayList<>();
        //Matrix에서 초기화한 DB를 이용
        String sql = String.format("SELECT * FROM %s", Matrix.TB_NAME);
        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            String stnNm1 = cursor.getString(2);
            String stnNm2 = cursor.getString(3);
            double weight = cursor.getDouble(type);

            //찾으려는 역과 일치할 시, route에 추가
            if (stnNm1.equals(station)) {
                route.add(new Vertex(stnNm2, weight));
            } else if (stnNm2.equals(station)) {
                route.add(new Vertex(stnNm1, weight));
            }
        }
        return route;
    }

    public ArrayList<Vertex[]> getVertexList() {
        return vertexList;
    }

    public String[] getStn() {
        return stn;
    }
}
