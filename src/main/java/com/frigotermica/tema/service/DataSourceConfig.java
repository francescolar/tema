package com.frigotermica.tema.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Value("${URL_DB}")
    private String url_db;

    @Value("${USERNAME_DB}")
    private String username_db;

    @Value("${PASSWORD_DB}")
    private String password_db;

    @Bean
    public DataSource dataSource() {
        return new DriverManagerDataSource(url_db, username_db, password_db);
    }
}