package org.example.cryptowsclient.instrument;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InstrumentRunner implements CommandLineRunner {

    private final InstrumentService syncService;

    public InstrumentRunner(InstrumentService syncService) {
        this.syncService = syncService;
    }

    @Override
    public void run(String... args) {
        syncService.syncInstruments();
    }
}
