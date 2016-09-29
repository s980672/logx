package com.sktechx.palab.logx.service;

import com.sktechx.palab.logx.model.enumOptionType;
import com.sktechx.palab.logx.model.enumRCType;
import com.sktechx.palab.logx.secondary.service.CategoryService;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
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
    ElasticsearchCommonAnalysisService commonService;
    @Autowired
    ElasticsearchPVAnalysisService esService;



    @Autowired
    ElasticsearchUVAnalysisService esUVService;

    @Autowired
    CategoryService categoryService;


    //매일 그날의 request call 수를 저장한다
    //매일 0시 5분에 전날 request call를 조회 및 저장
    @Scheduled(cron = "0 5 00 1/1 * *")
//    @Scheduled(cron="0/30 * * * * *")
    public void saveDailyPVUV() throws ParseException {

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
            esService.generateAllPV(enumRCType.daily, date1, date2);
            esUVService.generateAllUV(enumRCType.daily, date1, date2);


        } catch (IOException e) {

            logger.error(e.getLocalizedMessage());

            e.printStackTrace();
        }

    }

//    @Scheduled(cron="0/3 * * * * *")
    public void testMonthlyPV() throws IOException, ParseException {

        logger.debug("=========================");
        logger.debug("start every monthly!!");
        logger.debug("=========================");

        LocalDate start = LocalDate.parse("2016-05-01", DateTimeFormat.forPattern("yyyy-MM-dd"));

        //LocalDate tmp = LocalDate.now();

        //LocalDate start = LocalDate.of(tmp.getYear(), tmp.getMonth(), 1);

        LocalDate end = start.plusMonths(5);

        for (; start.isBefore(end); start = start.plusMonths(1)) {

            String date1 = start.toString("yyyy-MM-dd");
            String date2 = start.plusMonths(1).toString("yyyy-MM-dd");

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


    @Scheduled(cron = "0 20 00 1/1 * *")
    public void saveWeeklyPV() throws IOException, ParseException {

        logger.debug("=========================");
        logger.debug("start generate weekly!!");
        logger.debug("=========================");

        LocalDate previousSunday = LocalDate.now().withDayOfWeek(DateTimeConstants.MONDAY).minusDays(1);

        LocalDate start = previousSunday.minusDays(6);//MONDAY
        LocalDate end = previousSunday;

        String date1 = start.toString("yyyy-MM-dd");
        String date2 = end.toString("yyyy-MM-dd");

        logger.debug("weekly :: start date : {} - end date : {}", date1, date2);

        esService.generateAllPV(enumRCType.weekly, date1, date2);
    }

    //매월 1일 0시 5분 마다
    //@Scheduled(cron="0 5 0 1 1/1 ?")
    //@Scheduled(cron="0/3 * * * * *")
    //매일 0시 30분에 전날 request call를 조회 및 저장
    @Scheduled(cron = "0 30 00 1/1 * *")
    public void saveMonthlyPV() throws IOException, ParseException {

        logger.debug("=========================");
        logger.debug("start every monthly!!");
        logger.debug("=========================");

        //LocalDate start = LocalDate.of(2016, 5, 1);

        LocalDate tmp = LocalDate.now();

        LocalDate start = LocalDate.now();
        start = start.withDayOfMonth(1);

        LocalDate end = start.plusMonths(1);

        String date1 = start.toString("yyyy-MM-dd");
        String date2 = end.toString("yyyy-MM-dd");

        logger.debug("monthly :: start date : {} - end date : {}", date1, date2);
        esService.generateAllPV(enumRCType.monthly, date1, date2);
        esUVService.generateAllUV(enumRCType.monthly, date1, date2);

    }

    //전전달 한달치 index를 삭제한다
    @Scheduled(cron = "0 30 0 1 * *")
    public void deleteAMonthAgoIndices() throws IOException {

        LocalDate date = LocalDate.now();

        //매달 첫날에 돌지 않은 경우 대비
        date = date.withDayOfMonth(1);
        logger.debug("date : {}", date);

        //전전달 데이터 삭제
        LocalDate start = date.minusMonths(2);//전전달 1일 부터
        LocalDate end = date.minusMonths(1).minusDays(1); //전전달 마지막날 까지 30일 또는 31일

        logger.debug("start : {} ~~ end : {}", start, end);

        LocalDate firstWeekOfLastMonth = date.minusMonths(1);

        int weekYear = start.getWeekyear();

        //4주전 데이터 4주치 삭제
        int startWeek = start.getWeekOfWeekyear();
        int endWeek = end.getWeekOfWeekyear();
        int checkWeek = firstWeekOfLastMonth.getWeekOfWeekyear();

        logger.debug("startWeek : {} ~~ endWeek : {} ~ checkWeek : {}", startWeek, endWeek, checkWeek);


        if ( checkWeek == endWeek ){
            endWeek--;
        }

        String indexName = "";

        //4주치 데이터 삭제
        for(int i = startWeek ; i <= endWeek ; i++){

            indexName = "filebeat-"+weekYear+"-w"+ i;

            logger.debug("@_@ delete index : {}", indexName);

            commonService.deleteIndex(indexName);
        }


    }
}
