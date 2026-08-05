package com.migration.platform.access.datasource;

import com.migration.platform.spi.security.CryptoService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 图3-2 服务接入层：数据源管理（凭证经 Crypto 加密存储）.
 */
public class DatasourceService {

    private final CryptoService cryptoService;
    private final Map<String, Datasource> datasources = new ConcurrentHashMap<String, Datasource>();

    public DatasourceService(CryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }

    public Datasource register(String name, String jdbcUrl, String username, String password, String keyAlias) {
        Datasource ds = new Datasource();
        ds.setDatasourceId(UUID.randomUUID().toString());
        ds.setName(name);
        ds.setJdbcUrl(jdbcUrl);
        ds.setUsername(username);
        ds.setPasswordCipher(cryptoService.encrypt(password, keyAlias));
        ds.setKeyAlias(keyAlias);
        datasources.put(ds.getDatasourceId(), ds);
        return ds;
    }

    public String decryptPassword(String datasourceId) {
        Datasource ds = datasources.get(datasourceId);
        if (ds == null) {
            throw new IllegalArgumentException("datasource not found: " + datasourceId);
        }
        return cryptoService.decrypt(ds.getPasswordCipher(), ds.getKeyAlias());
    }

    public Datasource get(String datasourceId) {
        return datasources.get(datasourceId);
    }

    public List<Datasource> list() {
        return Collections.unmodifiableList(new ArrayList<Datasource>(datasources.values()));
    }

    public static class Datasource {
        private String datasourceId;
        private String name;
        private String jdbcUrl;
        private String username;
        private String passwordCipher;
        private String keyAlias;

        public String getDatasourceId() {
            return datasourceId;
        }

        public void setDatasourceId(String datasourceId) {
            this.datasourceId = datasourceId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getJdbcUrl() {
            return jdbcUrl;
        }

        public void setJdbcUrl(String jdbcUrl) {
            this.jdbcUrl = jdbcUrl;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPasswordCipher() {
            return passwordCipher;
        }

        public void setPasswordCipher(String passwordCipher) {
            this.passwordCipher = passwordCipher;
        }

        public String getKeyAlias() {
            return keyAlias;
        }

        public void setKeyAlias(String keyAlias) {
            this.keyAlias = keyAlias;
        }
    }
}
