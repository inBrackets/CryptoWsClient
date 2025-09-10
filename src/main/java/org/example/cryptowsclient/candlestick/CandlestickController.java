package org.example.cryptowsclient.candlestick;

import lombok.AllArgsConstructor;
import org.example.cryptowsclient.candlestick.dto.CandlestickWithInstrumentNameDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
                .status(HttpStatus.OK).body(candlestickService.getAllPersistedCandlesticks());
    }
}