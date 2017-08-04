package top.zeroyiq.coolweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import top.zeroyiq.coolweather.WeatherActivity;
import top.zeroyiq.coolweather.gson.Weather;
import top.zeroyiq.coolweather.util.HttpUtil;
import top.zeroyiq.coolweather.util.Utility;

/**
 * 后台自动更新天气
 * Created by ZeroyiQ on 2017/8/4.
 */

public class AutoUpdateService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWeather();
        updateBingPic();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int eightHours = 60 * 60 * 1000 * 8;        // 每八个小时一更新
        long triggerAtTime = SystemClock.elapsedRealtime() + eightHours;
        Intent i = new Intent(this, AutoUpdateService.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    private void updateBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                editor.putString("bing_pic", bingPic);
                editor.apply();
            }
        });
    }

    private void updateWeather() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String prefWeather = preferences.getString("weather", null);
        if (preferences != null) {
            Weather weather = Utility.handleWeatherResponse(prefWeather);
            String weatherId = weather.basic.weatherId;
            String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=9f0860ac30cc4a12bf58d4e9cc38c98a";

            HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String responseText = response.body().string();
                            final Weather weather = Utility.handleWeatherResponse(responseText);
                            if (weather != null && "ok".equals(weather.status)) {
                                SharedPreferences.Editor editor = PreferenceManager.
                                        getDefaultSharedPreferences(AutoUpdateService.this).edit();
                                editor.putString("weather", responseText);
                                editor.apply();
                            }
                        }
                    }
            );
        }
    }
}
