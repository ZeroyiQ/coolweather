package top.zeroyiq.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by ZeroyiQ on 2017/8/3.
 */

public class County extends DataSupport{
    private int id;                 // 县 ID

    private String countyName;      // 县名

    private String weatherId;       // 天气ID

    private int cityId;             // 所属市 ID

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
