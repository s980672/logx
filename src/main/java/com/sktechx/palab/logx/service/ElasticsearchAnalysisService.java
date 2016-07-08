package com.sktechx.palab.logx.service;

import com.sktechx.palab.logx.model.RequestCall;
import com.sktechx.palab.logx.model.ServiceRequestCall;
import com.sktechx.palab.logx.model.enumRCType;
import com.sktechx.palab.logx.repository.RequestCallRepository;
import com.sktechx.palab.logx.repository.ServiceRCRepository;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.search.aggregation.TermsAggregation;
import io.searchbox.params.SearchType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

/**
 * Created by 1002382 on 2016. 7. 5..
 */
@Service
public class ElasticsearchAnalysisService {

    Logger logger = LoggerFactory.getLogger(ElasticsearchAnalysisService.class);


    @Autowired
    private JestClient client;

    private final String INDEX = "logx";
    private final String INDEX_TYPE = "log";



    @Autowired
    RequestCallRepository rcRepo;

    @Autowired
    ServiceRCRepository svcRCRepo;


    public void generatePV(Long start, Long end){


        
        SearchResult response = getResult(AggReqDSLs.getQueryPVDuringPeriod(start, end));

        logger.debug("total : {}", response.getTotal());

        RequestCall rc = new RequestCall(enumRCType.daily, new Date(start), new Long(response.getTotal()));

        rcRepo.save(rc);

    }

    public void generateSVCPV(Long start, Long end){

        ///////////////////////////////////////////////////////////////////////////////
        //service pv

        SearchResult response = getResult(AggReqDSLs.getQueryServicePV(start , end));


        TermsAggregation svcPV = response.getAggregations().getTermsAggregation("serviceRC");

        svcPV.getBuckets().stream().forEach(b -> {

            ServiceRequestCall svcRC = new ServiceRequestCall(enumRCType.daily, new Date(start), b.getKey(), b.getCount());

            svcRCRepo.save(svcRC);

        });
    }

    public SearchResult getResult(String queryString){

        Search.Builder searchBuilder = new Search.Builder(queryString).addIndex(INDEX).addType(INDEX_TYPE).setSearchType(SearchType.COUNT);

        //addIndex("filebeat-2016-w26").addType("log").setSearchType(SearchType.COUNT);

        SearchResult response = null;
        try {

            response = client.execute(searchBuilder.build());

        } catch (IOException e) {

            logger.error(e.getLocalizedMessage());

            e.printStackTrace();

        }

        logger.debug(response.getJsonString());

        return response;
    }

}
