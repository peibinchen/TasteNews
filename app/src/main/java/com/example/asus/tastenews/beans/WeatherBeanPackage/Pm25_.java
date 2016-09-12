package com.example.asus.tastenews.beans.WeatherBeanPackage;

import java.util.HashMap;
import java.util.Map;

public class Pm25_ {

    private String curPm;
    private String pm25;
    private String pm10;
    private Integer level;
    private String quality;
    private String des;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The curPm
     */
    public String getCurPm() {
        return curPm;
    }

    /**
     *
     * @param curPm
     * The curPm
     */
    public void setCurPm(String curPm) {
        this.curPm = curPm;
    }

    /**
     *
     * @return
     * The pm25
     */
    public String getPm25() {
        return pm25;
    }

    /**
     *
     * @param pm25
     * The pm25
     */
    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }

    /**
     *
     * @return
     * The pm10
     */
    public String getPm10() {
        return pm10;
    }

    /**
     *
     * @param pm10
     * The pm10
     */
    public void setPm10(String pm10) {
        this.pm10 = pm10;
    }

    /**
     *
     * @return
     * The level
     */
    public Integer getLevel() {
        return level;
    }

    /**
     *
     * @param level
     * The level
     */
    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     *
     * @return
     * The quality
     */
    public String getQuality() {
        return quality;
    }

    /**
     *
     * @param quality
     * The quality
     */
    public void setQuality(String quality) {
        this.quality = quality;
    }

    /**
     *
     * @return
     * The des
     */
    public String getDes() {
        return des;
    }

    /**
     *
     * @param des
     * The des
     */
    public void setDes(String des) {
        this.des = des;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}