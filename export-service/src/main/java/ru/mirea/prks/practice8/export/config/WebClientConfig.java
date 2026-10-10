package ru.mirea.prks.practice8.export.config;

import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
public class WebClientConfig {
    @Bean
    WebClient datasetWebClient(WebClient.Builder builder,
                               @Value("${dataset-service.url}") String datasetServiceUrl) {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000)
                .responseTimeout(Duration.ofSeconds(2));
        return builder.baseUrl(datasetServiceUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
