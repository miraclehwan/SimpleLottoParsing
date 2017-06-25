package com.lottopasing;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView1 = (TextView) findViewById(R.id.textview1);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView4 = (TextView) findViewById(R.id.textView4);

        GetLottoNumberTask task = new GetLottoNumberTask();
        task.execute();



    }


    private class GetLottoNumberTask extends AsyncTask<Void, Void, Map<String,String>> {


        @Override
        protected Map<String, String> doInBackground(Void... params) {
            Map<String,String> result = new HashMap<String,String>();
            try {
                Document document = Jsoup.connect("http://nlotto.co.kr/common.do?method=main").get();
                Elements elements = document.select(".lotto_area #lottoDrwNo");
                result.put("latestLottoCount", elements.text());

                for( int i = 1; i < 7; i++ ) {
                    elements = document.select(".lotto_area #numView #drwtNo" + i );
                    result.put("number" + i, elements.attr("alt"));
                }

                elements = document.select(".lotto_area #numView #bnusNo");
                result.put("number7", elements.attr("alt"));

                elements = document.select(".lotto_area .winner_num #lottoNo1Su");
                result.put("winGameCount", elements.text());

                elements = document.select(".lotto_area .winner_money #lottoNo1SuAmount");
                result.put("winGameMoney", elements.text());

            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Map<String, String> map) {

            textView1.setText(map.get("latestLottoCount")+"회");
            textView2.setText(map.get("number1") + "," +
                    map.get("number2") + "," +
                    map.get("number3") + "," +
                    map.get("number4") + "," +
                    map.get("number5") + "," +
                    map.get("number6") + "\n보너스번호 : " +
                    map.get("number7"));
            textView3.setText(map.get("winGameCount")+"명");
            textView4.setText(map.get("winGameMoney")+"원");

            Log.e("회차 ", map.get("latestLottoCount")+"회");
            Log.e("당첨번호 ", map.get("number1") + "," +
                    map.get("number2") + "," +
                    map.get("number3") + "," +
                    map.get("number4") + "," +
                    map.get("number5") + "," +
                    map.get("number6") + " 보너스번호 : " +
                    map.get("number7"));
            Log.e("당첨인원 ", map.get("winGameCount")+"명");
            Log.e("당첨액 ", map.get("winGameMoney")+"원");


        }
    }

}
