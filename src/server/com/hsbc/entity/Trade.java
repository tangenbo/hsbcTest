package server.com.hsbc.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by etang on 2020/6/11
 */
public class Trade {
    @JsonProperty
    private long id;

    @JsonProperty
    private Long tradeDate;

    @JsonProperty
    private Integer tradeAmount;

    @JsonProperty
    private String tradeType;

    @JsonProperty
    private String currencyPair;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(Long tradeDate) {
        this.tradeDate = tradeDate;
    }

    public Integer getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(Integer tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getCurrencyPair() {
        return currencyPair;
    }

    public void setCurrencyPair(String currencyPair) {
        this.currencyPair = currencyPair;
    }
}
