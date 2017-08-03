package top.zeroyiq.coolweather;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import top.zeroyiq.coolweather.gson.Forecast;
import top.zeroyiq.coolweather.gson.Weather;
import top.zeroyiq.coolweather.util.HttpUtil;
import top.zeroyiq.coolweather.util.Utility;

public class WeatherActivity extends AppCompatActivity {

    @BindView(R.id.tv_title_city)
    TextView tvTitleCity;
    @BindView(R.id.tv_update_time)
    TextView tvUpdateTime;
    @BindView(R.id.tv_degree)
    TextView tvDegree;
    @BindView(R.id.tv_info)
    TextView tvInfo;
    @BindView(R.id.forecast_layout)
    LinearLayout forecastLayout;
    @BindView(R.id.tv_aqi)
    TextView tvAqi;
    @BindView(R.id.tv_pm2_5)
    TextView tv5;
    @BindView(R.id.tv_comfort)
    TextView tvComfort;
    @BindView(R.id.tv_cw)
    TextView tvCw;
    @BindView(R.id.tv_sport)
    TextView tvSport;
    @BindView(R.id.weather_layout)
    ScrollView weatherLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        // 查看缓存
        String weatherString = preferences.getString("weather", null);
//        if (weatherString != null) {
//            Weather weather = Utility.handleWeatherResponse(weatherString);     // 有缓存直接解析
//            showWeatherInfo(weather);
//        } else {
            String weatherId = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(weatherId);
//        }
    }

    /**
     * 根据天气 id 请求城市天气信息
     * @param weatherId
     */
    private void requestWeather(final String weatherId) {
        String weatherUri = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=9f0860ac30cc4a12bf58d4e9cc38c98a";
        HttpUtil.sendOkHtttpRequst(weatherUri, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String responseText = response.body().string();
                        final Weather weather = Utility.handleWeatherResponse(responseText);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (weather != null && "ok".equals(weather.status)) {
                                    SharedPreferences.Editor editor = PreferenceManager.
                                            getDefaultSharedPreferences(WeatherActivity.this).edit();
                                    editor.putString("weather", responseText);
                                    editor.apply();
                                    showWeatherInfo(weather);
                                } else {
                                    Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
        );
    }

    /**
     * 将 Weather 中的信息展示出来
     * @param weather
     */
    private void showWeatherInfo(Weather weather) {
        // 基本天气信息
        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature + "°C";
        String weatherInfo = weather.now.more.info;

        tvTitleCity.setText(cityName);
        tvUpdateTime.setText(updateTime);
        tvDegree.setText(degree);
        tvInfo.setText(weatherInfo);

        // 天气预报
        forecastLayout.removeAllViews();
        for (Forecast forecast :
                weather.forecastList) {
            View v = LayoutInflater.from(this).inflate(R.layout.forecast_itme, forecastLayout, false);
            TextView dateText = (TextView) v.findViewById(R.id.tv_date);
            TextView infoText = (TextView) v.findViewById(R.id.tv_item_info);
            TextView maxText = (TextView) v.findViewById(R.id.tv_max);
            TextView minText = (TextView) v.findViewById(R.id.tv_min);

            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            forecastLayout.addView(v);
        }

        // AQI && PM2.5
        if (weather.aqi != null) {
            tvAqi.setText(weather.aqi.city.aqi);
            tv5.setText(weather.aqi.city.pm25);
        }

        // 出行建议
        String comfort = "舒适度：" + weather.suggestion.comfort.info;
        String carWash = "洗车指数：" + weather.suggestion.carWash.info;
        String sport = "运动建议：" + weather.suggestion.sport.info;

        tvComfort.setText(comfort);
        tvCw.setText(carWash);
        tvSport.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);
    }
}
