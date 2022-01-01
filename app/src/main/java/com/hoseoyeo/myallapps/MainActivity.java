package com.hoseoyeo.myallapps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button btnHouseOutWeather, btnMeasureHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setDefaults();
    }

    private void setDefaults() {
        btnOnClickListener listener = new btnOnClickListener();
        btnHouseOutWeather = (Button) findViewById(R.id.btnhomehoutweather);
        btnMeasureHeight = (Button)findViewById(R.id.btnmeasureheight);

        btnHouseOutWeather.setOnClickListener(listener);
        btnMeasureHeight.setOnClickListener(listener);
    }

    class btnOnClickListener implements View.OnClickListener {
        Intent intent;

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnhomehoutweather:
                    intent = new Intent(MainActivity.this, HouseOutWeather.class);
                    startActivity(intent);
                    break;
                case R.id.btnmeasureheight:
                    intent = new Intent(MainActivity.this, MeasureHeight.class);
                    startActivity(intent);
            }
        }
    }
}