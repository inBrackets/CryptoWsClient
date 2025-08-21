package org.example.cryptowsclient.instrument;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/instrument")
public class InstrumentController {

    private final InstrumentService instrumentService;

    @GetMapping("/get-all")
    @CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8080"})
    private ResponseEntity<List<InstrumentItemDto>> getAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(instrumentService.getAllInstruments());
    }
}
