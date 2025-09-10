package org.example.cryptowsclient.candlestick.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandlestickRepository extends JpaRepository<CandlestickEntity, CandlestickId> {

}
