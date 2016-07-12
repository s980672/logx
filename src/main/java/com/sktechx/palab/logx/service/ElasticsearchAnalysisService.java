package com.sktechx.palab.logx.service;

import com.sktechx.palab.logx.model.RequestCall;
import com.sktechx.palab.logx.model.ServiceRequestCall;
import com.sktechx.palab.logx.model.SvcAppRC;
import com.sktechx.palab.logx.model.enumRCType;
import com.sktechx.palab.logx.repository.RequestCallRepository;
import com.sktechx.palab.logx.repository.ServiceRCRepository;
import com.sktechx.palab.logx.repository.SvcAppRCRepository;
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

    @Autowired
    SvcAppRCRepository svcAppRCRepo;



    public void generatePV(Long start, Long end) throws IOException {


        
        SearchResult response = getResult(AggReqDSLs.getQueryPVDuringPeriod(start, end));

        logger.debug("total : {}", response.getTotal());

        RequestCall rc = new RequestCall(enumRCType.daily, new Date(start), new Long(response.getTotal()));

        rcRepo.save(rc);

    }

    public void generateSVCPV(Long start, Long end) throws IOException {

        ///////////////////////////////////////////////////////////////////////////////
        //service pv

        SearchResult response = getResult(AggReqDSLs.getQueryServicePV(start, end));


        TermsAggregation svcPV = response.getAggregations().getTermsAggregation("serviceRC");

        svcPV.getBuckets().stream().forEach(b -> {

            ServiceRequestCall svcRC = new ServiceRequestCall(enumRCType.daily, new Date(start), b.getKey(), b.getCount());

            svcRCRepo.save(svcRC);

        });
    }

    public void generateSvcAppPV(long start, long end) throws IOException {
        SearchResult result = getResult(AggReqDSLs.getQueryServiceAPPPV(start, end));

        TermsAggregation svcPV = result.getAggregations().getTermsAggregation("serviceRC");

        svcPV.getBuckets().stream().forEach(svc-> {

            TermsAggregation appRC = svc.getTermsAggregation("appRC");

            appRC.getBuckets().stream().forEach(app -> {

                SvcAppRC svcAppPV = new SvcAppRC(enumRCType.daily, new Date(start), svc.getKey(), app.getKey(), app.getCount());

                logger.debug("##########################");
                logger.debug("SvcAppPV : {}", svcAppPV);
                logger.debug("##########################");

                svcAppRCRepo.save(svcAppPV);
            });


        });
    }


    public SearchResult getResult(String queryString) throws IOException {

        Search.Builder searchBuilder = new Search.Builder(queryString).addIndex(INDEX).addType(INDEX_TYPE).setSearchType(SearchType.COUNT);

        //addIndex("filebeat-2016-w26").addType("log").setSearchType(SearchType.COUNT);

        SearchResult response = null;

        response = client.execute(searchBuilder.build());



        logger.debug(response.getJsonString());

        return response;
    }

    public void generateSvcPVForMonth() {

        LocalDate start = new LocalDate().withDayOfMonth(1);
        start = start.minusMonths(1);
        LocalDate end = new LocalDate().withDayOfMonth(1).minusDays(1);

        final LocalDate finalStart = start;


        svcRCRepo.findDistinctSvcId().stream().forEach(svc -> {

            ServiceRequestCall svcRC = new ServiceRequestCall();

            svcRC.getId().setRcType(enumRCType.monthly);
            svcRC.getId().setReqDt(finalStart.toDate());
            svcRC.getId().setSvcId(svc);

            svcRCRepo.findByRcTypeAndBetweenDates(enumRCType.daily, finalStart.toDate(), end.toDate()).stream().
                    filter(pv -> pv.getId().getSvcId().equals(svc)).forEach(pv -> {

                logger.debug("pv : {}", pv);


                svcRC.setCount(svcRC.getCount() + pv.getCount());


            });

            logger.debug("svcRC : {}", svcRC);

            svcRCRepo.save(svcRC);

        });



    }
}
