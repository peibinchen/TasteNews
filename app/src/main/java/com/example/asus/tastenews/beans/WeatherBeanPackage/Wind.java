package com.example.asus.tastenews.beans.WeatherBeanPackage;

import java.util.HashMap;
import java.util.Map;

public class Wind {

    private String windspeed;
    private String direct;
    private String power;
    private Object offset;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The windspeed
     */
    public String getWindspeed() {
        return windspeed;
    }

    /**
     *
     * @param windspeed
     * The windspeed
     */
    public void setWindspeed(String windspeed) {
        this.windspeed = windspeed;
    }

    /**
     *
     * @return
     * The direct
     */
    public String getDirect() {
        return direct;
    }

    /**
     *
     * @param direct
     * The direct
     */
    public void setDirect(String direct) {
        this.direct = direct;
    }

    /**
     *
     * @return
     * The power
     */
    public String getPower() {
        return power;
    }

    /**
     *
     * @param power
     * The power
     */
    public void setPower(String power) {
        this.power = power;
    }

    /**
     *
     * @return
     * The offset
     */
    public Object getOffset() {
        return offset;
    }

    /**
     *
     * @param offset
     * The offset
     */
    public void setOffset(Object offset) {
        this.offset = offset;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}