package com.example.asus.tastenews.beans.WeatherBeanPackage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Info {

    private List<String> kongtiao = new ArrayList<String>();
    private List<String> yundong = new ArrayList<String>();
    private List<String> ziwaixian = new ArrayList<String>();
    private List<String> ganmao = new ArrayList<String>();
    private List<String> xiche = new ArrayList<String>();
    private List<String> wuran = new ArrayList<String>();
    private List<String> chuanyi = new ArrayList<String>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The kongtiao
     */
    public List<String> getKongtiao() {
        return kongtiao;
    }

    /**
     *
     * @param kongtiao
     * The kongtiao
     */
    public void setKongtiao(List<String> kongtiao) {
        this.kongtiao = kongtiao;
    }

    /**
     *
     * @return
     * The yundong
     */
    public List<String> getYundong() {
        return yundong;
    }

    /**
     *
     * @param yundong
     * The yundong
     */
    public void setYundong(List<String> yundong) {
        this.yundong = yundong;
    }

    /**
     *
     * @return
     * The ziwaixian
     */
    public List<String> getZiwaixian() {
        return ziwaixian;
    }

    /**
     *
     * @param ziwaixian
     * The ziwaixian
     */
    public void setZiwaixian(List<String> ziwaixian) {
        this.ziwaixian = ziwaixian;
    }

    /**
     *
     * @return
     * The ganmao
     */
    public List<String> getGanmao() {
        return ganmao;
    }

    /**
     *
     * @param ganmao
     * The ganmao
     */
    public void setGanmao(List<String> ganmao) {
        this.ganmao = ganmao;
    }

    /**
     *
     * @return
     * The xiche
     */
    public List<String> getXiche() {
        return xiche;
    }

    /**
     *
     * @param xiche
     * The xiche
     */
    public void setXiche(List<String> xiche) {
        this.xiche = xiche;
    }

    /**
     *
     * @return
     * The wuran
     */
    public List<String> getWuran() {
        return wuran;
    }

    /**
     *
     * @param wuran
     * The wuran
     */
    public void setWuran(List<String> wuran) {
        this.wuran = wuran;
    }

    /**
     *
     * @return
     * The chuanyi
     */
    public List<String> getChuanyi() {
        return chuanyi;
    }

    /**
     *
     * @param chuanyi
     * The chuanyi
     */
    public void setChuanyi(List<String> chuanyi) {
        this.chuanyi = chuanyi;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}