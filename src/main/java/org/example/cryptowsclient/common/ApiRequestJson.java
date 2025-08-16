package org.example.cryptowsclient.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiRequestJson {
    private Long id;
    private String method;
    @Builder.Default
    private Map<String, Object> params = Collections.emptyMap();
    private String sig;

    @JsonProperty("api_key")
    private String apiKey;

    private Long nonce;
}