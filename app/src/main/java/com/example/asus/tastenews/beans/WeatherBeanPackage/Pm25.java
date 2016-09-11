package com.example.asus.tastenews.beans.WeatherBeanPackage;

import java.util.HashMap;
import java.util.Map;

public class Pm25 {

    private String key;
    private Integer showDesc;
    private Pm25_ pm25;
    private String dateTime;
    private String cityName;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The key
     */
    public String getKey() {
        return key;
    }

    /**
     *
     * @param key
     * The key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     *
     * @return
     * The showDesc
     */
    public Integer getShowDesc() {
        return showDesc;
    }

    /**
     *
     * @param showDesc
     * The show_desc
     */
    public void setShowDesc(Integer showDesc) {
        this.showDesc = showDesc;
    }

    /**
     *
     * @return
     * The pm25
     */
    public Pm25_ getPm25() {
        return pm25;
    }

    /**
     *
     * @param pm25
     * The pm25
     */
    public void setPm25(Pm25_ pm25) {
        this.pm25 = pm25;
    }

    /**
     *
     * @return
     * The dateTime
     */
    public String getDateTime() {
        return dateTime;
    }

    /**
     *
     * @param dateTime
     * The dateTime
     */
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    /**
     *
     * @return
     * The cityName
     */
    public String getCityName() {
        return cityName;
    }

    /**
     *
     * @param cityName
     * The cityName
     */
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}