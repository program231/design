package com.migration.platform.spi.security;

/**
 * Credential / payload crypto SPI.
 */
public interface CryptoService {

    String encrypt(String plaintext, String keyAlias);

    String decrypt(String ciphertext, String keyAlias);
}
