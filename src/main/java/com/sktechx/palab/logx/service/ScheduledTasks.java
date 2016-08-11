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
    @Scheduled(cron="0 5 00 1/1 * *")
    //@Scheduled(cron="0/30 * * * * *")
    public void savecDailyPVUV() throws ParseException {

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        // 특정 형태의 날짜로 값을 뽑기 
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        오늘날짜
        String date2 = df.format(cal.getTime());
//        어제날짜
        cal.add(Calendar.DATE, -1);
        String date1 = df.format(cal.getTime());    	

        try {

            esService.generatePV(enumRCType.daily, date1, date2);
            esService.generateSvcOption1PV(enumOptionType.APP, enumRCType.daily, date1, date2);
            esService.generateSvcOption1PV(enumOptionType.API, enumRCType.daily, date1, date2);
            esService.generateSvcOption1PV(enumOptionType.ERROR, enumRCType.daily, date1, date2);
            esService.generateSvcOption2PV(enumOptionType.APP_API, enumRCType.daily, date1, date2);
            esService.generateSvcOption2PV(enumOptionType.API_APP, enumRCType.daily, date1, date2);
            esService.generateSvcOptionERROR(enumOptionType.ERROR_API, enumRCType.daily, date1, date2);
            esService.generateSvcOptionERROR(enumOptionType.ERROR_APP, enumRCType.daily, date1, date2);

            esUVService.generateSvcUV(enumRCType.daily, date1, date2);
            esUVService.generateSvcOption1UV(enumOptionType.API, enumRCType.daily, date1, date2);
            esUVService.generateSvcOption1UV(enumOptionType.APP, enumRCType.daily, date1, date2);
            esUVService.generateSvcOption2UV(enumOptionType.APP_API, enumRCType.daily, date1, date2);
            esUVService.generateSvcOption2UV(enumOptionType.API_APP, enumRCType.daily, date1, date2);


        } catch (IOException e) {

            logger.error(e.getLocalizedMessage());

            e.printStackTrace();
        }

    }

    //@Scheduled(cron="0/3 * * * * *")
    public void testMonthlyPV() throws IOException, ParseException {

        logger.debug("=========================");
        logger.debug("start every monthly!!");
        logger.debug("=========================");

        LocalDate start = LocalDate.of(2016, 5, 1);

        //LocalDate tmp = LocalDate.now();

        //LocalDate start = LocalDate.of(tmp.getYear(), tmp.getMonth(), 1);

        LocalDate end = start.plusMonths(5);

        for (; start.isBefore(end); start=start.plusMonths(1) ) {

            String date1 = start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String date2 = start.plusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            logger.debug("date1 : {} -- date2 : {} -- end : {}", date1, date2, end);

            esService.generateSvcPV(enumRCType.monthly, date1, date2);
            esService.generateSvcOption1PV(enumOptionType.API, enumRCType.monthly, date1, date2);
            esService.generateSvcOption1PV(enumOptionType.APP, enumRCType.monthly, date1, date2);
            esService.generateSvcOption1PV(enumOptionType.ERROR, enumRCType.monthly, date1, date2);

            esService.generateSvcOptionERROR(enumOptionType.ERROR_API, enumRCType.monthly, date1, date2);
            esService.generateSvcOptionERROR(enumOptionType.ERROR_APP, enumRCType.monthly, date1, date2);

            esService.generateSvcOption2PV(enumOptionType.APP_API, enumRCType.monthly, date1, date2);
            esService.generateSvcOption2PV(enumOptionType.API_APP, enumRCType.monthly, date1, date2);
            esService.generateSvcOption2PV(enumOptionType.ERROR_API, enumRCType.monthly, date1, date2);
            esService.generateSvcOption2PV(enumOptionType.ERROR_APP, enumRCType.monthly, date1, date2);

            esUVService.generateSvcUV(enumRCType.monthly, date1, date2);
            esUVService.generateSvcOption1UV(enumOptionType.API, enumRCType.monthly, date1, date2);
            esUVService.generateSvcOption1UV(enumOptionType.APP, enumRCType.monthly, date1, date2);
            esUVService.generateSvcOption2UV(enumOptionType.APP_API, enumRCType.monthly, date1, date2);
            esUVService.generateSvcOption2UV(enumOptionType.API_APP, enumRCType.monthly, date1, date2);

        }
    }


    //매월 1일 0시 5분 마다
    //@Scheduled(cron="0 5 0 1 1/1 ?")
    //@Scheduled(cron="0/3 * * * * *")
    //매일 0시 30분에 전날 request call를 조회 및 저장
    @Scheduled(cron="0 10 0 1/1 * *")
    public void saveMonthlyPV() throws IOException, ParseException {

        logger.debug("=========================");
        logger.debug("start every monthly!!");
        logger.debug("=========================");

        //LocalDate start = LocalDate.of(2016, 5, 1);

        LocalDate tmp = LocalDate.now();

        LocalDate start = LocalDate.of(tmp.getYear(), tmp.getMonth(), 1);

        LocalDate end = start.plusMonths(1);

        String date1 = start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String date2 = end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        logger.debug("start date : {} - end date : {}", date1, date2);

        esService.generateSvcPV(enumRCType.monthly, date1, date2);
        esService.generateSvcOption1PV(enumOptionType.API, enumRCType.monthly, date1, date2);
        esService.generateSvcOption1PV(enumOptionType.APP, enumRCType.monthly, date1, date2);
        esService.generateSvcOption1PV(enumOptionType.ERROR, enumRCType.monthly, date1, date2);

        esService.generateSvcOption2PV(enumOptionType.APP_API, enumRCType.monthly, date1, date2);
        esService.generateSvcOption2PV(enumOptionType.API_APP, enumRCType.monthly, date1, date2);
        esService.generateSvcOption2PV(enumOptionType.ERROR_API, enumRCType.monthly, date1, date2);
        esService.generateSvcOption2PV(enumOptionType.ERROR_APP, enumRCType.monthly, date1, date2);
        esService.generateSvcOptionERROR(enumOptionType.ERROR_API, enumRCType.monthly, date1, date2);
        esService.generateSvcOptionERROR(enumOptionType.ERROR_APP, enumRCType.monthly, date1, date2);

        esUVService.generateSvcUV(enumRCType.monthly, date1, date2);
        esUVService.generateSvcOption1UV(enumOptionType.API, enumRCType.monthly, date1, date2);
        esUVService.generateSvcOption1UV(enumOptionType.APP, enumRCType.monthly, date1, date2);
        esUVService.generateSvcOption2UV(enumOptionType.APP_API, enumRCType.monthly, date1, date2);
        esUVService.generateSvcOption2UV(enumOptionType.API_APP, enumRCType.monthly, date1, date2);



    }


}
