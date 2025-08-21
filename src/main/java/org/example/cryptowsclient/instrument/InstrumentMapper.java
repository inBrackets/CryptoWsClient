package org.example.cryptowsclient.instrument;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
interface InstrumentMapper {

    InstrumentEntity toEntity(InstrumentDto dto);

    InstrumentDto toDto(InstrumentEntity entity);
}
