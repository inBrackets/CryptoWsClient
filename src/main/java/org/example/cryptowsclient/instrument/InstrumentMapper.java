package org.example.cryptowsclient.instrument;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
interface InstrumentMapper {

    InstrumentEntity toEntity(InstrumentItemDto dto);

    InstrumentItemDto toDto(InstrumentEntity entity);
}
