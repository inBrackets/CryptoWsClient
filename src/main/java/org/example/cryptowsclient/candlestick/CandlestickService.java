package org.example.cryptowsclient.candlestick;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.cryptowsclient.candlestick.db.CandlestickEntity;
import org.example.cryptowsclient.candlestick.db.CandlestickId;
import org.example.cryptowsclient.candlestick.db.CandlestickRepository;
import org.example.cryptowsclient.candlestick.dto.CandlestickDto;
import org.example.cryptowsclient.candlestick.dto.CandlestickMapper;
import org.example.cryptowsclient.candlestick.dto.CandlestickWithInstrumentNameDto;
import org.example.cryptowsclient.common.ApiResponseDto;
import org.example.cryptowsclient.common.ApiResultDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
@AllArgsConstructor
public class CandlestickService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final CandlestickRepository candlestickRepository;
    private static final String BASE_URL = "https://api.crypto.com/exchange/v1/";
    private final CandlestickMapper candlestickMapper;

    public ResponseEntity<ApiResponseDto<ApiResultDto<CandlestickDto>>> getLast300CandlesticksByInstrumentName(
            String instrumentName, String timeframe
    ) {
        String url = format("%s/public/get-candlestick?instrument_name=%s&timeframe=%s&count=300", BASE_URL, instrumentName, timeframe);
        ParameterizedTypeReference<ApiResponseDto<ApiResultDto<CandlestickDto>>> typeRef =
                new ParameterizedTypeReference<>() {
                };

        return restTemplate.exchange(url, HttpMethod.GET, null, typeRef);
    }

    public ResponseEntity<ApiResponseDto<ApiResultDto<CandlestickDto>>> getLast300CandlesticksByInstrumentNameBeforeTimestamp(
            String instrumentName, String timeframe, long end_ts
    ) {
        String url = format("%s/public/get-candlestick?instrument_name=%s&timeframe=%s&count=300&end_ts=%s", BASE_URL, instrumentName, timeframe, end_ts);
        ParameterizedTypeReference<ApiResponseDto<ApiResultDto<CandlestickDto>>> typeRef =
                new ParameterizedTypeReference<>() {
                };

        return restTemplate.exchange(url, HttpMethod.GET, null, typeRef);
    }

    public List<CandlestickWithInstrumentNameDto> getAllPersistedCandlesticks() {
        return candlestickRepository.findAll().stream().map(candlestickMapper::toDtoWithInstrumentName).collect(Collectors.toList());
    }

    @Transactional
    public void saveCandlesticks(String instrumentName, List<CandlestickDto> candleStickData) {

        candleStickData.stream()
                .map(dto -> CandlestickEntity.builder()
                        .id(new CandlestickId(instrumentName, dto.getTimestamp()))
                        .openPrice(dto.getOpenPrice())
                        .highPrice(dto.getHighPrice())
                        .lowPrice(dto.getLowPrice())
                        .closePrice(dto.getClosePrice())
                        .volume(dto.getVolume())
                        .build()).forEach(candlestickRepository::save);
    }

    public void saveTodayCandleSticks() {
        List<CandlestickDto> last300CandleSticks = getLast300CandlesticksByInstrumentName("CRO_USD", "1m").getBody().getResult().getData();
        saveCandlesticks("CRO_USD", last300CandleSticks);
        long utcMidnight = LocalDate.now(ZoneOffset.UTC)
                .atStartOfDay(ZoneOffset.UTC)
                .toInstant()
                .toEpochMilli();
        long startTime = last300CandleSticks.stream().mapToLong(CandlestickDto::getTimestamp).min().orElseThrow();

        while (startTime > utcMidnight) {
            last300CandleSticks = getLast300CandlesticksByInstrumentNameBeforeTimestamp("CRO_USD", "1m", startTime + 60000).getBody().getResult().getData();
            saveCandlesticks("CRO_USD", last300CandleSticks);
            startTime = last300CandleSticks.stream().mapToLong(CandlestickDto::getTimestamp).min().orElseThrow();
        }
    }
}

