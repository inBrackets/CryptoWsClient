package org.example.cryptowsclient.instrument;

import org.example.cryptowsclient.common.ApiResponseDto;
import org.example.cryptowsclient.common.ApiResultDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class InstrumentService {

    private final RestTemplate restTemplate;
    private final InstrumentRepository instrumentRepository;

    public InstrumentService(InstrumentRepository instrumentRepository) {
        this.restTemplate = new RestTemplate();
        this.instrumentRepository = instrumentRepository;
    }

    public void syncInstruments() {
        String url = "https://api.crypto.com/exchange/v1/public/get-instruments";
        ParameterizedTypeReference<ApiResponseDto<ApiResultDto<InstrumentItemDto>>> typeRef =
                new ParameterizedTypeReference<>() {};

        ResponseEntity<ApiResponseDto<ApiResultDto<InstrumentItemDto>>> response =
                restTemplate.exchange(url, HttpMethod.GET, null, typeRef);

        ApiResponseDto<ApiResultDto<InstrumentItemDto>> body = response.getBody();

        if (body != null && body.getResult() != null && body.getResult().getData() != null) {
            List<InstrumentItemDto> instruments = body.getResult().getData();

            List<InstrumentEntity> entities = instruments.stream()
                    .map(this::mapToEntity)
                    .toList();

            instrumentRepository.saveAll(entities);
            System.out.println("✅ Saved " + entities.size() + " instruments to DB");
        }

    }

    private InstrumentEntity mapToEntity(InstrumentItemDto dto) {
        InstrumentEntity entity = new InstrumentEntity();
        entity.setSymbol(dto.getSymbol());
        entity.setInstType(dto.getInstType());
        entity.setDisplayName(dto.getDisplayName());
        entity.setBaseCurrency(dto.getBaseCurrency());
        entity.setQuoteCurrency(dto.getQuoteCurrency());
        entity.setQuoteDecimals(dto.getQuoteDecimals());
        entity.setQuantityDecimals(dto.getQuantityDecimals());
        entity.setPriceTickSize(dto.getPriceTickSize());
        entity.setQuantityTickSize(dto.getQuantityTickSize());
        entity.setMaxLeverage(dto.getMaxLeverage());
        entity.setTradable(dto.isTradable());
        entity.setExpiryTimestampMs(dto.getExpiryTimestampMs());
        entity.setBetaProduct(dto.isBetaProduct());
        entity.setUnderlyingSymbol(dto.getUnderlyingSymbol());
        entity.setContractSize(dto.getContract_size());
        entity.setMarginBuyEnabled(dto.isMarginBuyEnabled());
        entity.setMarginSellEnabled(dto.isMarginSellEnabled());
        return entity;
    }

}
