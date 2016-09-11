package com.example.asus.tastenews.beans.WeatherBeanPackage;

import java.util.HashMap;
import java.util.Map;

public class Weather {

    private String humidity;
    private String img;
    private String info;
    private String temperature;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The humidity
     */
    public String getHumidity() {
        return humidity;
    }

    /**
     *
     * @param humidity
     * The humidity
     */
    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    /**
     *
     * @return
     * The img
     */
    public String getImg() {
        return img;
    }

    /**
     *
     * @param img
     * The img
     */
    public void setImg(String img) {
        this.img = img;
    }

    /**
     *
     * @return
     * The info
     */
    public String getInfo() {
        return info;
    }

    /**
     *
     * @param info
     * The info
     */
    public void setInfo(String info) {
        this.info = info;
    }

    /**
     *
     * @return
     * The temperature
     */
    public String getTemperature() {
        return temperature;
    }

    /**
     *
     * @param temperature
     * The temperature
     */
    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
