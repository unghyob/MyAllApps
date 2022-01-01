package com.hoseoyeo.myallapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HouseOutWeather extends AppCompatActivity {
    final int DUSTY_10 = 1;
    final int DUSTY_2_5 = 2;
    final int DUSTY_1_0 = 3;

    final int[] DUSTY_10_STATUS = {30, 80, 150};
    final int[] DUSTY_2_5_STATUS = {15, 35, 75};


    Button btnGetHtml;
    TextView txtTemperature, txtHumidity, txtDust10, txtDust2_5, txtDust1_0, txttime;
    TextView txtDust10_status, txtDust2_5_status, txtDust1_0_status;
    WebView webTemp, webHumi, webDust10, webDust2_5, webDust1_0;
    ImageView imgDiscomport;

    Handler handler = new Handler() {
        String getMessage;

        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    break;
                case 1:
                    getMessage = (String) msg.obj;

                    try {
                        JSONObject jsonObject = new JSONObject(getMessage);
                        JSONObject jsonFeeds = (JSONObject) ((JSONArray) jsonObject.get("feeds")).get(0);
                        System.out.println(jsonFeeds);
                        double temp = jsonFeeds.getDouble("field1");
                        double humi = jsonFeeds.getDouble("field2");
                        int dust10 = jsonFeeds.getInt("field3");
                        int dust2_5 = jsonFeeds.getInt("field4");
                        int dust1_0 = jsonFeeds.getInt("field5");

                        // 날씨 정보 자료 입력
                        txtTemperature.setText(String.format("%.1f", temp) + " ºC");
                        txtHumidity.setText(String.format("%.1f", humi) + " %");
                        txtDust10.setText(dust10 + "");
                        txtDust2_5.setText(dust2_5 + "");
                        txtDust1_0.setText(dust1_0 + "");

                        // 불쾌지수 입력
                        double discomport = 0.81 * temp + 0.01 * humi * (0.99 * temp - 14.3) + 46.3;


                        // 미세먼지 상태 입력
                        String result;
                        result = getResultOfDusty(dust10, DUSTY_10);
                        if (result.equals("   좋음")) {
                            txtDust10_status.setBackgroundColor(HouseOutWeather.this.getResources().getColor(R.color.DodgerBlue));
                        } else if (result.equals("   보통")) {
                            txtDust10_status.setBackgroundColor(HouseOutWeather.this.getResources().getColor(R.color.SeaGreen));
                        } else if (result.equals("   나쁨")) {
                            txtDust10_status.setBackgroundColor(HouseOutWeather.this.getResources().getColor(R.color.Chocolate));
                        } else {
                            txtDust10_status.setBackgroundColor(HouseOutWeather.this.getResources().getColor(R.color.DarkRed));
                        }
                        txtDust10_status.setText(result);

                        result = getResultOfDusty(dust2_5, DUSTY_2_5);
                        if (result.equals("   좋음")) {
                            txtDust2_5_status.setBackgroundColor(HouseOutWeather.this.getResources().getColor(R.color.DodgerBlue));
                        } else if (result.equals("   보통")) {
                            txtDust2_5_status.setBackgroundColor(HouseOutWeather.this.getResources().getColor(R.color.SeaGreen));
                        } else if (result.equals("   나쁨")) {
                            txtDust2_5_status.setBackgroundColor(HouseOutWeather.this.getResources().getColor(R.color.Chocolate));
                        } else {
                            txtDust2_5_status.setBackgroundColor(HouseOutWeather.this.getResources().getColor(R.color.DarkRed));
                        }
                        txtDust2_5_status.setText(result);

                        result = getResultOfDusty(dust10, DUSTY_10);
                        if (result.equals("   좋음")) {
                            txtDust1_0_status.setBackgroundColor(HouseOutWeather.this.getResources().getColor(R.color.DodgerBlue));
                        } else if (result.equals("   보통")) {
                            txtDust1_0_status.setBackgroundColor(HouseOutWeather.this.getResources().getColor(R.color.SeaGreen));
                        } else if (result.equals("   나쁨")) {
                            txtDust1_0_status.setBackgroundColor(HouseOutWeather.this.getResources().getColor(R.color.Chocolate));
                        } else {
                            txtDust1_0_status.setBackgroundColor(HouseOutWeather.this.getResources().getColor(R.color.DarkRed));
                        }
                        txtDust1_0_status.setText(result);

                        // 그래프 띄우기
                        webTemp.getSettings().setJavaScriptEnabled(true);
                        webHumi.getSettings().setJavaScriptEnabled(true);
                        webDust10.getSettings().setJavaScriptEnabled(true);
                        webDust2_5.getSettings().setJavaScriptEnabled(true);
                        webDust1_0.getSettings().setJavaScriptEnabled(true);

                        webTemp.setInitialScale(250); // 35% initialScale to fit
                        webHumi.setInitialScale(250); // 35% initialScale to fit
                        webDust10.setInitialScale(250); // 35% initialScale to fit
                        webDust2_5.setInitialScale(250); // 35% initialScale to fit
                        webDust1_0.setInitialScale(250); // 35% initialScale to fit

                        webTemp.loadUrl(Re.weather.graphUrl + "/" + 1 + "?" + Re.weather.praphOption);
                        webHumi.loadUrl(Re.weather.graphUrl + "/" + 2 + "?" + Re.weather.praphOption);
                        webDust10.loadUrl(Re.weather.graphUrl + "/" + 3 + "?" + Re.weather.praphOption);
                        webDust2_5.loadUrl(Re.weather.graphUrl + "/" + 4 + "?" + Re.weather.praphOption);
                        webDust1_0.loadUrl(Re.weather.graphUrl + "/" + 5 + "?" + Re.weather.praphOption);

                    } catch (Exception e) {
                        System.out.println("JSON 파싱 에러");
                        System.out.println(e.getMessage());
                        System.out.println(e.getStackTrace());
                    }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_out_weather);

        setDefaults();

        htmlDown downThread = new htmlDown(handler);
        downThread.start();
        SimpleDateFormat form = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분");
        Date date = new Date();
        txttime.setText(form.format(date).toString());
    }

    private void setDefaults() {
        btnGetHtml = (Button) findViewById(R.id.btnhouseoutweatherdown);

        txtTemperature = (TextView) findViewById(R.id.txthouseoutweathertemperature);
        txtHumidity = (TextView) findViewById(R.id.txthouseoutweatherhumidity);
        txtDust10 = (TextView) findViewById(R.id.txthouseoutweatherdust10);
        txtDust2_5 = (TextView) findViewById(R.id.txthouseoutweatherdust2_5);
        txtDust1_0 = (TextView) findViewById(R.id.txthouseoutweatherdust1_0);
        txttime = (TextView) findViewById(R.id.txthouseoutweathertime);

        txtDust10_status = (TextView) findViewById(R.id.txthouseoutweatherdust10_status);
        txtDust2_5_status = (TextView) findViewById(R.id.txthouseoutweatherdust2_5_status);
        txtDust1_0_status = (TextView) findViewById(R.id.txthouseoutweatherdust1_0_status);

        webTemp = (WebView) findViewById(R.id.webhouseoutweathertemp);
        webHumi = (WebView) findViewById(R.id.webhouseoutweatherhumi);
        webDust10 = (WebView) findViewById(R.id.webhouseoutweatherdust10);
        webDust2_5 = (WebView) findViewById(R.id.webhouseoutweatherdust2_5);
        webDust1_0 = (WebView) findViewById(R.id.webhouseoutweatherdust1_0);

        imgDiscomport = (ImageView) findViewById(R.id.imgdiscomport);

        onClickListener listener = new onClickListener();

        btnGetHtml.setOnClickListener(listener);
    }

    private String getResultOfDusty(int value, int dusty) {
        String result = "";
        int[] status = null;
        switch (dusty) {
            case DUSTY_10:
                status = DUSTY_10_STATUS;
                break;
            case DUSTY_2_5:
                status = DUSTY_2_5_STATUS;
                break;
            case DUSTY_1_0:
                status = DUSTY_2_5_STATUS;
                break;
        }

        for (int i = 0; i < 3; i++) {
            if (value < status[i]) {
                result = (i == 0 ? "   좋음" : (i == 1 ? "   보통" : (i == 2 ? "   나쁨" : "   매우나쁨")));
                break;
            }
        }

        return result;
    }

    class onClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnhouseoutweatherdown:
                    htmlDown downThread = new htmlDown(handler);
                    downThread.start();
                    SimpleDateFormat form = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분");
                    Date date = new Date();
                    txttime.setText(form.format(date).toString());
                    break;
            }
        }
    }

    class htmlDown extends Thread {
        Handler handler = null;
        String strUrl = null;

        public htmlDown(Handler handler) {
            this.handler = handler;

            strUrl = Re.weather.url + "/" + Re.weather.fileName + "?api_key=" + Re.weather.read_api_key + "&results=" + 1;
        }

        @Override
        public void run() {
            String jsonStr = null;
            try {
                jsonStr = Jsoup.connect(strUrl).timeout(4000)
                        .userAgent("Mozilla")
                        .ignoreContentType(true)
                        .execute().body();
                System.out.println(jsonStr);

                Message message = new Message();
                message.what = 1;
                message.obj = jsonStr;
                handler.sendMessage(message);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println(e.getStackTrace());
                System.out.println("날씨 json 다운 중 에러 발생");
            }
        }
    }
}