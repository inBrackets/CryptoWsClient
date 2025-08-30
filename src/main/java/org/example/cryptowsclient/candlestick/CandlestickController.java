package org.example.cryptowsclient.candlestick;

import lombok.AllArgsConstructor;
import org.example.cryptowsclient.common.ApiResponseDto;
import org.example.cryptowsclient.common.ApiResultDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/candlestick")
public class CandlestickController {

    private final CandlestickService candlestickService;

    @GetMapping("/get-all")
    @CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8080"})
    private ResponseEntity<ApiResponseDto<ApiResultDto<CandlestickDto>>> getAll(
            @RequestParam(value = "instrument_name", required = false, defaultValue = "CRO_USD") String instrumentName,
            @RequestParam(value = "time_frame", required = false, defaultValue = "M15") String timeFrame
    ) {
        return candlestickService.getCandlesticksByInstrumentName(instrumentName, timeFrame);
    }
}