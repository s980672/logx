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
public class ElasticsearchAnalysisService {

    Logger logger = LoggerFactory.getLogger(ElasticsearchAnalysisService.class);


    @Autowired
    private JestClient client;

    private final String INDEX = "bulk-gateway-log";
    private final String INDEX_TYPE = "log";



    @Autowired
    RequestCallRepository rcRepo;

    @Autowired
    ServiceRCRepository svcRCRepo;

    @Autowired
    SvcOption1RCRepository svcOption1RCRep;

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
    public void generatePV(String start, String end) throws IOException, ParseException {




        SearchResult response = getResult(AggReqDSLs.getQueryPVDuringPeriod(start, end));

        logger.debug("total : {}", response.getTotal());

        ReqCall rc = new ReqCall(enumRCType.daily, start, new Long(response.getTotal()));

        rcRepo.save(rc);

    }

    public void generateSVCPV(String start, String end) throws IOException, ParseException {

        ///////////////////////////////////////////////////////////////////////////////
        //service pv

        SearchResult response = getResult(AggReqDSLs.getQueryServicePV(start, end));


        TermsAggregation svcPV = response.getAggregations().getTermsAggregation("serviceRC");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(start);

        svcPV.getBuckets().stream().forEach(b -> {

            ServiceRequestCall svcRC = new ServiceRequestCall(enumRCType.daily, date, b.getKey(), b.getCount());

            svcRCRepo.save(svcRC);

        });
    }

    public void generateSvcOption1PV(enumOption1Type opType, String start, String end) throws IOException, ParseException {

        String optionField = null;
        if(opType == enumOption1Type.API){
            optionField = "apiPath";
        }else{
            optionField = "appKey";
        }

        SearchResult result = getResult(AggReqDSLs.getQueryServiceOption1PV(optionField, start, end));

        TermsAggregation svcPV = result.getAggregations().getTermsAggregation("serviceRC");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(start);


        svcPV.getBuckets().stream().forEach(svc-> {

            TermsAggregation appRC = svc.getTermsAggregation("option1RC");

            appRC.getBuckets().stream().forEach(app -> {

                SvcOption1RC svcOp1PV = new SvcOption1RC(enumRCType.daily, opType, date, svc.getKey(), app.getKey(), app.getCount());

                logger.debug("##########################");
                logger.debug("SvcAppPV : {}", svcOp1PV);
                logger.debug("##########################");

                svcOption1RCRep.save(svcOp1PV);
            });


        });
    }


    private SearchResult getResult(String queryString) throws IOException {

        Search.Builder searchBuilder = new Search.Builder(queryString).addIndex(INDEX).addType(INDEX_TYPE).setSearchType(SearchType.COUNT);

        SearchResult response = client.execute(searchBuilder.build());

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
