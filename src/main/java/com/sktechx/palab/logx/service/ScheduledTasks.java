package com.sktechx.palab.logx.service;

import com.sktechx.palab.logx.model.enumOptionType;
import com.sktechx.palab.logx.model.enumRCType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
    @Scheduled(cron="0 5 12 1/1 * *")
    //@Scheduled(cron="0/30 * * * * *")
    public void savecDailyPV() throws ParseException {

        // daily pv
        //전날 pv를 구한다
        String date1 = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String date2 = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        try {

            //////////////////////////////////////
            //UV
            esUVService.generateSVUV(enumRCType.daily, date1, date1);

            ////////////////////////////////////
            //PV
            esService.generateSvcPV(enumRCType.daily, date1, date2);
            esService.generateSvcOption1PV(enumRCType.daily, enumOptionType.API, date1, date1);
            esService.generateSvcOption1PV(enumRCType.daily, enumOptionType.APP, date1, date1);
            esService.generateSvcOption1PV(enumRCType.daily, enumOptionType.ERROR, date1, date1);
            esService.generateSvcOption2PV(enumRCType.daily, enumOptionType.APP_API, date1, date2);
            esService.generateSvcOption2PV(enumRCType.daily, enumOptionType.API_APP, date1, date2);
            esService.generateSvcOption2PV(enumRCType.daily, enumOptionType.ERROR_APP, date1, date2);
            esService.generateSvcOption2PV(enumRCType.daily, enumOptionType.ERROR_API, date1, date2);


        } catch (IOException e) {

            logger.error(e.getLocalizedMessage());

            e.printStackTrace();
        }

    }

    //매월 1일 0시 5분 마다
    //@Scheduled(cron="0 5 0 1 1/1 ?")
    //매일 12시 5분
    @Scheduled(cron="0 5 12 1/1 * *")
    //매 3초 마다
    //@Scheduled(cron="0/3 * * * * *")
    public void saveMonthlyPV() throws IOException, ParseException {

        logger.debug("=========================");
        logger.debug("start every monthly!!");
        logger.debug("=========================");

        LocalDate tmp = LocalDate.now();

        //LocalDate start = LocalDate.of(tmp.getYear(), tmp.getMonth(), 1);

        LocalDate start = LocalDate.of(tmp.getYear(), 5, 1);

        LocalDate end = start.plusMonths(1);

        String date1 = start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String date2 = end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        esService.generateSvcPV(enumRCType.monthly, date1, date2);
        esService.generateSvcOption1PV(enumRCType.monthly, enumOptionType.API, date1, date1);
        esService.generateSvcOption1PV(enumRCType.monthly, enumOptionType.APP, date1, date1);
        esService.generateSvcOption1PV(enumRCType.monthly, enumOptionType.ERROR, date1, date1);

        esService.generateSvcOption2PV(enumRCType.monthly, enumOptionType.APP_API, date1, date2);
        esService.generateSvcOption2PV(enumRCType.monthly, enumOptionType.API_APP, date1, date2);
        esService.generateSvcOption2PV(enumRCType.monthly, enumOptionType.ERROR_API, date1, date2);
        esService.generateSvcOption2PV(enumRCType.monthly, enumOptionType.ERROR_APP, date1, date2);


    }





}
