package com.sktechx.palab.logx.service;

import com.sktechx.palab.logx.model.enumOptionType;
import com.sktechx.palab.logx.model.enumRCType;
import com.sktechx.palab.logx.model.enumStatsType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by 1002382 on 2016. 7. 7..
 */
@Component
@Configurable
public class ScheduledTasks {

    private Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);


    @Autowired
    ElasticsearchPVAnalysisService esService;
    

    @Autowired
    ElasticsearchUVAnalysisService esUVService;


    //매일 그날의 request call 수를 저장한다
    //매일 0시 5분에 전날 request call를 조회 및 저장
    //@Scheduled(cron="0 5 12 1/1 * *")
    @Scheduled(cron="0/30 * * * * *")
    public void savecDailyPV() throws ParseException {

        // daily pv
        //전날 pv를 구한다
    	String date1;
    	String date2 = null;

    	date1 =  "2016-05-02";
    	date2 =  "2016-05-03";		

	        try {
	
	        		
//		            esService.generatePV(date1, date2);
		
//		            esService.generateSVCPV(date1, date2);
//		
//		            esService.generateSvcOption1PV(enumOptionType.API, date1, date2);
//		            esService.generateSvcOption2PV(enumRCType.daily, enumOptionType.APP_API, date1, date2);
//		            esService.generateSvcOption2PV(enumRCType.monthly,enumOptionType.APP_API, date1, date2);

		            esService.generateSvcOption1PV(enumRCType.daily,enumOptionType.API, date1, date1);
		            esService.generateSvcOption2PV(enumRCType.daily,enumOptionType.APP_API, date1, date2);
		            esService.generateSvcOption2PV(enumRCType.daily,enumOptionType.APP_API, date1, date2);
		            
//		            esUVService.generateSVCUV(enumRCType.daily, date1, date2);
//		            esUVService.generateSvcOption1UV(enumOptionType.API, enumRCType.daily, date1, date2);
//		            esUVService.generateSvcOption1UV(enumOptionType.APP, enumRCType.daily, date1, date2);
		        	esUVService.generateSvcOption2UV(enumOptionType.APP_API, enumRCType.daily, date1, date2);
		        	esUVService.generateSvcOption2UV(enumOptionType.APP_API,enumRCType.monthly, date1, date2);
	
		        	esUVService.generateSvcOption2UV(enumOptionType.API_APP, enumRCType.daily, date1, date2);
		        	esUVService.generateSvcOption2UV(enumOptionType.API_APP,enumRCType.monthly, date1, date2);
		        	
		
	        } catch (IOException e) {
	
	            logger.error(e.getLocalizedMessage());
	
	            e.printStackTrace();
	        }	    

    }

    //매월 1일 0시 5분 마다
    //@Scheduled(cron="0 5 0 1 1/1 ?")
    @Scheduled(cron="0/3 * * * * *")
    public void saveMonthlyPV() throws IOException, ParseException {

        logger.debug("=========================");
        logger.debug("start every monthly!!");
        logger.debug("=========================");

        LocalDate start = LocalDate.of(2016, 5, 1);

        LocalDate end = start.plusMonths(1);

        String date1 = start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String date2 = end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        esService.generateSvcOption1PV(enumRCType.monthly, enumOptionType.API, date1, date1);
        esService.generateSvcOption1PV(enumRCType.monthly, enumOptionType.APP, date1, date1);
        esService.generateSvcOption1PV(enumRCType.monthly, enumOptionType.ERROR, date1, date1);

        esService.generateSvcOption2PV(enumRCType.monthly, enumOptionType.APP_API, date1, date2);
        esService.generateSvcOption2PV(enumRCType.monthly, enumOptionType.API_APP, date1, date2);
        esService.generateSvcOption2PV(enumRCType.monthly, enumOptionType.ERROR_API, date1, date2);
        esService.generateSvcOption2PV(enumRCType.monthly, enumOptionType.ERROR_APP, date1, date2);

        esService.generateSvcPV(enumRCType.monthly, date1, date2);

    }





}
