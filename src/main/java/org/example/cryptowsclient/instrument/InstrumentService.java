package org.example.cryptowsclient.instrument;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.cryptowsclient.common.ApiResponseDto;
import org.example.cryptowsclient.common.ApiResultDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class InstrumentService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final InstrumentRepository instrumentRepository;
    private final InstrumentMapper instrumentMapper;

    public void syncInstruments() {

        ApiResponseDto<ApiResultDto<InstrumentDto>> body = getAllInstrumentsFromExternalExchange().getBody();

        if (body != null && body.getResult() != null && body.getResult().getData() != null) {
            List<InstrumentDto> instruments = body.getResult().getData();

            List<InstrumentEntity> entities = instruments.stream()
                    .map(instrumentMapper::toEntity)
                    .toList();

            instrumentRepository.saveAll(entities);
            log.info("✅ Saved {} instruments to DB", entities.size());
        }
    }

    private ResponseEntity<ApiResponseDto<ApiResultDto<InstrumentDto>>> getAllInstrumentsFromExternalExchange() {
        String url = "https://api.crypto.com/exchange/v1/public/get-instruments";
        ParameterizedTypeReference<ApiResponseDto<ApiResultDto<InstrumentDto>>> typeRef =
                new ParameterizedTypeReference<>() {};

                return restTemplate.exchange(url, HttpMethod.GET, null, typeRef);
    }

    public List<InstrumentDto> getAllInstruments() {
        return instrumentRepository.findAll().stream()
                .map(instrumentMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<InstrumentDto> getAllCcyPairs() {
        return instrumentRepository.findAll().stream()
                .map(instrumentMapper::toDto)
                .filter(x->x.getInstType().equalsIgnoreCase("CCY_PAIR"))
                .collect(Collectors.toList());
    }

}
