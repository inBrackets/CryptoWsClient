package org.example.cryptowsclient.candlestick.dto;

import org.example.cryptowsclient.candlestick.db.CandlestickEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CandlestickMapper {

    @Mapping(target = "instrumentName", source = "id.instrumentName")
    @Mapping(target = "timestamp", source = "id.timestamp")
    @Mapping(target = "timeframe", source = "id.timeframe")
    CandlestickWithInstrumentNameDto toDtoWithInstrumentName(CandlestickEntity candlestickEntity);
}