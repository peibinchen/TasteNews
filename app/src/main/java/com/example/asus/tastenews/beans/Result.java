package com.example.asus.tastenews.beans;

import java.util.ArrayList;


public class Result {

    private java.util.List<com.example.asus.tastenews.beans.List> list = new ArrayList<com.example.asus.tastenews.beans.List>();
    private Integer totalPage;
    private Integer ps;
    private Integer pno;
//    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The list
     */
    public java.util.List<com.example.asus.tastenews.beans.List> getList() {
        return list;
    }

    /**
     *
     * @param list
     * The list
     */
    public void setList(java.util.List<com.example.asus.tastenews.beans.List> list) {
        this.list = list;
    }

    /**
     *
     * @return
     * The totalPage
     */
    public Integer getTotalPage() {
        return totalPage;
    }

    /**
     *
     * @param totalPage
     * The totalPage
     */
    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    /**
     *
     * @return
     * The ps
     */
    public Integer getPs() {
        return ps;
    }

    /**
     *
     * @param ps
     * The ps
     */
    public void setPs(Integer ps) {
        this.ps = ps;
    }

    /**
     *
     * @return
     * The pno
     */
    public Integer getPno() {
        return pno;
    }

    /**
     *
     * @param pno
     * The pno
     */
    public void setPno(Integer pno) {
        this.pno = pno;
    }

//    public Map<String, Object> getAdditionalProperties() {
//        return this.additionalProperties;
//    }
//
//    public void setAdditionalProperty(String name, Object value) {
//        this.additionalProperties.put(name, value);
//    }

}