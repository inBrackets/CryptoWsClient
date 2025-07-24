package org.example.cryptowsclient.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BookSnapshot {
    public List<List<String>> asks;
    public List<List<String>> bids;
    public long t;
    public long tt;
    public long u;
    public long cs;

    @Override
    public String toString() {
        return "\n  BookSnapshot{" +
                "\n    asks=" + asks +
                ",\n    bids=" + bids +
                ",\n    t=" + t +
                ",\n    tt=" + tt +
                ",\n    u=" + u +
                ",\n    cs=" + cs +
                "\n  }";
    }
}
