package org.example.cryptowsclient.common;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiCreateOrderResultDto {

    @JsonProperty("client_oid")
    private Long clientOrderId;
    @JsonProperty("order_id")
    private Long orderId;

}
