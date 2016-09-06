package com.sktechx.palab.logx.service;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.indices.DeleteIndex;
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

    private final String INDEX = "logx";
    private final String INDEX_TYPE = "log";

    public SearchResult getResult(String queryString) throws IOException {

        Search.Builder searchBuilder = new Search.Builder(queryString).addIndex(INDEX).addType(INDEX_TYPE).setSearchType(SearchType.COUNT);

        SearchResult response = client.execute(searchBuilder.build());

        if ( response.getResponseCode() % 100 != 2 ) {
            logger.error("{}", response.getErrorMessage());
        }

        logger.debug(response.getJsonString());

        return response;
    }

    public JestResult deleteIndex(String indexName) throws IOException {

        DeleteIndex.Builder deleteIndex = new DeleteIndex.Builder(indexName);

        JestResult result = client.execute(deleteIndex.build());

        if ( result.getResponseCode() % 100 != 2 ) {
            logger.error("{}", result.getErrorMessage());
        }

        logger.debug(result.getJsonString());

        return result;
    }
}
