package com.deploji.scheduler.jwt;

import javax.crypto.SecretKey;
import java.util.Base64;

public class JwtKey implements SecretKey {
    private String secret;

    public JwtKey(String secret) {
        this.secret = secret;
    }

    @Override
    public String getAlgorithm() {
        return "HS384";
    }

    @Override
    public String getFormat() {
        return null;
    }

    @Override
    public byte[] getEncoded() {
        return Base64.getEncoder().encodeToString(secret.getBytes()).getBytes();
    }
}
