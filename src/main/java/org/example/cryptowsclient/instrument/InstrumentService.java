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
    private final InstrumentMapper instrumentMapper;

    public InstrumentService(InstrumentRepository instrumentRepository,
                             InstrumentMapper instrumentMapper) {
        this.restTemplate = new RestTemplate();
        this.instrumentRepository = instrumentRepository;
        this.instrumentMapper = instrumentMapper;
    }

    public void syncInstruments() {

        ApiResponseDto<ApiResultDto<InstrumentItemDto>> body = getAllInstruments().getBody();

        if (body != null && body.getResult() != null && body.getResult().getData() != null) {
            List<InstrumentItemDto> instruments = body.getResult().getData();

            List<InstrumentEntity> entities = instruments.stream()
                    .map(instrumentMapper::toDto)
                    .toList();

            instrumentRepository.saveAll(entities);
            System.out.println("✅ Saved " + entities.size() + " instruments to DB");
        }
    }

    public ResponseEntity<ApiResponseDto<ApiResultDto<InstrumentItemDto>>> getAllInstruments() {
        String url = "https://api.crypto.com/exchange/v1/public/get-instruments";
        ParameterizedTypeReference<ApiResponseDto<ApiResultDto<InstrumentItemDto>>> typeRef =
                new ParameterizedTypeReference<>() {};

                return restTemplate.exchange(url, HttpMethod.GET, null, typeRef);
    }

}
