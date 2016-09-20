package com.sktechx.palab.logx.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sktechx.palab.logx.service.ExportExcelService;
import com.sktechx.palab.logx.web.StatisticsController;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.Filter;
import java.nio.charset.Charset;


@EnableJpaRepositories( basePackages = "com.sktechx.palab.logx.repository" )
@EntityScan(basePackages= {"com.sktechx.palab.logx.domain"})
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.sktechx.palab.logx.service", "com.sktechx.palab.logx.model"})
@EnableGlobalMethodSecurity( prePostEnabled = true )
public class Application
{

    public static void main( String[] args )
    {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public ExportExcelService excelExportService() {
        return new ExportExcelService();
    }

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

    @Bean
    ExportExcelService statisticsExcelExportService(){
        return new ExportExcelService();
    }


    @Bean
    public StatisticsController statisticsController(){
        return new StatisticsController();
    }

    @Bean
    public HttpMessageConverter<String> responseBodyConverter()
    {
        return new StringHttpMessageConverter( Charset.forName( "UTF-8" ) );
    }


    @Bean
    MappingJackson2HttpMessageConverter converter()
    {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES );
        mapper.disable( SerializationFeature.FAIL_ON_EMPTY_BEANS );

        mapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
//        mapper.writerWithView(ProductView.class);

        converter.setObjectMapper(mapper);
        return converter;
    }

    @Bean
    public Filter characterEncodingFilter()
    {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding( "UTF-8" );
        characterEncodingFilter.setForceEncoding( true );
        return characterEncodingFilter;
    }



}
