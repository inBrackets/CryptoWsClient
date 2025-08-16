package org.example.cryptowsclient.orderhistory.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultDto<T> {

    @JsonProperty("data")
    private List<T> data;

}
