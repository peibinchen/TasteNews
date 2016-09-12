package com.example.asus.tastenews.beans.WeatherBeanPackage;

import java.util.HashMap;
import java.util.Map;

public class Realtime {

    private Wind wind;
    private String time;
    private Weather weather;
    private Integer dataUptime;
    private String date;
    private String cityCode;
    private String cityName;
    private Integer week;
    private String moon;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The wind
     */
    public Wind getWind() {
        return wind;
    }

    /**
     *
     * @param wind
     * The wind
     */
    public void setWind(Wind wind) {
        this.wind = wind;
    }

    /**
     *
     * @return
     * The time
     */
    public String getTime() {
        return time;
    }

    /**
     *
     * @param time
     * The time
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     *
     * @return
     * The weather
     */
    public Weather getWeather() {
        return weather;
    }

    /**
     *
     * @param weather
     * The weather
     */
    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    /**
     *
     * @return
     * The dataUptime
     */
    public Integer getDataUptime() {
        return dataUptime;
    }

    /**
     *
     * @param dataUptime
     * The dataUptime
     */
    public void setDataUptime(Integer dataUptime) {
        this.dataUptime = dataUptime;
    }

    /**
     *
     * @return
     * The date
     */
    public String getDate() {
        return date;
    }

    /**
     *
     * @param date
     * The date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     *
     * @return
     * The cityCode
     */
    public String getCityCode() {
        return cityCode;
    }

    /**
     *
     * @param cityCode
     * The city_code
     */
    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
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
     * The city_name
     */
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    /**
     *
     * @return
     * The week
     */
    public Integer getWeek() {
        return week;
    }

    /**
     *
     * @param week
     * The week
     */
    public void setWeek(Integer week) {
        this.week = week;
    }

    /**
     *
     * @return
     * The moon
     */
    public String getMoon() {
        return moon;
    }

    /**
     *
     * @param moon
     * The moon
     */
    public void setMoon(String moon) {
        this.moon = moon;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}