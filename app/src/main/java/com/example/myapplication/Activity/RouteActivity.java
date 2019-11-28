package com.example.myapplication.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.myapplication.R;
import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ButtonObject;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class RouteActivity extends AppCompatActivity {
    private TextView startStnTextView1, endStnTextView1, routeStnTextView1;
    private TextView startStnTextView2, endStnTextView2, routeStnTextView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        TabHost tabHost1 = (TabHost) findViewById(R.id.tabHost1);
        tabHost1.setup();

        // 첫 번째 Tab. (탭 표시 텍스트:"TAB 1"), (페이지 뷰:"content1")
        TabHost.TabSpec ts1 = tabHost1.newTabSpec("Tab Spec 1");
        ts1.setContent(R.id.content1);
        ts1.setIndicator("최단시간");
        tabHost1.addTab(ts1);

        // 두 번째 Tab. (탭 표시 텍스트:"TAB 2"), (페이지 뷰:"content2")
        TabHost.TabSpec ts2 = tabHost1.newTabSpec("Tab Spec 2");
        ts2.setContent(R.id.content2);
        ts2.setIndicator("최소요금");
        tabHost1.addTab(ts2);

        startStnTextView1 = findViewById(R.id.startStnText1);
        endStnTextView1 = findViewById(R.id.endStnText1);
        routeStnTextView1 = findViewById(R.id.routeStnTextView1);

        startStnTextView2 = findViewById(R.id.startStnText2);
        endStnTextView2 = findViewById(R.id.endStnText2);
        routeStnTextView2 = findViewById(R.id.routeStnTextView2);

        startStnTextView1.setText(getIntent().getStringExtra("startStn"));
        endStnTextView1.setText(getIntent().getStringExtra("endStn"));
        routeStnTextView1.setText(getIntent().getStringExtra("routeStn"));

        startStnTextView2.setText(getIntent().getStringExtra("startStn"));
        endStnTextView2.setText(getIntent().getStringExtra("endStn"));
        routeStnTextView2.setText(getIntent().getStringExtra("route2Stn"));

    }
    public void btnClick(View view){

            FeedTemplate params = FeedTemplate
                    .newBuilder(ContentObject.newBuilder("동행_지하철어플리케이션",
                            "https://mblogthumb-phinf.pstatic.net/20150625_78/swing_v_1435170768415IrPQR_GIF/_%BF%F2%C1%F7_%C6%FE%BE%E7%BB%F5_%B0%A1%B0%ED%C0־%EE.gif?type=w2",
                            LinkObject.newBuilder().setWebUrl("https://developers.kakao.com")
                                    .setMobileWebUrl("https://developers.kakao.com").build())
                            .setDescrption("분 이후도착예정입니다.")
                            .build())
                    .addButton(new ButtonObject("웹에서 보기", LinkObject.newBuilder().setWebUrl("https://developers.kakao.com").setMobileWebUrl("https://developers.kakao.com").build()))
                    .addButton(new ButtonObject("앱에서 보기", LinkObject.newBuilder()
                            .setWebUrl("https://developers.kakao.com")
                            .setMobileWebUrl("https://developers.kakao.com")
                            .setAndroidExecutionParams("key1=value1")
                            .setIosExecutionParams("key1=value1")
                            .build()))
                    .build();

            Map<String, String> serverCallbackArgs = new HashMap<String, String>();
            serverCallbackArgs.put("user_id", "${current_user_id}");
            serverCallbackArgs.put("product_id", "${shared_product_id}");


            KakaoLinkService.getInstance().sendDefault(this, params, new ResponseCallback <KakaoLinkResponse>() {
                @Override
                public void onFailure(ErrorResult errorResult) {}

                @Override
                public void onSuccess(KakaoLinkResponse result) {
                }
            });

    }
//        public void shareKakao(){
////            final KakaoLink kakaoLink = KakaoLink.getKakaoLink(this);
////            final KakaoTalkLinkMessageBuilder kakaoBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
//
//
//            FeedTemplate params = FeedTemplate
//                    .newBuilder(ContentObject.newBuilder("디저트 사진",
//                            "http://mud-kage.kakao.co.kr/dn/NTmhS/btqfEUdFAUf/FjKzkZsnoeE4o19klTOVI1/openlink_640x640s.jpg",
//                            LinkObject.newBuilder().setWebUrl("https://developers.kakao.com")
//                                    .setMobileWebUrl("https://developers.kakao.com").build())
//                            .setDescrption("아메리카노, 빵, 케익")
//                            .build())
//                    .setSocial(SocialObject.newBuilder().setLikeCount(10).setCommentCount(20)
//                            .setSharedCount(30).setViewCount(40).build())
//                    .addButton(new ButtonObject("웹에서 보기", LinkObject.newBuilder().setWebUrl("https://developers.kakao.com").setMobileWebUrl("https://developers.kakao.com").build()))
//                    .addButton(new ButtonObject("앱에서 보기", LinkObject.newBuilder()
//                            .setWebUrl("https://developers.kakao.com")
//                            .setMobileWebUrl("https://developers.kakao.com")
//                            .setAndroidExecutionParams("key1=value1")
//                            .setIosExecutionParams("key1=value1")
//                            .build()))
//                    .build();
//
//            Map<String, String> serverCallbackArgs = new HashMap<String, String>();
//            serverCallbackArgs.put("user_id", "${current_user_id}");
//            serverCallbackArgs.put("product_id", "${shared_product_id}");
//
//
//            KakaoLinkService.getInstance().sendDefault(this, params, new ResponseCallback <KakaoLinkResponse>() {
//                @Override
//                public void onFailure(ErrorResult errorResult) {}
//
//                @Override
//                public void onSuccess(KakaoLinkResponse result) {}
//            });




//            kakaoBuilder.addText("카카오링크 테스트");
//
//            String url = "https://ifh.cc/g/fwA8V.png";
//            kakaoBuilder.addImage(url, 160, 100);
//
//            kakaoBuilder.addAppButton("앱 실행");
//
//            kakaoLink.sendMessage(kakaoBuilder, this);

    }
//}
