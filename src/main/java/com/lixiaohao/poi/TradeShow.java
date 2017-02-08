package com.lixiaohao.poi;

/**
 * Created by lixiaohao on 2017/2/8
 *
 * @Description
 * @Create 2017-02-08 13:34
 * @Company
 */
public class TradeShow {

    /**xx*/
    private String tradeShowName;
    /**xx*/
    private String dateAttended;
    /**xx*/
    private String  hostCountryOrRegion;

    public TradeShow() {
    }

    public String getTradeShowName() {
        return tradeShowName;
    }

    public void setTradeShowName(String tradeShowName) {
        this.tradeShowName = tradeShowName;
    }

    public String getDateAttended() {
        return dateAttended;
    }

    public void setDateAttended(String dateAttended) {
        this.dateAttended = dateAttended;
    }

    public String getHostCountryOrRegion() {
        return hostCountryOrRegion;
    }

    public void setHostCountryOrRegion(String hostCountryOrRegion) {
        this.hostCountryOrRegion = hostCountryOrRegion;
    }

    @Override
    public String toString() {
        return "TradeShow{" +
                "tradeShowName='" + tradeShowName + '\'' +
                ", dateAttended='" + dateAttended + '\'' +
                ", hostCountryOrRegion='" + hostCountryOrRegion + '\'' +
                '}';
    }
}
