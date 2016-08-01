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
public class ElasticsearchUVAnalysisService {
	
    Logger logger = LoggerFactory.getLogger(ElasticsearchUVAnalysisService.class);

    @Autowired
    RequestCallRepository rcRepo;

    @Autowired
    ServiceRCRepository svcRCRepo;
    
    @Autowired
    ElasticsearchCommonAnalysisService CommonAnalysisService;
    
    
//    서비스 daily . monthly service 별 uv 
    public void generateSVUV(enumRCType dayType, String start, String end) throws IOException, ParseException{
    	
    	 SearchResult response = CommonAnalysisService.getResult(AggReqDSLs.getQueryServiceUV(start, end));
      	 
    	 System.out.println("query >>  "+AggReqDSLs.getQueryServiceUV(start, end).toString());
    	 System.out.println("1111111111111");

         TermsAggregation svcPV = response.getAggregations().getTermsAggregation("serviceRC");

         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
         Date date = sdf.parse(start);

         svcPV.getBuckets().stream().forEach(svc -> {          	 
        	 
        	 System.out.println("1111111111111");
        	 
        	 TermsAggregation uvSvc = svc.getTermsAggregation("uvCount");
        	 
        	 System.out.println("22222222222222");
        	 
        	 uvSvc.getBuckets().stream().forEach(svcsub->{
        		 
        		 System.out.println("###########"+svc.getKey());
        		 System.out.println("###########"+svcsub.getKey());
        		
        	 });
        	 

//             ServiceRequestCall svcRC = new ServiceRequestCall(dayType, date, b.getKey(), b.getCount());
//
//             svcRCRepo.save(svcRC);

         });
    	
    }


}
