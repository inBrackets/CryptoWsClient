package org.example.cryptowsclient.book.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    public String toJson() {
        return String.format("""
                {
                  "id": %d,
                  "method": "%s",
                  "code": %d
                }
                """, id, method, code);
    }

}
