package com.migration.platform.core.security;

import com.migration.platform.spi.security.CryptoService;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Demo crypto using reversible Base64 (NOT for production).
 */
public class DemoCryptoService implements CryptoService {

    @Override
    public String encrypt(String plaintext, String keyAlias) {
        if (plaintext == null) {
            return null;
        }
        String payload = keyAlias + ":" + plaintext;
        return Base64.getEncoder().encodeToString(payload.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String decrypt(String ciphertext, String keyAlias) {
        if (ciphertext == null) {
            return null;
        }
        String decoded = new String(Base64.getDecoder().decode(ciphertext), StandardCharsets.UTF_8);
        String prefix = keyAlias + ":";
        if (!decoded.startsWith(prefix)) {
            throw new IllegalStateException("ciphertext key alias mismatch");
        }
        return decoded.substring(prefix.length());
    }
}
