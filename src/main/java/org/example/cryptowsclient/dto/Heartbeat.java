package org.example.cryptowsclient.dto;

public class Heartbeat {
    private long id;
    private String method;
    private int code;

    public long getId() {
        return id;
    }

    public int getCode() {
        return code;
    }

    public String getMethod() {
        return method;
    }
}
