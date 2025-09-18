package org.example.cryptowsclient.orderhistory.db;

import org.example.cryptowsclient.orderhistory.dto.OrderItemDto;
import org.example.cryptowsclient.orderhistory.dto.enums.ExecInst;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderHistoryMapper {

    @Mapping(target = "execInst", source = "execInst", qualifiedByName = "stringToList")
    OrderItemDto toDto(OrderHistoryEntity orderHistoryEntity);

    @Mapping(target = "execInst", source = "execInst", qualifiedByName = "listToString")
    OrderHistoryEntity toEntity(OrderItemDto orderItemDto);

    @Named("listToString")
    default String listToString(List<ExecInst> list) {
        return list != null
                ? list.stream().map(Enum::name).collect(Collectors.joining(","))
                : null;
    }

    @Named("stringToList")
    default List<ExecInst> stringToList(String value) {
        return (value != null && !value.isEmpty())
                ? Arrays.stream(value.split(","))
                .map(ExecInst::valueOf)
                .toList()
                : List.of();
    }
}