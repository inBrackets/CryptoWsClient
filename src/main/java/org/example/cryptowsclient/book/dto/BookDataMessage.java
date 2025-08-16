package org.example.cryptowsclient.book.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//@JsonIgnoreProperties(ignoreUnknown = true)
public class BookDataMessage {
    public long id;
    public String method;
    public int code;
    @JsonIgnore
    public String channel;
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
