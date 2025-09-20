package org.example.cryptowsclient.candlestick;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.cryptowsclient.candlestick.db.CandlestickEntity;
import org.example.cryptowsclient.candlestick.db.CandlestickId;
import org.example.cryptowsclient.candlestick.db.CandlestickRepository;
import org.example.cryptowsclient.candlestick.dto.CandlestickDto;
import org.example.cryptowsclient.candlestick.dto.CandlestickMapper;
import org.example.cryptowsclient.candlestick.dto.CandlestickWithInstrumentNameDto;
import org.example.cryptowsclient.candlestick.enums.TimeFrame;
import org.example.cryptowsclient.common.ApiResponseDto;
import org.example.cryptowsclient.common.ApiResultDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.lang.Thread.sleep;

@Service
@AllArgsConstructor
public class CandlestickService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final CandlestickRepository candlestickRepository;
    private static final String BASE_URL = "https://api.crypto.com/exchange/v1/";
    private final CandlestickMapper candlestickMapper;

    public ResponseEntity<ApiResponseDto<ApiResultDto<CandlestickDto>>> getLast300CandlesticksByInstrumentName(
            String instrumentName, TimeFrame timeframe
    ) {
        String url = GetCandlestickUrlBuilder.create()
                .instrumentName(instrumentName)
                .timeframe(timeframe)
                .build();

        ParameterizedTypeReference<ApiResponseDto<ApiResultDto<CandlestickDto>>> typeRef =
                new ParameterizedTypeReference<>() {
                };

        try {
            sleep(20L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return restTemplate.exchange(url, HttpMethod.GET, null, typeRef);
    }

    public ResponseEntity<ApiResponseDto<ApiResultDto<CandlestickDto>>> getCandlesticksBetweenTimeRangeByInstrumentName(
            String instrumentName, TimeFrame timeframe, long startTimeStamp, long endTimeStamp
    ) {
        String url = GetCandlestickUrlBuilder.create()
                .instrumentName(instrumentName)
                .timeframe(timeframe)
                .startTs(startTimeStamp)
                .endTs(endTimeStamp)
                .build();
        ParameterizedTypeReference<ApiResponseDto<ApiResultDto<CandlestickDto>>> typeRef =
                new ParameterizedTypeReference<>() {
                };

        try {
            sleep(20L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return restTemplate.exchange(url, HttpMethod.GET, null, typeRef);
    }

    public ResponseEntity<ApiResponseDto<ApiResultDto<CandlestickDto>>> getCandlesticksByInstrumentName(
            String instrumentName, TimeFrame timeframe, int count
    ) {
        String url = GetCandlestickUrlBuilder.create()
                .instrumentName(instrumentName)
                .timeframe(timeframe)
                .count(count)
                .build();
        ParameterizedTypeReference<ApiResponseDto<ApiResultDto<CandlestickDto>>> typeRef =
                new ParameterizedTypeReference<>() {
                };

        return restTemplate.exchange(url, HttpMethod.GET, null, typeRef);
    }

    public List<CandlestickWithInstrumentNameDto> getAllPersistedCandlesticks() {
        return candlestickRepository.findAll().stream().map(candlestickMapper::toDtoWithInstrumentName).collect(Collectors.toList());
    }

    public List<CandlestickWithInstrumentNameDto> getCandlesticks(String instrumentName, TimeFrame timeframe) {
        List<CandlestickEntity> candlestickEntities = candlestickRepository.findByIdInstrumentNameAndIdTimeframe(instrumentName, timeframe.getSymbol());
        return candlestickEntities.stream().map(candlestickMapper::toDtoWithInstrumentName).collect(Collectors.toList());
    }

    @Transactional
    public void saveCandlesticks(String instrumentName, TimeFrame timeFrame, List<CandlestickDto> candleStickData) {

        candleStickData.stream()
                .map(dto -> CandlestickEntity.builder()
                        .id(new CandlestickId(instrumentName, timeFrame.getSymbol(), dto.getTimestamp()))
                        .openPrice(dto.getOpenPrice())
                        .highPrice(dto.getHighPrice())
                        .lowPrice(dto.getLowPrice())
                        .closePrice(dto.getClosePrice())
                        .volume(dto.getVolume())
                        .build()).forEach(candlestickRepository::upsert);
    }

    @Transactional
    public void saveLastDaysCandleSticks(String instrument, TimeFrame timeframe, int daysCount) {
        System.out.println(format("Started to add to DB candlesticks with instrument %s and timeframe %s", instrument, timeframe.getSymbol()));
        long currentTime = Instant.now().toEpochMilli();
        long startTime = currentTime - Duration.ofDays(daysCount).toMillis();

        long durationOfCandlesticksInMillis = timeframe.getDurationOfCandlesticksInMillis(250);

        while (startTime < currentTime) {
            List<CandlestickDto> last300CandleSticks =
                    getCandlesticksBetweenTimeRangeByInstrumentName(instrument, timeframe, startTime, startTime + durationOfCandlesticksInMillis)
                            .getBody().getResult().getData();

            saveCandlesticks(instrument, timeframe, last300CandleSticks);
            startTime = startTime + durationOfCandlesticksInMillis;
        }

        System.out.println(format("Added to DB candlesticks with instrument %s and timeframe %s", instrument, timeframe.getSymbol()));
    }

    @Transactional
    public void saveLastXCandleSticks(String instrument, TimeFrame timeframe, int candlestickCount) {
        System.out.println(format("Started to add to DB candlesticks with instrument %s and timeframe %s", instrument, timeframe.getSymbol()));

        List<CandlestickDto> lastXCandleSticks =
                getCandlesticksByInstrumentName(instrument, timeframe, candlestickCount)
                        .getBody().getResult().getData();

        saveCandlesticks(instrument, timeframe, lastXCandleSticks);

        System.out.println(format("Added to DB candlesticks with instrument %s and timeframe %s", instrument, timeframe.getSymbol()));
    }

    public List<Map<String, Object>> calculateRsi(int barCount, TimeFrame timeFrame, String instrumentName) {
        List<CandlestickWithInstrumentNameDto> candleSticks = getCandlesticks(instrumentName, timeFrame);

        BarSeries series = Ta4jConverter.toBarSeries(candleSticks, timeFrame);
        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
        RSIIndicator rsi = new RSIIndicator(closePrice, barCount);
        List<Map<String, Object>> rsiValues = new ArrayList<>();

        for (int i = barCount - 1; i <= series.getEndIndex(); i++) {
            Map<String, Object> entry = new HashMap<>();
            entry.put("timestamp", series.getBar(i).getEndTime().toInstant().toEpochMilli());
            entry.put("rsi", rsi.getValue(i).doubleValue());
            rsiValues.add(entry);
        }
        return rsiValues;
    }

    @Transactional
    public void removeOldestCandlestickByTimeFrame(TimeFrame timeFrame) {
        candlestickRepository.deleteLatestByTimeframe(timeFrame);
    }

    @Transactional
    public long getCandlesticksCountByTimeframe(TimeFrame timeFrame) {
        return candlestickRepository.countByTimeFrame(timeFrame);
    }
}

