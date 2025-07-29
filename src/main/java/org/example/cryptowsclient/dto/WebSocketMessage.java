package org.example.cryptowsclient.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WebSocketMessage {
    @JsonProperty("id")
    private int id;

    @JsonProperty("method")
    private String method;

    @JsonProperty("code")
    private Integer code;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public Integer getCode() { return code; }
    public void setCode(Integer code) { this.code = code; }
}