package com.sktechx.palab.logx.service;

import com.sktechx.palab.logx.model.*;
import com.sktechx.palab.logx.repository.RequestCallRepository;
import com.sktechx.palab.logx.repository.ServiceRCRepository;
import com.sktechx.palab.logx.repository.SvcOption1RCRepository;
import com.sktechx.palab.logx.repository.SvcOption2RCRepository;
import io.searchbox.core.SearchResult;
import io.searchbox.core.search.aggregation.TermsAggregation;
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
public class ElasticsearchPVAnalysisService {

    Logger logger = LoggerFactory.getLogger(ElasticsearchPVAnalysisService.class);



    @Autowired
    RequestCallRepository rcRepo;

    @Autowired
    ServiceRCRepository svcRCRepo;

    @Autowired
    SvcOption1RCRepository svcOption1RCRep;

    @Autowired
    SvcOption2RCRepository svcOption2RCRep;

    @Autowired
    ElasticsearchCommonAnalysisService CommonAnalysisService;




    public void generatePV(enumRCType rcType, String start, String end) throws IOException, ParseException {


        SearchResult response = CommonAnalysisService.getResult(AggReqDSLs.getQueryPVDuringPeriod(start, end));

        logger.debug("total : {}", response.getTotal());

        ReqCall rc = new ReqCall(rcType, start, new Long(response.getTotal()));

        rcRepo.save(rc);

    }

    public void generateSvcPV(enumRCType rcType, String start, String end) throws IOException, ParseException {

        ///////////////////////////////////////////////////////////////////////////////
        //service pv

        SearchResult response = CommonAnalysisService.getResult(AggReqDSLs.getQueryServicePV(start, end));


        TermsAggregation svcPV = response.getAggregations().getTermsAggregation("serviceRC");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(start);

        svcPV.getBuckets().stream().forEach(b -> {

            ServiceRequestCall svcRC = new ServiceRequestCall(enumStatsType.PV, enumRCType.daily, date, b.getKey(), b.getCount());

            svcRCRepo.save(svcRC);

        });
    }

    public void generateSvcOption1PV(enumRCType rcType, enumOptionType opType, String start, String end) throws IOException, ParseException {


        String optionField = null;
        if(opType == enumOptionType.API){
            optionField = "apiPath";
        }else{
            optionField = "appKey";
        }

        SearchResult result = CommonAnalysisService.getResult(AggReqDSLs.getQueryServiceOption1PV(optionField, start, end));

        TermsAggregation svcPV = result.getAggregations().getTermsAggregation("serviceRC");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(start);


        svcPV.getBuckets().stream().forEach(svc-> {

            TermsAggregation appRC = svc.getTermsAggregation("option1RC");

            appRC.getBuckets().stream().forEach(app -> {

                SvcOption1RC svcOp1PV = new SvcOption1RC(enumStatsType.PV, enumRCType.daily, opType, date, svc.getKey(), app.getKey(), app.getCount());

                logger.debug("##########################");
                logger.debug("SvcOption1RC : {}", svcOp1PV);
                logger.debug("##########################");

                svcOption1RCRep.save(svcOp1PV);
            });


        });
    }


    public void generateSvcOption2PV(enumRCType rcType, enumOptionType opType, String start, String end) throws IOException, ParseException {

        String queryDsl = null;
        String queryOption1Option2AllSvcPV = null;
        switch(opType){
            case APP_API:
                queryDsl = AggReqDSLs.getQueryServiceOption2PV("appKey", "apiPath", start, end);
                break;
            case API_APP:
                queryDsl = AggReqDSLs.getQueryServiceOption2PV("apiPath", "appKey", start, end);
                queryOption1Option2AllSvcPV = AggReqDSLs.getQueryOption1Option2AllSvcPV("appKey", "apiPath", start, end);
                break;
            case ERROR_API:
                queryDsl = AggReqDSLs.getQueryServiceOption2PV("responseCode", "apiPath", start, end);
                queryOption1Option2AllSvcPV = AggReqDSLs.getQueryOption1Option2AllSvcPV("responseCode", "apiPath", start, end);
                break;
            case ERROR_APP:
                queryDsl = AggReqDSLs.getQueryServiceOption2PV("responseCode", "appKey", start, end);
                queryOption1Option2AllSvcPV = AggReqDSLs.getQueryOption1Option2AllSvcPV("responseCode", "appKey", start, end);
        }

        logger.debug(queryDsl);

        SearchResult result = CommonAnalysisService.getResult(queryDsl);


        TermsAggregation svcPV = result.getAggregations().getTermsAggregation("serviceRC");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(start);


        svcPV.getBuckets().stream().forEach(svc-> {

            TermsAggregation apiRC = svc.getTermsAggregation("option1RC");

            apiRC.getBuckets().stream().forEach(api -> {

            	TermsAggregation appRC = api.getTermsAggregation("option2RC");

            	appRC.getBuckets().stream().forEach(app ->{


	                    SvcOption2RC svcOp2PV = new SvcOption2RC(enumStatsType.PV, rcType, opType, date, svc.getKey(), api.getKey(), app.getKey(), app.getCount());

	                    logger.debug("##########################");
	                    logger.debug("SvcOption2RC : {}", svcOp2PV);
	                    logger.debug("##########################");

	                    svcOption2RCRep.save(svcOp2PV);

            		}
            	);


            });


        });


    }


}
