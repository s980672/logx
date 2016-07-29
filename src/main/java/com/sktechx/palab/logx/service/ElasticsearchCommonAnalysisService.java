package com.sktechx.palab.logx.service;

import com.sktechx.palab.logx.model.*;
import com.sktechx.palab.logx.repository.*;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.search.aggregation.TermsAggregation;
import io.searchbox.params.SearchType;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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




    @Autowired
    ErrorCountRepository errCntRepo;


    @Autowired
    ErrorSvcCountRepository errSvcCntRepo;


    public void generateErrorSvcCount(String start, String end) throws IOException, ParseException {

        SearchResult res = getResult(AggReqDSLs.getQueryErrorSvcCount(start, end));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(start);

        res.getAggregations().getTermsAggregation("errorCount").getBuckets().stream().forEach(err->{

            err.getTermsAggregation("svcId").getBuckets().stream().forEach(svc -> {

                ErrSvcCount esc = new ErrSvcCount(enumRCType.daily, date, svc.getKey(), err.getKey(), svc.getCount());
                errSvcCntRepo.save(esc);
            });
        });
    }


    public void generateErrorCount(String start, String end) throws IOException, ParseException {
        SearchResult res = getResult(AggReqDSLs.getQueryErrorCount(start, end));

        logger.debug("total : {}", res.getTotal());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(start);

        res.getAggregations().getTermsAggregation("errorCount").getBuckets().stream().forEach(er-> {
            ErrorCount ec = new ErrorCount(enumRCType.daily, date, er.getKey(), er.getCount());
            errCntRepo.save(ec);
        });

    }


    public SearchResult getResult(String queryString) throws IOException {

        Search.Builder searchBuilder = new Search.Builder(queryString).addIndex(INDEX).addType(INDEX_TYPE).setSearchType(SearchType.COUNT);

        SearchResult response = client.execute(searchBuilder.build());

        logger.debug(response.getJsonString());

        return response;
    }

}
