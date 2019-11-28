package com.example.myapplication.Utiles;

import com.example.myapplication.R;

// 호선정보 추상 클래스
public abstract class SubwayLine {
    public static String[] lineNm = {
            "A호선", "B호선", "C호선", "D호선",
            "E호선", "F호선", "G호선"};
    private static String[] lineSymbol = {
            "A", "B", "C", "D",
            "E", "F", "G"};
    private static String[] lineColor = {
            "#009246","#00498B", "#8E764B", "#BB1833",
            "#E0A134", "#CC660D", "#6E98BB" };
    private static int[] lineResId = {
            R.drawable.line_first, R.drawable.line_second, R.drawable.line_third, R.drawable.line_fourth,
            R.drawable.line_fifth, R.drawable.line_sixth, R.drawable.line_seventh };
    private static int[] lineBgResId = {
            R.drawable.bg_line_first, R.drawable.bg_line_second, R.drawable.bg_line_third, R.drawable.bg_line_fourth,
            R.drawable.bg_line_fifth, R.drawable.bg_line_sixth, R.drawable.bg_line_seventh};

    public static String getLineSymbol(String mLineNm) {
        for (int i = 0; i < lineNm.length; i++)
            if (mLineNm.equals(lineNm[i]))
                return lineSymbol[i];
        return null;
    }

    public static int getResId(String mLineNm) {
        for (int i = 0; i < lineNm.length; i++)
            if (mLineNm.equals(lineNm[i]))
                return lineResId[i];
        return -1;
    }

    public static String getLineColor(String mLineSymbol) {
        for (int i = 0; i < lineSymbol.length; i++) {
            if (mLineSymbol.equals(lineSymbol[i]))
                return lineColor[i];
        }
        return null;
    }

    public static int getBgResId(String mLineNm) {
        for (int i = 0; i < lineNm.length; i++)
            if (mLineNm.equals(lineNm[i]))
                return lineBgResId[i];
        return -1;
    }

    public static String getLineNm(String mLineSymbol) {
        for (int i = 0; i < lineNm.length; i++)
            if (mLineSymbol.equals(lineSymbol[i]))
                return lineNm[i];
        return null;
    }

}
