package org.example.cryptowsclient.auth;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.example.cryptowsclient.common.ApiRequestJson;

@Slf4j
public class SigningUtil {

    private static final String HMAC_SHA256 = "HmacSHA256";
    private static final int MAX_LEVEL = 3;

    public static boolean verifySignature(ApiRequestJson apiRequestJson, String secret) {
        try {
            return genSignature(apiRequestJson, secret).equalsIgnoreCase(apiRequestJson.getSig());
        } catch (Exception e) {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public static String getParamString(final Object paramObject) {
        StringBuilder sb = new StringBuilder();
        appendParamString(sb, paramObject, 0);
        return sb.toString();
    }


    @SuppressWarnings("unchecked")
    private static void appendParamString(final StringBuilder paramsStringBuilder, final Object paramObject, final int level) {
        if (level >= MAX_LEVEL) {
            paramsStringBuilder.append(paramObject.toString());
            return;
        }

        if (paramObject instanceof Map) {
            TreeMap<String, Object> params = new TreeMap<>((Map) paramObject);
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                if (entry.getValue() instanceof Double) {
                    paramsStringBuilder
                            .append(entry.getKey())
                            .append((new BigDecimal(entry.getValue().toString()))
                                    .stripTrailingZeros()
                                    .toPlainString());
                } else if (entry.getValue() instanceof List || entry.getValue() instanceof Map) {
                    paramsStringBuilder
                            .append(entry.getKey());
                    appendParamString(paramsStringBuilder, entry.getValue(), level + 1);
                } else {
                    paramsStringBuilder
                            .append(entry.getKey())
                            .append(entry.getValue());
                }
            }
        } else if (paramObject instanceof List) {
            List list = (List) paramObject;
            for (Object o : list) {
                appendParamString(paramsStringBuilder, o, level + 1);
            }
        } else {
            paramsStringBuilder.append(paramObject.toString());
        }
    }

    public static String genSignature(ApiRequestJson apiRequestJson, String secret)
            throws NoSuchAlgorithmException, InvalidKeyException {
        final byte[] byteKey = secret.getBytes(StandardCharsets.UTF_8);
        Mac mac = Mac.getInstance(HMAC_SHA256);
        SecretKeySpec keySpec = new SecretKeySpec(byteKey, HMAC_SHA256);
        mac.init(keySpec);

        String paramsString = "";

        if (apiRequestJson.getParams() != null) {
            paramsString += getParamString(apiRequestJson.getParams());
        }

        String sigPayload =
                apiRequestJson.getMethod()
                        + apiRequestJson.getId()
                        + apiRequestJson.getApiKey()
                        + paramsString
                        + (apiRequestJson.getNonce() == null ? "" : apiRequestJson.getNonce());

        byte[] macData = mac.doFinal(sigPayload.getBytes(StandardCharsets.UTF_8));

        return Hex.encodeHexString(macData);
    }

    public static ApiRequestJson sign(ApiRequestJson apiRequestJson, String secret)
            throws InvalidKeyException, NoSuchAlgorithmException {
        apiRequestJson.setSig(genSignature(apiRequestJson, secret));

        return apiRequestJson;
    }

    public static String signAndParseToJsonString(ApiRequestJson apiRequestJson, String secret) {
        apiRequestJson.setNonce(System.currentTimeMillis());
        ObjectMapper mapper = new ObjectMapper();
        String requestJsonBody;
        try {
            requestJsonBody = mapper.writeValueAsString(sign(apiRequestJson, secret));
        } catch (JsonProcessingException | InvalidKeyException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return requestJsonBody;
    }

    public static void main(String[] argv) throws InvalidKeyException, NoSuchAlgorithmException {
        ApiRequestJson apiRequestJson = ApiRequestJson.builder()
                .id(11L)
                .apiKey("token")
                .method("public/auth")
                .nonce(1589594102779L)
                .build();

        log.info(genSignature(apiRequestJson, "se"));

        log.info(sign(apiRequestJson, "se").toString());

        log.info(signAndParseToJsonString(apiRequestJson, "se"));

    }
}