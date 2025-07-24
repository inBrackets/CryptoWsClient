package org.example.cryptowsclient.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BookDataMessage {
    public long id;
    public String method;
    public int code;
    public BookResult result;

    @Override
    public String toString() {
        return "BookDataMessage{" +
                "id=" + id +
                ", method='" + method + '\'' +
                ", code=" + code +
                ", result=" + result +
                '}';
    }
}
