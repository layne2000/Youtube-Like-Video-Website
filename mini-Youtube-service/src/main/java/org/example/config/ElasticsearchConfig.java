package org.example.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {

    @Value("${elasticsearch.url}")
    private String esUrl;

    @Bean
    public ElasticsearchClient elasticsearchClient() {
        // Parse the esUrl to create a HttpHost
        HttpHost httpHost = HttpHost.create(esUrl);

        // Create the low-level client
        RestClient restClient = RestClient.builder(httpHost).build();

        // Create the transport with a Jackson mapper
        RestClientTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());

        // Create the API client
        return new ElasticsearchClient(transport);
    }
}
