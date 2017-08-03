package top.zeroyiq.coolweather.gson;

/**
 * Created by ZeroyiQ on 2017/8/3.
 */

public class AQI {
    public AQICity city;
    public class AQICity {
        public String aqi;

        public String pm25;
    }
}
