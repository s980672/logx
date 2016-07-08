package com.sktechx.palab.logx.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;

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
    //매 1분 마다
    @Scheduled(cron="0 */1 * * * *")
    public void savecDailyPV() {

        // daily pv
        //전날 pv를 구한다
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);


        long end = cal.getTimeInMillis();

        logger.debug("today : {}", cal.getTime());

        cal.add(cal.DATE, -1);

        long start = cal.getTimeInMillis();

        logger.debug("yesterday : {}", cal.getTime());

        logger.debug("start ~ end :  {} ~ {}", start, end);

        //////////////////////////////////
        //특정일에만 데이터가 있어서 테스트 날짜
        //for test
        end = 1467176400000l;
        start = 1467172800000l;
        //////////////////////////////////

        esService.generatePV(start, end);

        esService.generateSVCPV(start, end);







    }


    //주마다 pv를 저장한다
    //매 1분 마다
    @Scheduled(cron="0 */2 * * * *")
    public void saveWeeklyPV() {
        logger.debug("=========================");
        logger.debug("start every week!!");
        logger.debug("=========================");





    }






}
