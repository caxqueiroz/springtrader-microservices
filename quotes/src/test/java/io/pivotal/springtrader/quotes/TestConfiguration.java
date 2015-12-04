package io.pivotal.springtrader.quotes;


import io.pivotal.springtrader.quotes.domain.Stock;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Defaults to use for the tests.
 *
 * @author David Ferreira Pinto
 */
public class TestConfiguration {

    public static final String QUOTE_SYMBOL = "AAPL";
    public static final String QUOTE_NAME = "Apple Inc";
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss zzzXXX yyyy", Locale.ENGLISH);
    public static final String QUOTE_DATE_STRING = "Wed May 6 00:00:00 UTC-04:00 2015";
    public static final double QUOTE_LAST_PRICE = 26.135;
    public static final double QUOTE_CHANGE = 0.00500000000000256d;
    public static final double QUOTE_CHANGE_PERCENT = 0.0191350937619692;
    public static final double QUOTE_MSDATE = 42130d;

    public static final String COMPANY_EXCHANGE = "NASDAQ";
    public static final String NULL_QUOTE_SYMBOL = "LALALALA";

    /*
     * {"Status":"SUCCESS","Name":"EMC Corp","Symbol":"EMC","LastPrice":26.135,
     * "Change":0.00500000000000256,"ChangePercent":0.0191350937619692,
     * "Timestamp":"Wed May 6 00:00:00 UTC-04:00 2015","MSDate":42130,
     * "MarketCap":50755764235,"Volume":15159291,"ChangeYTD":29.74,
     * "ChangePercentYTD":-12.1217215870881,"High":0,"Low":0,"Open":26.52}
     */
    public static Stock stock() {
        Stock quote = new Stock();
        quote.setStatus("SUCCESS");
        quote.setSymbol(QUOTE_SYMBOL);
        quote.setLastPrice(QUOTE_LAST_PRICE);
        quote.setChange(QUOTE_CHANGE);
        quote.setChangePercent(QUOTE_CHANGE_PERCENT);
        try {
            quote.setTimestamp(dateFormat.parse(QUOTE_DATE_STRING));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        quote.setmSDate(QUOTE_MSDATE);
        quote.setMarketCap(50755764235.00);
        quote.setVolume(15159291);
        quote.setChangeYTD(29.74);
        quote.setChangePercentYTD(-12.1217215870881);
        quote.setHigh(0.0);
        quote.setLow(0.0);
        quote.setOpen(26.52);


        quote.setExchange(COMPANY_EXCHANGE);
        quote.setName(QUOTE_NAME);
        quote.setSymbol(QUOTE_SYMBOL);
        return quote;

    }
}
