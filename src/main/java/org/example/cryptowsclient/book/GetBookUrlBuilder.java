package org.example.cryptowsclient.book;

public class GetBookUrlBuilder {

    private static final String BASE_URL = "https://api.crypto.com/exchange/v1/public/get-book";

    private String instrumentName;
    private Integer depth = 300;

    public static GetBookUrlBuilder create() {
        return new GetBookUrlBuilder();
    }

    public GetBookUrlBuilder instrumentName(String instrumentName) {
        this.instrumentName = instrumentName;
        return this;
    }

    public GetBookUrlBuilder depth(Integer depth) {
        this.depth = depth;
        return this;
    }

    public String build() {
        StringBuilder sb = new StringBuilder(BASE_URL);
        String prefix = "?";

        if (instrumentName != null) {
            sb.append(prefix).append("instrument_name=").append(instrumentName);
            prefix = "&";
        }
        if (depth != null) {
            sb.append(prefix).append("depth=").append(depth);
            prefix = "&";
        }

        return sb.toString();
    }
}
