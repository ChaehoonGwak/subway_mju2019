package com.example.myapplication.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myapplication.R;

public class SitelinkActivity extends AppCompatActivity {

    private ImageButton btnWeather;
    private ImageButton btnWebtoon;
    private ImageButton btnMusic;
    private ImageButton btnShopping;
    private ImageButton btnMovie;
    private ImageButton btnTravel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitelink);

        //날씨
        btnWeather = findViewById(R.id.weather);
        btnWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://weather.naver.com/"));
                startActivity(intent);
            }
        });
        //웹툰
        btnWebtoon = findViewById(R.id.webtoon);
        btnWebtoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://comic.naver.com/index.nhn"));
                startActivity(intent);
            }
        });
        //음악
        btnMusic = findViewById(R.id.music);
        btnMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://music.naver.com/"));
                startActivity(intent);
            }
        });
        //쇼핑
        btnShopping = findViewById(R.id.shopping);
        btnShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://shopping.naver.com/"));
                startActivity(intent);
            }
        });
        //영화
        btnMovie= findViewById(R.id.movie);
        btnMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://movie.naver.com/"));
                startActivity(intent);
            }
        });
        //여행
        btnTravel = findViewById(R.id.travel);
        btnTravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://flight.naver.com/flights/"));
                startActivity(intent);
            }
        });

    }
}
