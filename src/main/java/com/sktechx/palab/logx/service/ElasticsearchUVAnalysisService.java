package com.sktechx.palab.logx.service;

import com.sktechx.palab.logx.model.*;
import com.sktechx.palab.logx.repository.*;
import io.searchbox.core.SearchResult;
import io.searchbox.core.search.aggregation.CardinalityAggregation;
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
    
    
//    서비스 daily . monthly service 별 uv 
    public void generateSvcUV(enumRCType dayType, String start, String end) throws IOException, ParseException{
    	
    	 SearchResult result = CommonAnalysisService.getResult(AggReqDSLs.getQueryServiceUV(start, end));     	 

    	 TermsAggregation svcUV = result.getAggregations().getTermsAggregation("serviceRC");
    	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
         Date date = sdf.parse(start);
    	 
    	 svcUV.getBuckets().stream().forEach(svc -> {
 
			CardinalityAggregation count = svc.getCardinalityAggregation("uvCount");
			 
			ServiceRequestCall svcRC;
			svcRC = new ServiceRequestCall(enumStatsType.UV, dayType, date, svc.getKey(), count.getCardinality());
			
			svcRCRepo.save(svcRC);
    		 
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
            	 
            	 SvcOption1RC svcOp1UV = new SvcOption1RC(enumStatsType.UV, dayType, opType, date, svc.getKey(), svcsub.getKey(), count.getCardinality());
                 svcOption1RCRep.save(svcOp1UV);
            	 
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
	          		          	 
					SvcOption2RC svcOp2UV = new SvcOption2RC(enumStatsType.UV, dayType, opType, date, svc.getKey(), svcsub1.getKey(), svcsub2.getKey(),count.getCardinality());	          	 
					
	
					logger.debug("##########################");
					logger.debug("SvcOption1RC : {}", svcOp2UV);
					logger.debug("##########################");
					
					svcOption2RCRep.save(svcOp2UV);
               });
          	 
           });
       });    
	}


}
