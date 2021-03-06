package com.sktechx.palab.logx.service;

import com.sktechx.palab.logx.model.*;
import com.sktechx.palab.logx.repository.RequestCallRepository;
import com.sktechx.palab.logx.repository.ServiceRCRepository;
import com.sktechx.palab.logx.repository.SvcOption1RCRepository;
import com.sktechx.palab.logx.repository.SvcOption2RCRepository;
import com.sktechx.palab.logx.secondary.service.CategoryService;
import io.searchbox.core.SearchResult;
import io.searchbox.core.search.aggregation.CardinalityAggregation;
import io.searchbox.core.search.aggregation.TermsAggregation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 1002382 on 2016. 7. 5..
 */
@Service
public class ElasticsearchUVAnalysisService {

    Logger logger = LoggerFactory.getLogger(ElasticsearchUVAnalysisService.class);

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
    CategoryService categoryService;


    public void generateAllUV(enumRCType rcType, String date1, String date2) throws IOException, ParseException {
        generateSvcUV(rcType, date1, date2);
        generateSvcOption1UV(enumOptionType.API, rcType, date1, date2);
        generateSvcOption1UV(enumOptionType.APP, rcType, date1, date2);
        generateSvcOption2UV(enumOptionType.APP_API, rcType, date1, date2);
        generateSvcOption2UV(enumOptionType.API_APP, rcType, date1, date2);
    }

//    서비스 daily . monthly service 별 uv
    public void generateSvcUV(enumRCType dayType, String start, String end) throws IOException, ParseException{

    	 SearchResult result = CommonAnalysisService.getResult(AggReqDSLs.getQueryServiceUV(start, end));

    	 TermsAggregation svcUV = result.getAggregations().getTermsAggregation("serviceRC");
    	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
         Date date = sdf.parse(start);

    	 svcUV.getBuckets().stream().forEach(svc -> {

			CardinalityAggregation count = svc.getCardinalityAggregation("uvCount");

             try {
                 ServiceRequestCall svcRC = new ServiceRequestCall(enumStatsType.UV, dayType, date,  categoryService.getServiceId(svc.getKey()), svc.getKey(), count.getCardinality());
                 svcRCRepo.save(svcRC);
             } catch (InvalidKeyException e) {
                 logger.debug(e.getLocalizedMessage());
             }

    	 });
    }


    public void generateSvcOption1UV(enumOptionType opType, enumRCType dayType, String start, String end) throws IOException, ParseException{

    	  String optionField = null;
          if(opType == enumOptionType.API){
              optionField = "apiPath";
          }else{
              optionField = "appKey";
          }

          SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
          Date date = sdf.parse(start);

    	 SearchResult result = CommonAnalysisService.getResult(AggReqDSLs.getQueryServiceOption1UV(optionField,start, end));


         TermsAggregation svcUV = result.getAggregations().getTermsAggregation("serviceRC");

         svcUV.getBuckets().stream().forEach(svc-> {

             TermsAggregation option1RC = svc.getTermsAggregation("option1RC");


             option1RC.getBuckets().stream().forEach(svcsub -> {

            	 CardinalityAggregation count = svcsub.getCardinalityAggregation("uvCount");

                 try {
                     SvcOption1RC svcOp1UV = new SvcOption1RC(enumStatsType.UV, dayType, opType, date,
                        categoryService.getServiceId(svc.getKey()), svc.getKey(), svcsub.getKey(), count.getCardinality());
                     svcOption1RCRep.save(svcOp1UV);
                 } catch (InvalidKeyException e) {
                     logger.debug(e.getLocalizedMessage());
                 }

             });
         });
	}


    public void generateSvcOption2UV(enumOptionType opType, enumRCType dayType, String start, String end) throws IOException, ParseException{

		String optionField = null;
		String suboptionField = null;
		if(opType == enumOptionType.API_APP){
			optionField = "apiPath";
			suboptionField = "appKey";
		}else{
			optionField = "appKey";
			suboptionField = "apiPath";
		}


		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse(start);

		SearchResult result = CommonAnalysisService.getResult(AggReqDSLs.getQueryServiceOption2UV(optionField,suboptionField,start, end));
		TermsAggregation svcUV = result.getAggregations().getTermsAggregation("serviceRC");


		svcUV.getBuckets().stream().forEach(svc-> {
			TermsAggregation option1RC = svc.getTermsAggregation("option1RC");


			option1RC.getBuckets().stream().forEach(svcsub1 -> {
				TermsAggregation option2RC = svcsub1.getTermsAggregation("option2RC");

				option2RC.getBuckets().stream().forEach(svcsub2 -> {
					CardinalityAggregation count = svcsub2.getCardinalityAggregation("uvCount");

                    try {
                        SvcOption2RC svcOp2UV = new SvcOption2RC(enumStatsType.UV, dayType, opType, date,
                            categoryService.getServiceId(svc.getKey()), svc.getKey(), svcsub1.getKey(), svcsub2.getKey(),count.getCardinality());
                        logger.debug("##########################");
                        logger.debug("SvcOption1RC : {}", svcOp2UV);
                        logger.debug("##########################");

                        svcOption2RCRep.save(svcOp2UV);

                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    }

               });

           });
       });
	}


}



