package com.example.asus.tastenews.beans.WeatherBeanPackage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Data {

    private Realtime realtime;
    private Life life;
    private List<Weather_> weather = new ArrayList<Weather_>();
    private Pm25 pm25;
    private Object date;
    private Integer isForeign;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The realtime
     */
    public Realtime getRealtime() {
        return realtime;
    }

    /**
     *
     * @param realtime
     * The realtime
     */
    public void setRealtime(Realtime realtime) {
        this.realtime = realtime;
    }

    /**
     *
     * @return
     * The life
     */
    public Life getLife() {
        return life;
    }

    /**
     *
     * @param life
     * The life
     */
    public void setLife(Life life) {
        this.life = life;
    }

    /**
     *
     * @return
     * The weather
     */
    public List<Weather_> getWeather() {
        return weather;
    }

    /**
     *
     * @param weather
     * The weather
     */
    public void setWeather(List<Weather_> weather) {
        this.weather = weather;
    }

    /**
     *
     * @return
     * The pm25
     */
    public Pm25 getPm25() {
        return pm25;
    }

    /**
     *
     * @param pm25
     * The pm25
     */
    public void setPm25(Pm25 pm25) {
        this.pm25 = pm25;
    }

    /**
     *
     * @return
     * The date
     */
    public Object getDate() {
        return date;
    }

    /**
     *
     * @param date
     * The date
     */
    public void setDate(Object date) {
        this.date = date;
    }

    /**
     *
     * @return
     * The isForeign
     */
    public Integer getIsForeign() {
        return isForeign;
    }

    /**
     *
     * @param isForeign
     * The isForeign
     */
    public void setIsForeign(Integer isForeign) {
        this.isForeign = isForeign;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}