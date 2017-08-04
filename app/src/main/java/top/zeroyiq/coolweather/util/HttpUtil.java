package top.zeroyiq.coolweather.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 *  网路连接模块
 * Created by ZeroyiQ on 2017/8/3.
 */

public class HttpUtil {
    public static void sendOkHttpRequest(String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);


    }
}
