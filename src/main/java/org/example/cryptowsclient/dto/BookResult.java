package org.example.cryptowsclient.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BookResult {
    public String instrument_name;
    public String subscription;
    public String channel;
    public int depth;
    public List<BookSnapshot> data;

    @Override
    public String toString() {
        return "BookResult{" +
                "instrument_name='" + instrument_name + '\'' +
                ", subscription='" + subscription + '\'' +
                ", channel='" + channel + '\'' +
                ", depth=" + depth +
                ", data=" + data +
                '}';
    }
}
