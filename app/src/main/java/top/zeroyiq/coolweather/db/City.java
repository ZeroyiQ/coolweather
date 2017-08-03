package top.zeroyiq.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by ZeroyiQ on 2017/8/3.
 */

public class City extends DataSupport {

    private int id;             // 市 ID

    private String cityName;    // 市名

    private int cityCode;       // 市代号

    private int provinceId;     // 所属省 ID

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }
}
