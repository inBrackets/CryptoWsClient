package org.example.cryptowsclient.book.dto;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class SubscribeRequest {
    int id;
    String method;
    Params params;
    String nonce;

    @Value
    @Builder
    public static class Params {
        @Singular
        List<String> channels;
    }

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public String toJson() throws JsonProcessingException {
        return MAPPER.writeValueAsString(this);
    }

    public static SubscribeRequest fromJson(String json) throws JsonProcessingException {
        return MAPPER.readValue(json, SubscribeRequest.class);
    }
}