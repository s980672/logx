package com.sktechx.palab.logx.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
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
    //매일 0시 5분에 전날 request call를 조회 및 저장
    @Scheduled(cron="0 5 12 1/1 * *")
    //@Scheduled(cron="0/3 * * * * *")
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
        //6/29
        end = 1467176400000l;
        start = 1467172800000l;
        //////////////////////////////////

        //6/30
        end = 1467298800000l;
        start = 1467212400000l;

        try {
            esService.generatePV(start, end);

            esService.generateSVCPV(start, end);

            esService.generateSvcAppPV(start, end);


        } catch (IOException e) {

            logger.error(e.getLocalizedMessage());

            e.printStackTrace();
        }

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
