package com.food.order.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
@Configuration
public class AppConfig {
  @Bean RestClient paymentClient() {
    return RestClient.builder().baseUrl("http://localhost:8082").build();
  }
}
