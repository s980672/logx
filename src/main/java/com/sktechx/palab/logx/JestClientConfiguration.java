package com.sktechx.palab.logx;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by 1002382 on 2016. 7. 6..
 */
@Configuration
public class JestClientConfiguration {

    @Value("${elasticsearch.search.endpoint}")
    private String address = "http://172.21.85.33/";

    @Value("${elasticsearch.search.max_connection}")
    private int maxTotalConnection = 10;

    @Value("${elasticsearch.search.conn_timeout}")
    private int connTimeout = 1000;

    @Value("${elasticsearch.search.read_timeout}")
    private int readTimeout = 3000;

    @Bean
    public JestClient jestClient(){
        // Configuration
        HttpClientConfig clientConfig = new HttpClientConfig.Builder(address)
                .multiThreaded(true)
                .maxTotalConnection(maxTotalConnection)
                .connTimeout(connTimeout)
                .readTimeout(readTimeout)
                .build();

        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(clientConfig);
        JestClient client = factory.getObject();
        return client;
    }
}