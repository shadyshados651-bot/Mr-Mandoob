package com.Mahmoud.sales_backend.config;


import java.sql.Connection;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DBConfig {

    @Bean
    public Connection connection() {
        return DatabaseConnection.getInstance().getConnection();
    }
}