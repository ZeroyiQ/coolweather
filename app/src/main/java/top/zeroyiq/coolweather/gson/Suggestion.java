package top.zeroyiq.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ZeroyiQ on 2017/8/3.
 */

public class Suggestion {
    @SerializedName("comf")
    public Comfort comfort;

    public class Comfort {
        @SerializedName("txt")
        public String info;
    }

    @SerializedName("cw")
    public CarWash carWash;

    public class CarWash {
        @SerializedName("txt")
        public String info;
    }

    @SerializedName("sport")
    public Sport sport;

    public class Sport {
        @SerializedName("txt")
        public String info;
    }
}
