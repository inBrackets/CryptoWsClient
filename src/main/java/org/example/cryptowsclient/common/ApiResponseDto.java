package org.example.cryptowsclient.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponseDto<GenericResult> {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("method")
    private String method;

    @JsonProperty("code")
    private Integer code;

    @JsonProperty("result")
    private GenericResult result;

}
