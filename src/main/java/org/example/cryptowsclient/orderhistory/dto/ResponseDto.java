package org.example.cryptowsclient.orderhistory.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto<Result> {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("method")
    private String method;

    @JsonProperty("code")
    private Integer code;

    @JsonProperty("result")
    private Result result;

}
