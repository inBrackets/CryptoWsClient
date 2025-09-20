package org.example.cryptowsclient.candlestick;

import org.example.cryptowsclient.candlestick.enums.TimeFrame;

public class GetCandlestickUrlBuilder {

    private static final String BASE_URL = "https://api.crypto.com/exchange/v1/public/get-candlestick";

    private String instrumentName;
    private String timeframe;
    private Integer count = 300;
    private Long endTs;
    private Long startTs;

    public static GetCandlestickUrlBuilder create() {
        return new GetCandlestickUrlBuilder();
    }

    public GetCandlestickUrlBuilder instrumentName(String instrumentName) {
        this.instrumentName = instrumentName;
        return this;
    }

    public GetCandlestickUrlBuilder timeframe(TimeFrame timeframe) {
        this.timeframe = timeframe.getSymbol();
        return this;
    }

    public GetCandlestickUrlBuilder count(Integer count) {
        this.count = count;
        return this;
    }

    public GetCandlestickUrlBuilder endTs(Long endTs) {
        this.endTs = endTs;
        return this;
    }

    public GetCandlestickUrlBuilder startTs(Long startTs) {
        this.startTs = startTs;
        return this;
    }

    public String build() {
        StringBuilder sb = new StringBuilder(BASE_URL);
        String prefix = "?";

        if (instrumentName != null) {
            sb.append(prefix).append("instrument_name=").append(instrumentName);
            prefix = "&";
        }
        if (timeframe != null) {
            sb.append(prefix).append("timeframe=").append(timeframe);
            prefix = "&";
        }
        if (count != null) {
            sb.append(prefix).append("count=").append(count);
            prefix = "&";
        }
        if (endTs != null) {
            sb.append(prefix).append("end_ts=").append(endTs);
            prefix = "&";
        }
        if (startTs != null) {
            sb.append(prefix).append("start_ts=").append(startTs);
        }

        return sb.toString();
    }
}
