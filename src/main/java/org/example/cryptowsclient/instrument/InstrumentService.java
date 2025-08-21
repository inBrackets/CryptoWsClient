package org.example.cryptowsclient.instrument;

import lombok.AllArgsConstructor;
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
            System.out.println("✅ Saved " + entities.size() + " instruments to DB");
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

}
