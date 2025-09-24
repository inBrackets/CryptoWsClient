package org.example.cryptowsclient.book;

import lombok.AllArgsConstructor;
import org.example.cryptowsclient.book.dto.BookApiResultDto;
import org.example.cryptowsclient.candlestick.dto.CandlestickDto;
import org.example.cryptowsclient.candlestick.enums.TimeFrame;
import org.example.cryptowsclient.common.ApiResponseDto;
import org.example.cryptowsclient.common.ApiResultDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class BookRestService {

    private final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<ApiResponseDto<BookApiResultDto>> getOrderBook(
            String instrumentName, Integer depth
    ) {
        String url = GetBookUrlBuilder.create()
                .instrumentName(instrumentName)
                .depth(depth)
                .build();
        ParameterizedTypeReference<ApiResponseDto<BookApiResultDto>> typeRef =
                new ParameterizedTypeReference<>() {
                };

        return restTemplate.exchange(url, HttpMethod.GET, null, typeRef);
    }

}
