package com.example.myapplication.Activity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.DB.DBOpenHelper;
import com.example.myapplication.DB.Station;
import com.example.myapplication.Dialog.StationMenuDialog;
import com.example.myapplication.R;
import com.example.myapplication.Route.Dijkstras;
import com.example.myapplication.Route.Route;
import com.example.myapplication.Route.StationMatrix;
import com.example.myapplication.Route.Vertex;
import com.example.myapplication.Utiles.SubwayLine;
import com.example.myapplication.Utiles.SubwayMapTouchPoint;
import com.github.chrisbanes.photoview.OnViewTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity {
    private SubwayMapTouchPoint subwayMapTouchPoint;
    private StationMatrix stationMatrix;
    private ArrayList<Station> stnIdx;
    private PhotoView lineMapView;
    private Dijkstras dijkstraTime = new Dijkstras(),dijkstraFee = new Dijkstras();
    private TextView startStnTextView, endStnTextView;
    private StationMenuDialog dialog;

    private SearchListAdapter searchListAdapter;
    private SearchView searchView;
    private ListView searchList;
    Intent intent;

    private boolean isFabOpen; //FloatingAction Open/Close 여부
    private Animation fab_open, fab_close, rotate_forward, rotate_backward; //FloatingAction 애니메이션
    private FloatingActionButton fab,fab_favorite, fab_sitelink, fab_settings; //FloatingActionButton


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, Splash.class);
        startActivity(intent);
        lineMapView = findViewById(R.id.photoView);
        lineMapView.setImageResource(R.drawable.sample);
        lineMapView.setMaximumScale(2.0f); //최대확대크기 설정
        lineMapView.setMediumScale(1.5f); //중간확대크기 설정
        lineMapView.setMinimumScale(0.78f); //최소축소크기 설정
        lineMapView.setScale(0.78f, true); //앱 시작할 때 기본 크기 설정
        lineMapView.setOnViewTapListener(new OnLineMapViewTab());

        // 검색바 이벤트 리스너 추가
        searchList = findViewById(R.id.search_list);
        searchListAdapter = new SearchListAdapter(this);
        searchList.setAdapter(searchListAdapter);
        searchList.setOnItemClickListener(new OnSearchListItemClick());
        searchView = findViewById(R.id.search);
        searchView.setOnQueryTextListener(new OnSearchViewQueryText());

        // 현재 선택된 출발역, 도착역을 표시하는 텍스트뷰
        startStnTextView = findViewById(R.id.startStnTextView);
        endStnTextView = findViewById(R.id.endStnTextView);
        startStnTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(GONE);
                subwayMapTouchPoint.startStn = null;
            }
        });

        endStnTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(GONE);
                subwayMapTouchPoint.endStn = null;
            }
        });

        DBOpenHelper DBOpenHelper = new DBOpenHelper(MainActivity.this );
        SQLiteDatabase db = DBOpenHelper.getWritableDatabase();
        DBOpenHelper.setDatabase(db);
        DBOpenHelper.initDatabase(db,true);
        subwayMapTouchPoint = new SubwayMapTouchPoint(MainActivity.this); //역 터치 좌표 초기화
        stationMatrix = new StationMatrix(DBOpenHelper.getReadableDatabase());
        stnIdx = stationMatrix.getStnIdx();
        dijkstraTime=initRoute(new Route(db,4));    //시간
        dijkstraFee=initRoute(new Route(db,5 ));    //요금

        // activity_main 의 플로팅 액션 버튼 동그라미
        fab = findViewById(R.id.fab);
        fab_favorite = findViewById(R.id.fab_favorite);
        fab_sitelink = findViewById(R.id.fab_sitelink);
        fab_sitelink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SitelinkActivity.class);
                startActivity(intent);
            }
        });
        fab_settings = findViewById(R.id.fab_settings);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFAB(); // 버튼 클릭시 FloatingAction 애니메이션 시작
            }
        });

        fab_open = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(this, R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(this, R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(this, R.anim.rotate_backward);

    }

    @Override
    public void onBackPressed() {
        if (searchListAdapter.getCount() != 0) {
            searchView.setQuery("", false);
            searchListAdapter.clear();
            searchList.setAdapter(searchListAdapter); //리스트뷰 갱신
            return;
        }
    }

    // 역 터치 dialog
    private void displayStationTouchDialog(final Station stn) {
        // Dialog 생성, 메뉴 리스너 설정
        dialog = new StationMenuDialog(MainActivity.this, subwayMapTouchPoint.getLineNms(stnIdx, stn), stn.getName());
        dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    /* 이 switch문에서는 위에서 받은 역 정보(stn)를 팝업메뉴에서 선택한 아이템에 따라
                     * lineMapView의 startStn(출발역), endStn(도착역), viaStn(경유역)에 '저장'하거나
                     * '역 정보 액티비티'(StnInfoPagerActivity)를 호출할 수 있어
                     *
                     * switch문 아래에 있는 '경로 안내 액티비티'(RouteGuidancePagerActivity)는
                     * 출발역(SubwayMapTouchPoint.startStn), 도착역(SubwayMapTouchPoint.endStn)이 둘 다 설정된 경우에만 호출되도록 했고,
                     * 출발역, 도착역 둘 다 설정하기 전에 경유역(SubwayMapTouchPoint.viaStn)을 설정하면 출발역-경유역-도착역 경로를 알려줘 **/
                    case R.id.start: //출발역
                        subwayMapTouchPoint.startStn = stn; //SubwayMapTouchPoint map의 startStn에 출발역 정보를 저장한다
                        startStnTextView.setText(String.format("  %s: %s   ", MainActivity.this.getString(R.string.start_station), stn.getName()));
                        startStnTextView.setVisibility(View.VISIBLE);
                        if (subwayMapTouchPoint.endStn == null) { //SubwayMapTouchPoint map의 endStn이 선택되었는지 검사하여
                            dialog.cancel();
                            return; //null이면 다이얼로그를 종료하고,
                        }
                        break; //null이 아니면 switch문 다음에서 '경로 안내 액티비티'(RouteGuidancePagerActivity)를 호출한다

                    case R.id.end: //도착역 (R.id.start와 비슷함)
                        subwayMapTouchPoint.endStn = stn;
                        endStnTextView.setText(String.format("  %s: %s   ", MainActivity.this.getString(R.string.end_station), stn.getName()));
                        endStnTextView.setVisibility(View.VISIBLE);
                        if (subwayMapTouchPoint.startStn == null) {
                            dialog.cancel();
                            return;
                        }
                        break;


                case R.id.info: //정보
                    // 역 정보 Activity 호출
                    Intent intent = new Intent(MainActivity.this, StnInfoPagerActivity.class);
                    intent.putExtra("lines", subwayMapTouchPoint.getLineNms(stnIdx, stn));
                    intent.putExtra("name", stn.getName());
                    MainActivity.this.startActivity(intent);
                    dialog.cancel();
                    return;

                    default:
                        dialog.cancel();
                        return;
                }

            /* 출발역(startStn), 도착역(endStn) 모두 입력되었다면,
               경로탐색 쓰레드 실행 **/
                findRoute();
                dialog.cancel();
            }
        });
        dialog.show(); //dialog를 보여준다
    }

    private class OnLineMapViewTab implements OnViewTapListener {

        @Override
        public void onViewTap(View view, float x, float y) {
            // 터치한 좌표(x,y)를 사용하여 'stn 객체'에 터치한 역의 정보(StnPoint: 터치좌표, 역호선, 역이름)를 저장한다
            Station stn = subwayMapTouchPoint.getStation(
                    stnIdx,
                    lineMapView.getDisplayRect().left, lineMapView.getDisplayRect().top,
                    lineMapView.getScale(), x, y);
            if (stn != null) //터치한 위치에 역이 있는 경우 Dialog를 띄운다
                displayStationTouchDialog(stn);
        }
    }

    //각 역마다 초기화된 경로를 다익스트라에 삽입
    private Dijkstras initRoute(Route route){
        Dijkstras dijkstras = new Dijkstras();
        String stn[] = route.getStn();
        ArrayList<Vertex[]> vertex = route.getVertexList();
        int size=0;
        for(Vertex[] v : vertex){
            dijkstras.addVertex(stn[size++], Arrays.asList(v));
        }
        return dijkstras;
    }

    //다익스트라
    private void findRoute(){
        String start = subwayMapTouchPoint.startStn.getName();  //출발역
        String end = subwayMapTouchPoint.endStn.getName();      //도착역
        List<String> route  = dijkstraTime.getDijkstras(start,end);  //다익스트라(시간) 메소드 => 리스트 반환
        route.add(start);
        Collections.reverse(route);  //출력결과가 반대로 나와서 뒤집어준다.
        double time = dijkstraTime.getWeight(start,route);  //리스트의 담긴 역들 간의 총시간 반환
        int fee = (int)dijkstraFee.getWeight(start,route);  //리스트의 담긴 역들 간의 총요금 반환
        List<String> route2  = dijkstraFee.getDijkstras(start,end);  //다익스트라(요금) 메소드 => 리스트 반환
        route2.add(start);
        Collections.reverse(route2);  //출력결과가 반대로 나와서 뒤집어준다.
        double time2 = dijkstraTime.getWeight(start,route2);  //리스트의 담긴 역들 간의 총시간 반환
        int fee2 = (int)dijkstraFee.getWeight(start,route2);  //리스트의 담긴 역들 간의 총요금 반환


        String message="";   //리스트를 담을 문자열 변수 선언.
        for(String str : route){
            message += str;
            if(!(str.equals(end))){
                message+="->";
            }
        }
        String message2="";   //리스트를 담을 문자열 변수 선언.
        for(String str : route2){
            message2 += str;
            if(!(str.equals(end))){
                message2+="->";
            }
        }

        //경로 출력
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("경로").setMessage("약 "+time+"분\t|\t"+fee+"원\n"+message);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        intent = new Intent(getApplicationContext(),RouteActivity.class);
        intent.putExtra("startStn",start);
        intent.putExtra("endStn",end);
        intent.putExtra("routeStn","약 "+time+"분\t|\t"+fee+"원\n"+message);
        intent.putExtra("route2Stn","약 "+time2+"분\t|\t"+fee2+"원\n"+message2);
        startActivity(intent);//액티비티 띄우기


       /* Intent intent = new Intent(getApplicationContext(),RouteActivity.class);

        startActivity(intent);//액티비티 띄우기*/

        //출발역, 도착역 초기화
        subwayMapTouchPoint.startStn=null;
        subwayMapTouchPoint.endStn=null;
        startStnTextView.setVisibility(View.INVISIBLE);
        endStnTextView.setVisibility(View.INVISIBLE);

    }

    // SearchListView의 Adapter
    private class SearchListAdapter extends BaseAdapter {
        private ViewGroup.LayoutParams params =
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        private Context context;
        private int id = R.layout.item_station;
        private ArrayList<MyItem> myItems = new ArrayList<>();

        SearchListAdapter(Context context) {
            this.context = context;
        }

        void add(String lineNm, String Name, int X, int Y) {
            for (MyItem item : myItems) {
                if (X == item.x && Y == item.y) {
                    item.lienNms.add(lineNm);
                    return;
                }
            }
            myItems.add(new MyItem(lineNm, Name, X, Y));
        }

        void clear() {
            myItems.clear();
        }

        @Override
        public int getCount() {
            return myItems.size();
        }

        @Override
        public MyItem getItem(int position) {
            return myItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(id, parent, false);

                holder = new Holder();
                holder.lineSymLayout = convertView.findViewById(R.id.lineSymLayout);
                holder.Name = convertView.findViewById(R.id.Name);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
                holder.lineSymLayout.removeAllViews();
            }

            MyItem stn = myItems.get(position);
            for (String lineNm : stn.lienNms) {
                ImageView imageView = new ImageView(context);
                imageView.setImageResource(SubwayLine.getResId(lineNm));
                holder.lineSymLayout.addView(imageView, params);
            }
            holder.Name.setText(stn.Name);

            return convertView;
        }

        private class Holder {
            LinearLayout lineSymLayout;
            TextView Name;
        }

        private class MyItem {
            private ArrayList<String> lienNms = new ArrayList<>();
            private String Name;
            private int x, y;

            private MyItem(String lienNm, String Name, int x, int y) {
                lienNms.add(lienNm);
                this.Name = Name;
                this.x = x;
                this.y = y;
            }

            public String getName() {
                return Name;
            }

        }

    }

    private class OnSearchViewQueryText implements SearchView.OnQueryTextListener {

        @Override
        public boolean onQueryTextSubmit(String query) {
            if (searchListAdapter.getCount() == 0)
                return false;

            AdapterView.OnItemClickListener listener = searchList.getOnItemClickListener();
            if (listener != null)
                listener.onItemClick(null, null, 0, 0);
            return true;
        }

        @Override
        public boolean onQueryTextChange(String mText) {
            searchListAdapter.clear();
            if (mText.equals(""))
                return false;
            for (Station stn : stnIdx)
                if (stn.getName().startsWith(mText))
                    searchListAdapter.add(stn.getLineNm(), stn.getName(), stn.getX(), stn.getY());
            searchList.setAdapter(searchListAdapter); //리스트뷰 갱신
            return true;
        }

    }

    private class OnSearchListItemClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String Name = searchListAdapter.getItem(position).getName();
            Station mStn = null;
            for (Station stn : stnIdx)
                if (Name.equals(stn.getName()))
                    mStn = stn;

            if (mStn != null) {
                searchView.setQuery("", false);
                searchListAdapter.clear();
                searchList.setAdapter(searchListAdapter); //리스트뷰 갱신
                displayStationTouchDialog(mStn);
            }
        }

    }

    // FloatingAction 애니메이션 Open/Close
    private void animateFAB() {
        if (isFabOpen) {
            fab.startAnimation(rotate_backward);
            fab_favorite.startAnimation(fab_close);
            fab_sitelink.startAnimation(fab_close);
            fab_settings.startAnimation(fab_close);
            fab_favorite.setClickable(false);
            fab_sitelink.setClickable(false);
            fab_settings.setClickable(false);
            isFabOpen = false;
        } else {
            fab.startAnimation(rotate_forward);
            fab_favorite.startAnimation(fab_open);
            fab_sitelink.startAnimation(fab_open);
            fab_settings.startAnimation(fab_open);
            fab_favorite.setClickable(true);
            fab_sitelink.setClickable(true);
            fab_settings.setClickable(true);
            isFabOpen = true;
        }
    }
/*
    private void displayStationTouchDialog(Station stn) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(stn.getLineNm()).setMessage(stn.getName());

        AlertDialog alertDialog = builder.create();

        alertDialog.show();
    }
 */

}
