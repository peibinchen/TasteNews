package com.example.asus.tastenews.beans.WeatherBeanPackage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Info_ {

    private List<String> night = new ArrayList<String>();
    private List<String> day = new ArrayList<String>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The night
     */
    public List<String> getNight() {
        return night;
    }

    /**
     *
     * @param night
     * The night
     */
    public void setNight(List<String> night) {
        this.night = night;
    }

    /**
     *
     * @return
     * The day
     */
    public List<String> getDay() {
        return day;
    }

    /**
     *
     * @param day
     * The day
     */
    public void setDay(List<String> day) {
        this.day = day;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
