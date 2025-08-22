package org.example.cryptowsclient.candlestick;

import lombok.AllArgsConstructor;
import org.example.cryptowsclient.common.ApiResponseDto;
import org.example.cryptowsclient.common.ApiResultDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static java.lang.String.format;

@Service
@AllArgsConstructor
public class CandlestickService {

    private final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<ApiResponseDto<ApiResultDto<CandlestickDto>>> getCandlesticksByInstrumentName(
            String instrumentName
    ) {
        String url = format("https://api.crypto.com/exchange/v1//public/get-candlestick?instrument_name=%s&timeframe=M15&count=300", instrumentName);
        ParameterizedTypeReference<ApiResponseDto<ApiResultDto<CandlestickDto>>> typeRef =
                new ParameterizedTypeReference<>() {
                };

        return restTemplate.exchange(url, HttpMethod.GET, null, typeRef);
    }
}

