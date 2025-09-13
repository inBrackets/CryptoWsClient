package org.example.cryptowsclient.candlestick;

import org.example.cryptowsclient.candlestick.dto.CandlestickDto;
import org.example.cryptowsclient.candlestick.dto.CandlestickWithInstrumentNameDto;
import org.example.cryptowsclient.candlestick.enums.TimeFrame;
import org.ta4j.core.Bar;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBar;
import org.ta4j.core.BaseBarSeriesBuilder;
import org.ta4j.core.num.DecimalNum;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

public class Ta4jConverter {

    public static BarSeries toBarSeries(List<CandlestickWithInstrumentNameDto> candlesticks, TimeFrame timeFrame) {
        BarSeries series = new BaseBarSeriesBuilder().withName("MySeries").build();

        for (CandlestickWithInstrumentNameDto candlestick : candlesticks) {

            ZonedDateTime endTime = Instant.ofEpochMilli(candlestick.getTimestamp())
                    .atZone(ZoneOffset.UTC);

            Bar bar = new BaseBar(
                    timeFrame.getDuration(),
                    endTime.plusNanos(timeFrame.getDuration().toNanos()),
                    DecimalNum.valueOf(candlestick.getOpenPrice()),
                    DecimalNum.valueOf(candlestick.getHighPrice()),
                    DecimalNum.valueOf(candlestick.getLowPrice()),
                    DecimalNum.valueOf(candlestick.getClosePrice()),
                    DecimalNum.valueOf(candlestick.getVolume()),
                    DecimalNum.valueOf(0),
                    0L
            );

            series.addBar(bar);
        }

        return series;
    }
}