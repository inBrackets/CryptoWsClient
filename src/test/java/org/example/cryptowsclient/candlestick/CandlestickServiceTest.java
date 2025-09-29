package org.example.cryptowsclient.candlestick;

import org.example.cryptowsclient.candlestick.db.CandlestickRepository;
import org.example.cryptowsclient.candlestick.db.StartupCandlestickLoader;
import org.example.cryptowsclient.candlestick.enums.TimeFrame;
import org.example.cryptowsclient.orderhistory.db.StartupOrderHistoryLoader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class CandlestickServiceTest {

    @MockitoBean // prevents loading the method in the real class
    private StartupCandlestickLoader startupCandlestickLoader;

    @MockitoBean // prevents loading the method in the real class
    private StartupOrderHistoryLoader startupOrderHistoryLoader;

    @Autowired
    private CandlestickService candlestickService;

    @Autowired
    private CandlestickRepository candlestickRepository;

    @Test
    @Transactional  // still needed for test isolation
    @Rollback(false) // keep the data after test
    void testSaveLastDaysCandleSticks_shouldPersistData() {
        // given
        String instrument = "CRO_USD"; // pick one supported by API
        TimeFrame timeframe = TimeFrame.ONE_MINUTE;
        int daysCount = 1;

        long countBefore = candlestickRepository.countByTimeFrame(timeframe);

        // when
        candlestickService.saveLastDaysCandleSticks(instrument, timeframe, daysCount);

        // then
        long countAfter = candlestickRepository.countByTimeFrame(timeframe);

        assertThat(countAfter).isGreaterThan(countBefore);
        System.out.printf("Candlesticks before=%d, after=%d%n", countBefore, countAfter);
    }
}