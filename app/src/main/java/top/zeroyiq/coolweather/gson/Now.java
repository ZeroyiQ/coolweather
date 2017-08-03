package top.zeroyiq.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ZeroyiQ on 2017/8/3.
 */

public class Now {
    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    public class More {
        @SerializedName("txt")
        public String info;
    }


}
