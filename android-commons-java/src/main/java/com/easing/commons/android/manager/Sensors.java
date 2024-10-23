package com.easing.commons.android.manager;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.location.OnNmeaMessageListener;

import com.easing.commons.android.app.CommonApplication;
import com.easing.commons.android.event.EventBus;
import com.easing.commons.android.time.Times;

import java.text.DecimalFormat;

@SuppressWarnings("all")
public class Sensors {

    //启动GPS传感器一次，用于获取海拔
    public static void startLocationSensorOnce(Activity activity, String requestCode) {
        final long startTime = Times.millisOfNow();
        LocationManager locationManager = activity.getSystemService(LocationManager.class);
        locationManager.addNmeaListener(new OnNmeaMessageListener() {
            @Override
            public void onNmeaMessage(String message, long timestamp) {
                //时间过久，停止监听
                if (timestamp - startTime > 60 * 1000) {
                    locationManager.removeNmeaListener(this);
                    return;
                }
                //Context已销毁，停止监听
                if (activity.isFinishing()) {
                    locationManager.removeNmeaListener(this);
                    return;
                }
                //获取成功，停止监听，发布结果
                if (message.startsWith("$GPGGA") || message.startsWith("$GNGGA")) {
                    String[] tokens = message.split(",");
                    if (tokens.length < 10) return;
                    if (tokens[9].isEmpty()) return;
                    Double altitude = Double.parseDouble(tokens[9]);
                    locationManager.removeNmeaListener(this);
                    EventBus.core.emit("onAltitudeGet", requestCode, altitude);
                }
            }
        });
    }

    //启动压力传感器一次，用于获取海拔
    public static void startPressureSensorOnce(Activity activity, String requestCode) {
        SensorManager sensorManager = CommonApplication.ctx.getSystemService(SensorManager.class);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        if (sensor == null) return;
        sensorManager.registerListener(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (activity.isFinishing()) {
                    sensorManager.unregisterListener(this);
                    return;
                }
                float p = event.values[0];
                DecimalFormat df = new DecimalFormat("0.00");
                df.getRoundingMode();
                double altitude = 44330000 * (1 - (Math.pow((Double.parseDouble(df.format(p)) / 1013.25), (float) 1.0 / 5255.0)));
                sensorManager.unregisterListener(this);
                EventBus.core.emit("onPressureChange", requestCode, altitude);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {}
        }, sensor, SensorManager.SENSOR_DELAY_FASTEST);
    }
}
