package com.example.asus.tastenews.beans.WeatherBeanPackage;

import java.util.HashMap;
import java.util.Map;

public class Weather_ {

    private String date;
    private Info_ info;
    private String week;
    private String nongli;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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
     * The info
     */
    public Info_ getInfo() {
        return info;
    }

    /**
     *
     * @param info
     * The info
     */
    public void setInfo(Info_ info) {
        this.info = info;
    }

    /**
     *
     * @return
     * The week
     */
    public String getWeek() {
        return week;
    }

    /**
     *
     * @param week
     * The week
     */
    public void setWeek(String week) {
        this.week = week;
    }

    /**
     *
     * @return
     * The nongli
     */
    public String getNongli() {
        return nongli;
    }

    /**
     *
     * @param nongli
     * The nongli
     */
    public void setNongli(String nongli) {
        this.nongli = nongli;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
