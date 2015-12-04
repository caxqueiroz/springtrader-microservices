package io.pivotal.springtrader.quotes.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


/**
 * Created by cax on 28/11/2015.
 */
@Document(collection = "Stocks")
public class Stock {

    @Id
    @JsonProperty("Symbol")
    private String symbol;

    @LastModifiedDate
    @JsonIgnore
    private DateTime lastModifiedDate;

    @Indexed()
    @JsonProperty("Name")
    private String name;

    @JsonProperty("Exchange")
    private String exchange;

    @JsonProperty("Status")
    private String status;

    @JsonProperty(value = "LastPrice")
    private Double lastPrice;

    @JsonProperty("Change")
    private Double change;

    @JsonProperty("ChangePercent")
    private Double changePercent;

    @JsonProperty("Timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "EEE MMM dd HH:mm:ss zzzXXX yyyy", locale = "ENGLISH")
    private Date timestamp;

    @JsonProperty("MSDate")
    private Double mSDate;

    @JsonProperty("MarketCap")
    private Double marketCap;

    @JsonProperty("Volume")
    private Integer volume;

    @JsonProperty("ChangeYTD")
    private Double changeYTD;

    @JsonProperty("ChangePercentYTD")
    private Double changePercentYTD;

    @JsonProperty("High")
    private Double high;

    @JsonProperty("Low")
    private Double low;

    @JsonProperty("Open")
    private Double open;

    public DateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(DateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(Double lastPrice) {
        this.lastPrice = lastPrice;
    }

    public Double getChange() {
        return change;
    }

    public void setChange(Double change) {
        this.change = change;
    }

    public Double getChangePercent() {
        return changePercent;
    }

    public void setChangePercent(Double changePercent) {
        this.changePercent = changePercent;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Double getmSDate() {
        return mSDate;
    }

    public void setmSDate(Double mSDate) {
        this.mSDate = mSDate;
    }

    public Double getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(Double marketCap) {
        this.marketCap = marketCap;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public Double getChangeYTD() {
        return changeYTD;
    }

    public void setChangeYTD(Double changeYTD) {
        this.changeYTD = changeYTD;
    }

    public Double getChangePercentYTD() {
        return changePercentYTD;
    }

    public void setChangePercentYTD(Double changePercentYTD) {
        this.changePercentYTD = changePercentYTD;
    }

    public Double getHigh() {
        return high;
    }

    public void setHigh(Double high) {
        this.high = high;
    }

    public Double getLow() {
        return low;
    }

    public void setLow(Double low) {
        this.low = low;
    }

    public Double getOpen() {
        return open;
    }

    public void setOpen(Double open) {
        this.open = open;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "symbol='" + symbol + '\'' +
                ", name='" + name + '\'' +
                ", exchange='" + exchange + '\'' +
                ", status='" + status + '\'' +
                ", lastPrice=" + lastPrice +
                ", change=" + change +
                ", changePercent=" + changePercent +
                ", timestamp=" + timestamp +
                ", mSDate=" + mSDate +
                ", marketCap=" + marketCap +
                ", volume=" + volume +
                ", changeYTD=" + changeYTD +
                ", changePercentYTD=" + changePercentYTD +
                ", high=" + high +
                ", low=" + low +
                ", open=" + open +
                '}';
    }
}
