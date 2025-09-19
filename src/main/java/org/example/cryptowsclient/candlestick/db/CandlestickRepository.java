package org.example.cryptowsclient.candlestick.db;

import org.example.cryptowsclient.candlestick.enums.TimeFrame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandlestickRepository extends JpaRepository<CandlestickEntity, CandlestickId> {

    List<CandlestickEntity> findByIdInstrumentNameAndIdTimeframe(
            String instrumentName,
            String timeFrame);

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


    @Modifying
    @Query(value = """
    DELETE FROM candlestick
    WHERE timeframe = :#{#timeFrame.symbol}
      AND timestamp = (
          SELECT t.max_ts
          FROM (
              SELECT MAX(c2.timestamp) AS max_ts
              FROM candlestick c2
              WHERE c2.timeframe = :#{#timeFrame.symbol}
          ) AS t
      )
    LIMIT 1
""", nativeQuery = true)
    void deleteLatestByTimeframe(@Param("timeFrame") TimeFrame timeFrame);

    @Query(value = """
    SELECT COUNT(*)
    FROM candlestick
    WHERE timeframe = :#{#timeFrame.symbol}
""", nativeQuery = true)
    long countByTimeFrame(@Param("timeFrame") TimeFrame timeFrame);

}
