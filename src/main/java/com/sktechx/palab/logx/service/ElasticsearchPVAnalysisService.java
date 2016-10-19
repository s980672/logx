package com.sktechx.palab.logx.service;

import com.google.common.collect.Lists;
import com.sktechx.palab.logx.model.*;
import com.sktechx.palab.logx.repository.RequestCallRepository;
import com.sktechx.palab.logx.repository.ServiceRCRepository;
import com.sktechx.palab.logx.repository.SvcOption1RCRepository;
import com.sktechx.palab.logx.repository.SvcOption2RCRepository;
import com.sktechx.palab.logx.secondary.service.CategoryService;
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
import java.util.List;

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

    @Autowired
    ChangeNameIdService chgSvc;

    @Autowired
    CategoryService categoryService;


    //최근 4주 또는 4달간 데이터를 리턴
    public List<ReqCall> getPV(enumRCType rcType, Date date1, Date date2){
        return rcRepo.findByRcTypeAndBetween(rcType, date1, date2);
    }

    public List<ServiceRequestCall> getSvcPVTop5(enumRCType rcType, Date date1, Date date2) {
        List<String> top5Svc = svcRCRepo.findTop5Svc(rcType, date1, date2);

        List<ServiceRequestCall> result = Lists.newArrayList();


        top5Svc.stream().limit(5).forEach(svc -> {
            logger.debug("service : {}", svc);

            List<ServiceRequestCall> lst = svcRCRepo.findSumGroupBySvcIdBySvcIdAndStsTypeAndRcTypeAndBetween(svc, enumStatsType.PV, rcType, date1, date2);
            result.addAll(lst);
        });

        result.stream().forEach(r -> logger.debug("{}", r));

        chgSvc.fillNameOrIdOfAppOrSvc(enumOptionType.SVC, result);
        return result;
    }

    public List<SvcOption1RC> getRCPerApiTop10(enumRCType rcType, Date date1, Date date2) {
        //RC 갯수로 top10 API를 구한다
        List<String> top10Api = svcOption1RCRep.findTop10Option1(enumOptionType.API, rcType, date1, date2);
        List<SvcOption1RC> result = Lists.newArrayList();
        top10Api.stream().limit(10).forEach(api -> {
            SvcOption1RC tmp = new SvcOption1RC();
            svcOption1RCRep.findByOption1AndRcTypeAndBetween(api, enumOptionType.API, rcType, date1, date2).forEach(rc -> {
                tmp.setId(rc.getId());
                //TODO check logic
                tmp.setCount(tmp.getCount() + rc.getCount());
            });
            result.add(tmp);
        });

        return result;
    }

    public List<SvcOption1RC> getRCPerAppTop10(enumRCType rcType, Date date1, Date date2){
        //RC 갯수로 top10 APP를 구한다
        List<String> top10Api = svcOption1RCRep.findTop10Option1(enumOptionType.APP, rcType, date1, date2);
        List<SvcOption1RC> result = Lists.newArrayList();
        top10Api.stream().limit(10).forEach(app -> {
            SvcOption1RC tmp = new SvcOption1RC();
            svcOption1RCRep.findByOption1AndRcTypeAndBetween(app, enumOptionType.APP, rcType, date1, date2).stream().forEach(rc -> {
                tmp.setId(rc.getId());
                //서비스 별로 구분되는 값을 모두 더함
                tmp.setCount(tmp.getCount() + rc.getCount());
            });
            result.add(tmp);
        });
        chgSvc.fillNameOrIdOfAppOrSvc(enumOptionType.APP, result);
        return result;
    }


    public void generateAllPV(enumRCType rcType, String date1, String date2) throws IOException, ParseException {
        generatePV(rcType, date1, date2);
        generateSvcPV(rcType, date1, date2);
        generateSvcOption1PV(enumOptionType.APP, rcType, date1, date2);
        generateSvcOption1PV(enumOptionType.API, rcType, date1, date2);
        generateSvcOption1PV(enumOptionType.ERROR, rcType, date1, date2);
        generateSvcOption2PV(enumOptionType.APP_API, rcType, date1, date2);
        generateSvcOption2PV(enumOptionType.API_APP, rcType, date1, date2);
        generateSvcOption2PV(enumOptionType.ERROR_API, rcType, date1, date2);
        generateSvcOption2PV(enumOptionType.ERROR_APP, rcType, date1, date2);

        generateSvcOptionERROR(enumOptionType.ERROR_API, rcType, date1, date2);
        generateSvcOptionERROR(enumOptionType.ERROR_APP, rcType, date1, date2);
    }

    public void generatePV(enumRCType dayType, String start, String end) throws IOException, ParseException {


        SearchResult response = CommonAnalysisService.getResult(AggReqDSLs.getQueryPVDuringPeriod(start, end));

        logger.debug("total : {}", response.getTotal());

        ReqCall rc = new ReqCall(dayType, start, new Long(response.getTotal()));

        rcRepo.save(rc);

    }

    public void generateSvcPV(enumRCType rcType, String start, String end) throws IOException, ParseException {

        ///////////////////////////////////////////////////////////////////////////////
        //service pv

        SearchResult response = CommonAnalysisService.getResult(AggReqDSLs.getQueryServicePV(start, end));

        //service id
//        Map<String,String> serviceId =  secondaryService.GetServiceId();

        TermsAggregation svcPV = response.getAggregations().getTermsAggregation("serviceRC");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(start);

        svcPV.getBuckets().stream().forEach(b -> {

            try {
                ServiceRequestCall svcRC = new ServiceRequestCall(enumStatsType.PV, rcType, date,
                        categoryService.getServiceId(b.getKey())
                        , b.getKey(), b.getCount());

                svcRCRepo.save(svcRC);
            }catch (Exception e) {
                logger.error(e.getLocalizedMessage());
            }

        });
    }

    public void generateSvcOption1PV(enumOptionType opType,enumRCType dayType, String start, String end) throws IOException, ParseException {

        
        String queryDsl = null;
        String queryOption1AllSvcPV = null;
        switch(opType){
            case APP:
                queryDsl = AggReqDSLs.getQueryServiceOption1PV("appKey", start, end);   
                break;
            case API:
                queryDsl = AggReqDSLs.getQueryServiceOption1PV("apiPath", start, end);    
                break;
            case ERROR:
                queryDsl = AggReqDSLs.getQueryServiceOption1PV("responseCode", start, end);
        }

        logger.debug(queryDsl);
        

        SearchResult result = CommonAnalysisService.getResult(queryDsl);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(start);


		TermsAggregation svcPV = result.getAggregations().getTermsAggregation("serviceRC");
		svcPV.getBuckets().stream().forEach(svc-> {

            TermsAggregation appRC = svc.getTermsAggregation("option1RC");


            appRC.getBuckets().stream().forEach(app -> {


                SvcOption1RC svcOp1PV = new SvcOption1RC(enumStatsType.PV, dayType, opType, date,
                        categoryService.getServiceId(svc.getKey()), svc.getKey(),app.getKey(), app.getCount());

                logger.debug("##########################");
                logger.debug("SvcOption1RC : {}", svcOp1PV);
                logger.debug("##########################");

                svcOption1RCRep.save(svcOp1PV);
            });


        });
    }


    public void generateSvcOption2PV(enumOptionType opType, enumRCType dayType, String start, String end) throws IOException, ParseException {

        String queryDsl = null;        
        switch(opType){
            case APP_API:
                queryDsl = AggReqDSLs.getQueryServiceOption2PV("appKey", "apiPath", start, end);                
                break;
            case API_APP:
                queryDsl = AggReqDSLs.getQueryServiceOption2PV("apiPath", "appKey", start, end);
                break;
            case ERROR_API:
                queryDsl = AggReqDSLs.getQueryServiceOption2PV("responseCode", "apiPath", start, end);
                break;
            case ERROR_APP:
                queryDsl = AggReqDSLs.getQueryServiceOption2PV("responseCode", "appKey", start, end);
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
            
                    		
                    		SvcOption2RC svcOp2PV = new SvcOption2RC(enumStatsType.PV, dayType, opType, date,
                                    categoryService.getServiceId(svc.getKey()),svc.getKey(), api.getKey(), app.getKey(), app.getCount());
                    	
                    		

	                    logger.debug("##########################");
	                    logger.debug("SvcOption2RC : {}", svcOp2PV);
	                    logger.debug("##########################");

	                    svcOption2RCRep.save(svcOp2PV);

            		}
            	);
            });
        });
    }
    
    public void generateSvcOptionERROR(enumOptionType opType, enumRCType dayType, String start, String end) throws IOException, ParseException {
    	
    	String queryDsl = null;        
        switch(opType){
            case ERROR_API:
                queryDsl = AggReqDSLs.getQueryOption1Option2AllSvcPV("responseCode", "apiPath", start, end);
                break;
            case ERROR_APP:
                queryDsl = AggReqDSLs.getQueryOption1Option2AllSvcPV("responseCode", "appKey", start, end);
        }
        
        logger.debug(queryDsl);

        SearchResult result = CommonAnalysisService.getResult(queryDsl);
        TermsAggregation svcPV = result.getAggregations().getTermsAggregation("option1RC");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(start);

        svcPV.getBuckets().stream().forEach(svc-> {
    		TermsAggregation apiRC = svc.getTermsAggregation("option2RC");


            apiRC.getBuckets().stream().forEach(svcsub -> {
                    		
	        	SvcOption2RC svcOp2PV = new SvcOption2RC(enumStatsType.PV, dayType, opType, date,
                        "ALL", "ALL",svc.getKey(), svcsub.getKey(), svcsub.getCount());
	        		
	
	            logger.debug("##########################");
	            logger.debug("SvcOption2RC : {}", svcOp2PV);
	            logger.debug("##########################");
	
	            svcOption2RCRep.save(svcOp2PV);
        	});
            
        });
    	
    }

}
