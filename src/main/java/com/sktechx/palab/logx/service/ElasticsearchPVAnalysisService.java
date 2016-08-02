package com.sktechx.palab.logx.service;

import com.sktechx.palab.logx.model.*;
import com.sktechx.palab.logx.repository.*;
import io.searchbox.core.SearchResult;
import io.searchbox.core.search.aggregation.TermsAggregation;
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
 


    
    public void generatePV(String start, String end) throws IOException, ParseException {
    

        SearchResult response = CommonAnalysisService.getResult(AggReqDSLs.getQueryPVDuringPeriod(start, end));

        logger.debug("total : {}", response.getTotal());

        ReqCall rc = new ReqCall(enumRCType.daily, start, new Long(response.getTotal()));

        rcRepo.save(rc);

    }

    public void generateSVCPV(String start, String end) throws IOException, ParseException {

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

    public void generateSvcOption1PV(enumOptionType opType, String start, String end) throws IOException, ParseException {

    	
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
    

// srvice / app - api / api - app 선택 시 daily / monthly 데이터 생성
    public void generateSvcOption2PV(enumRCType dayType,enumOptionType opType, String start, String end) throws IOException, ParseException {

    	
        String optionField = null;
        String suboptionField = null;
        if(opType == enumOptionType.API_APP){
            optionField = "apiPath";
            suboptionField = "appKey";
        }else{
            optionField = "appKey";
            suboptionField = "apiPath";
        }

        SearchResult result = CommonAnalysisService.getResult(AggReqDSLs.getQueryServiceOption2PV(optionField, suboptionField, start, end));

        TermsAggregation svcPV = result.getAggregations().getTermsAggregation("serviceRC");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(start);


        svcPV.getBuckets().stream().forEach(svc-> {

            TermsAggregation apiRC = svc.getTermsAggregation("option1RC");           

            apiRC.getBuckets().stream().forEach(api -> {
            	
            	TermsAggregation appRC = api.getTermsAggregation("option2RC");
            	
            	appRC.getBuckets().stream().forEach(app ->{        		


	                    SvcOption2RC svcOp2PV = new SvcOption2RC(enumStatsType.PV, dayType, opType, date, svc.getKey(), api.getKey(), app.getKey(), app.getCount());
	
	                    logger.debug("##########################");
	                    logger.debug("SvcOption2RC : {}", svcOp2PV);
	                    logger.debug("##########################");
	
	                    svcOption2RCRep.save(svcOp2PV);
            		
            		}
            	);
            	
    
            });


        });
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
