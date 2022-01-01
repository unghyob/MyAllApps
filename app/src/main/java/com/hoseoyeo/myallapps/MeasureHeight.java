package com.hoseoyeo.myallapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class MeasureHeight extends AppCompatActivity implements View.OnClickListener, SensorEventListener {
    Button btnSet;
    TextView txtHeightContent;
    SensorManager sensorManager;
    List<Sensor> sensors;

    final int DATASIZE = 40;

    float stdPressure = 0;
    boolean flag = false;
    float sensorData[] = new float[DATASIZE];
    int pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measure_height);

        setDefaults();
    }

    private void setDefaults() {
        btnSet = (Button) findViewById(R.id.btnmeasureheightset);
        txtHeightContent = (TextView) findViewById(R.id.txtmeasureheightheight);

        btnSet.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnmeasureheightset:
                if (flag) {
                    sensorManager.unregisterListener(this);
                    flag = false;
                } else {
                    sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
                    sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
                    Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
                    sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
                }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_PRESSURE) {
            // 기준 기압 측정
            if (!flag) {
                if (pos < DATASIZE) {
                    // 일정 갯수 수집
                    sensorData[pos++] = sensorEvent.values[0];
                } else {
                    // 일정 갯수 수집 완료 -> 정렬
                    sort(sensorData);

                    // 일정 갯수 수집 완료 -> 정렬된 데이터 앞 뒤 데이터 제거 후 평균 구하기
                    int start = DATASIZE / 3;
                    int end = DATASIZE / 3 * 2;
                    float avg = getAverage(sensorData, start, end);
                    System.out.println("기압 평균 : " + avg);
                    stdPressure = avg;
                    System.out.println("기준 기압 : " + stdPressure);
                    pos = 0;
                    sensorData[pos++] = sensorEvent.values[0];
                    flag = true;
                }
            }
            // 기준 기압 측정 완료시
            else {
                if (pos < DATASIZE) {
                    // 일정 갯수 수집
                    sensorData[pos++] = sensorEvent.values[0];
                } else {
                    // 일정 갯수 수집 완료 -> 정렬
                    sort(sensorData);

                    // 일정 갯수 수집 완료 -> 정렬된 데이터 앞 뒤 데이터 제거 후 평균 구하기
                    int start = DATASIZE / 3;
                    int end = DATASIZE / 3 * 2;
                    float avg = getAverage(sensorData, start, end);
                    float height = 44332 * (float) (1 - Math.pow(avg / stdPressure, 0.19026));
                    height = (float) (Math.round(height * 10) / 10.0);

                    System.out.println("기준 기압 : " + stdPressure + ", 현재 기압 : " + avg + ", 기압차 : "
                            + (stdPressure - avg) + ", 현재 고도 : " + height);

                    txtHeightContent.setText(height + "");
                    ((TextView)findViewById(R.id.txttemp)).setText((int) height+"");

                    pos = 0;
                    sensorData[pos++] = sensorEvent.values[0];
                }
            }
        }
    }

    private void sort(float array[]) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length - i - 1; j++) {
                if (array[j] > array[j + 1]) {
                    float temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
    }

    private float getAverage(float array[], int start, int end) {
        float sum = 0;
        for (int i = start; i < end; i++) {
            sum += array[i];
        }
        return sum / (end - start);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        sensorManager.unregisterListener(this);
    }
}
