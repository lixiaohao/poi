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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TradeShow tradeShow = (TradeShow) o;

        if (tradeShowName != null ? !tradeShowName.equals(tradeShow.tradeShowName) : tradeShow.tradeShowName != null)
            return false;
        if (dateAttended != null ? !dateAttended.equals(tradeShow.dateAttended) : tradeShow.dateAttended != null)
            return false;
        return hostCountryOrRegion != null ? hostCountryOrRegion.equals(tradeShow.hostCountryOrRegion) : tradeShow.hostCountryOrRegion == null;

    }

    @Override
    public int hashCode() {
        int result = tradeShowName != null ? tradeShowName.hashCode() : 0;
        result = 31 * result + (dateAttended != null ? dateAttended.hashCode() : 0);
        result = 31 * result + (hostCountryOrRegion != null ? hostCountryOrRegion.hashCode() : 0);
        return result;
    }
}
