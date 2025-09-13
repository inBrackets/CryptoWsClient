package org.example.cryptowsclient.candlestick.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandlestickRepository extends JpaRepository<CandlestickEntity, CandlestickId> {

    @Query("SELECT c FROM CandlestickEntity c WHERE c.id.instrumentName = :instrumentName AND c.id.timeframe = :timeframe")
    List<CandlestickEntity> findByInstrumentNameAndTimeframe(@Param("instrumentName") String instrumentName,
                                                         @Param("timeframe") String timeframe);


    @Modifying
    @Query(value = """
    INSERT INTO candlestick (timeframe, timestamp, instrument_name, open_price, high_price, low_price, close_price, volume)
    VALUES (:#{#c.id.timeframe}, :#{#c.id.timestamp}, :#{#c.id.instrumentName},
            :#{#c.openPrice}, :#{#c.highPrice}, :#{#c.lowPrice}, :#{#c.closePrice}, :#{#c.volume})
    ON DUPLICATE KEY UPDATE 
        open_price = VALUES(open_price),
        high_price = VALUES(high_price),
        low_price = VALUES(low_price),
        close_price = VALUES(close_price),
        volume = VALUES(volume)
""", nativeQuery = true)
    void upsert(@Param("c") CandlestickEntity c);


}
