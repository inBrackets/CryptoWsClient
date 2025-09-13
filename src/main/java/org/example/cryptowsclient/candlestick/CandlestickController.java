package org.example.cryptowsclient.candlestick;

import lombok.AllArgsConstructor;
import org.example.cryptowsclient.candlestick.dto.CandlestickDto;
import org.example.cryptowsclient.candlestick.dto.CandlestickWithInstrumentNameDto;
import org.example.cryptowsclient.candlestick.enums.TimeFrame;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;

import java.util.List;

import static org.example.cryptowsclient.candlestick.enums.TimeFrame.FIFTEEN_MINUTES;

@RestController
@AllArgsConstructor
@RequestMapping("/api/candlestick")
public class CandlestickController {

    private final CandlestickService candlestickService;

    @GetMapping("/get-all")
    @CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8080"})
    private ResponseEntity<List<CandlestickWithInstrumentNameDto>> getAll(
            @RequestParam(value = "instrument_name", required = false, defaultValue = "CRO_USD") String instrumentName,
            @RequestParam(value = "time_frame", required = false, defaultValue = "1m") String timeFrame
    ) {
        return ResponseEntity
                .status(HttpStatus.OK).body(candlestickService.getCandlesticks(timeFrame, TimeFrame.fromSymbol(timeFrame)));
    }

    @GetMapping("/get-rsi")
    @CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8080"})
    private ResponseEntity<RSIIndicator> getRsi() {

        List<CandlestickWithInstrumentNameDto> candleSticks = candlestickService.getCandlesticks("CRO_USD", FIFTEEN_MINUTES);

        BarSeries series = Ta4jConverter.toBarSeries(candleSticks, FIFTEEN_MINUTES);

        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);

        RSIIndicator rsi = new RSIIndicator(closePrice, 14);

        for (int i = 0; i <= series.getEndIndex(); i++) {
            System.out.println("Index " + i + " | Time: " + series.getBar(i).getEndTime()
                    + " | RSI: " + rsi.getValue(i));
        }

        return ResponseEntity
                .status(HttpStatus.OK).body(rsi);
    }
}