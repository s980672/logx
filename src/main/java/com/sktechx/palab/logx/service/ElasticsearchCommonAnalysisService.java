package com.sktechx.palab.logx.service;

import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.params.SearchType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by 1002382 on 2016. 7. 5..
 */
@Service
public class ElasticsearchCommonAnalysisService {

    Logger logger = LoggerFactory.getLogger(ElasticsearchCommonAnalysisService.class);

    @Autowired
    private JestClient client;

    private final String INDEX = "bulk-gateway-log";
    private final String INDEX_TYPE = "log";

    public SearchResult getResult(String queryString) throws IOException {

        Search.Builder searchBuilder = new Search.Builder(queryString).addIndex(INDEX).addType(INDEX_TYPE).setSearchType(SearchType.COUNT);

        SearchResult response = client.execute(searchBuilder.build());

        logger.debug(response.getJsonString());

        return response;
    }

}
