package com.sktechx.palab.logx.service;

import com.sktechx.palab.logx.model.enumOptionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by 1002382 on 2016. 7. 7..
 */
@Component
@Configurable

public class ScheduledTasks {

    private Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);


    @Autowired
    ElasticsearchAnalysisService esService;




    //매일 그날의 request call 수를 저장한다
    //매일 0시 5분에 전날 request call를 조회 및 저장
    //@Scheduled(cron="0 5 12 1/1 * *")
    @Scheduled(cron="0/30 * * * * *")
    public void savecDailyPV() throws ParseException, IOException {

        // daily pv
        //전날 pv를 구한다


        String date1 = "2016-07-17";
        String date2 = "2016-07-18";

        esService.generatePV(date1, date2);

        esService.generateSVCPV(date1, date2);

        esService.generateSvcOption1PV(enumOptionType.APP, date1, date2);
        esService.generateSvcOption1PV(enumOptionType.API, date1, date2);
        esService.generateSvcOption1PV(enumOptionType.ERROR, date1, date2);

        esService.generateSvcOption2PV(enumOptionType.API_APP, date1, date2);
        esService.generateSvcOption2PV(enumOptionType.APP_API, date1, date2);
        esService.generateSvcOption2PV(enumOptionType.ERROR_APP, date1, date2);
        esService.generateSvcOption2PV(enumOptionType.ERROR_API, date1, date2);

        //esService.generateErrorCount(date1, date2);



    }


    //주마다 pv를 저장한다
    //매주 월요일 0시 5분 마다 전주 한주의 집계를 저장
    //@Scheduled(cron="0 5 0 ? * 1 *")
    public void saveWeeklyPV() {
        logger.debug("=========================");
        logger.debug("start every week!!");
        logger.debug("=========================");

    }

    //매월 1일 0시 5분 마다
    @Scheduled(cron="0 5 0 1 1/1 ?")
    //@Scheduled(cron="0/3 * * * * *")
    public void saveMonthlyPV() {
        logger.debug("=========================");
        logger.debug("start every monthly!!");
        logger.debug("=========================");

        esService.generateSvcPVForMonth();



    }





}
