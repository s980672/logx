package com.sktechx.palab.logx.test;

import com.sktechx.palab.logx.config.AbstractJUnit4SpringMvcTests;
import com.sktechx.palab.logx.config.Application;
import com.sktechx.palab.logx.service.ElasticsearchCommonAnalysisService;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

/**
 * Created by 1002382 on 2016. 7. 6..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class DeleteIndexTest extends AbstractJUnit4SpringMvcTests {
    Logger logger = LoggerFactory.getLogger(DeleteIndexTest.class);

    @Autowired
    ElasticsearchCommonAnalysisService commonService;

    @Test
    public void deleteAMonthAgoIndex() throws IOException {
        LocalDate date = LocalDate.now();

        date = date.withMonthOfYear(8);

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
