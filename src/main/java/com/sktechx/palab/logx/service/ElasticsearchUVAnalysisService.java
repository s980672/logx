package com.sktechx.palab.logx.service;

import com.sktechx.palab.logx.model.*;
import com.sktechx.palab.logx.repository.*;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.search.aggregation.CardinalityAggregation;
import io.searchbox.core.search.aggregation.TermsAggregation;
import io.searchbox.params.SearchType;

import org.elasticsearch.search.aggregations.metrics.cardinality.Cardinality;
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
public class ElasticsearchUVAnalysisService {
	
    Logger logger = LoggerFactory.getLogger(ElasticsearchUVAnalysisService.class);

    @Autowired
    RequestCallRepository rcRepo;

    @Autowired
    ServiceRCRepository svcRCRepo;    

    @Autowired
    SvcOption1RCRepository svcOption1RCRep;
    
    
    @Autowired
    ElasticsearchCommonAnalysisService CommonAnalysisService;
    
    
//    서비스 daily . monthly service 별 uv 
    public void generateSVCUV(enumRCType dayType, String start, String end) throws IOException, ParseException{
    	
    	 SearchResult result = CommonAnalysisService.getResult(AggReqDSLs.getQueryServiceUV(start, end));     	 

    	 TermsAggregation svcUV = result.getAggregations().getTermsAggregation("serviceRC");
    	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
         Date date = sdf.parse(start);
    	 
    	 svcUV.getBuckets().stream().forEach(svc -> {
 
			CardinalityAggregation count = svc.getCardinalityAggregation("uvCount");
			 
			ServiceRequestCall svcRC;
			svcRC = new ServiceRequestCall(enumStatsType.UV, enumRCType.daily, date, svc.getKey(), count.getCardinality());
			
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
    	 
    	 System.out.println("query >> "+AggReqDSLs.getQueryServiceOption1UV(optionField,start, end).toString());
    	 
         TermsAggregation svcUV = result.getAggregations().getTermsAggregation("serviceRC");

         
         svcUV.getBuckets().stream().forEach(svc-> {

             TermsAggregation apiRC = svc.getTermsAggregation("option1RC");  
             
             apiRC.getBuckets().stream().forEach(svcsub -> {
            	 
            	 CardinalityAggregation count = svcsub.getCardinalityAggregation("uvCount");
//            	 System.out.println(">>"+svc.getKey()+"###"+svcsub.getKey()+"##"+count.getCardinality());
            	 
            	 SvcOption1RC svcOp1UV = new SvcOption1RC(enumStatsType.UV, dayType, opType, date, svc.getKey(), svcsub.getKey(), count.getCardinality());

                 logger.debug("##########################");
                 logger.debug("SvcOption1RC : {}", svcOp1UV);
                 logger.debug("##########################");

                 svcOption1RCRep.save(svcOp1UV);
            	 
             });
             	
             
         });
    	
		
	}


}
