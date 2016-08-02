package com.sktechx.palab.logx.test;

import com.sktechx.palab.logx.config.AbstractJUnit4SpringMvcTests;
import com.sktechx.palab.logx.config.Application;
import com.sktechx.palab.logx.model.enumOptionType;
import com.sktechx.palab.logx.model.enumRCType;
import com.sktechx.palab.logx.repository.SvcOption1RCRepository;
import com.sktechx.palab.logx.repository.SvcOption2RCRepository;
import com.sktechx.palab.logx.service.ElasticsearchCommonAnalysisService;
import com.sktechx.palab.logx.service.ElasticsearchPVAnalysisService;
import com.sktechx.palab.logx.service.ElasticsearchUVAnalysisService;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by 1002382 on 2016. 7. 6..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class ESAnalysisBatchTest extends AbstractJUnit4SpringMvcTests {
    Logger logger = LoggerFactory.getLogger(ESAnalysisBatchTest.class);

    @Autowired
    ElasticsearchPVAnalysisService esService;

    @Autowired
    ElasticsearchUVAnalysisService esUvService;

    @Autowired
    ElasticsearchCommonAnalysisService commonesService;


    @Autowired
    SvcOption1RCRepository svcOption1RCRepo;

    @Autowired
    SvcOption2RCRepository svcOption2RCRepo;


    @Before
    public void init(){

    }


    @Test
    public void getOption1UV() throws IOException, ParseException {
        //////////////////////////////////
        //특정일에만 데이터가 있어서 테스트 날짜
        LocalDate startD = LocalDate.parse("2016-05-01", DateTimeFormat.forPattern("yyyy-MM-dd"));
        String start = startD.toString(); //"2016-07-18";
        String end = startD.plusDays(1).toString();

        esUvService.generateSvcOption1UV(enumOptionType.API, enumRCType.daily, start, end);

        logger.debug("getOption1UV==============================");


        svcOption1RCRepo.findAll().stream().forEach(rc-> {
            logger.debug(rc.toString());
        });

    }

    @Test
    public void getOption2PV() throws IOException, ParseException {

        //////////////////////////////////
        //특정일에만 데이터가 있어서 테스트 날짜
        LocalDate startD = LocalDate.parse("2016-05-01", DateTimeFormat.forPattern("yyyy-MM-dd"));
        String start = startD.toString(); //"2016-07-18";
        String end = startD.plusDays(1).toString();

        logger.debug("start : {}, end : {}", start, end);


        esService.generateSvcOption2PV(enumRCType.monthly, enumOptionType.API_APP, start, end);
        esService.generateSvcOption2PV(enumRCType.monthly, enumOptionType.APP_API, start, end);
        esService.generateSvcOption2PV(enumRCType.monthly, enumOptionType.ERROR_APP, start, end);
        esService.generateSvcOption2PV(enumRCType.monthly, enumOptionType.ERROR_API, start, end);

        logger.debug("svcOption2==============================");
        svcOption2RCRepo.findAll().stream().forEach(err -> logger.debug(err.toString()));


    }

    @Test
    public void getOption1PV() throws IOException, ParseException {
        //////////////////////////////////
        //특정일에만 데이터가 있어서 테스트 날짜
        LocalDate startD = LocalDate.parse("2016-07-17", DateTimeFormat.forPattern("yyyy-MM-dd"));
        String start = startD.toString(); //"2016-07-18";
        String end = startD.plusDays(1).toString();

        logger.debug("start : {}, end : {}", start, end);

        esService.generateSvcOption1PV(enumRCType.daily, enumOptionType.APP, start, end);
        esService.generateSvcOption1PV(enumRCType.daily, enumOptionType.API, start, end);
        esService.generateSvcOption1PV(enumRCType.daily, enumOptionType.ERROR, start, end);

        logger.debug("svcOption1==============================");

        svcOption1RCRepo.findAll().stream().forEach(pv -> logger.debug(pv.toString()));

    }

    @Test
    public void getServicePV() throws IOException, ParseException {


        //////////////////////////////////
        //특정일에만 데이터가 있어서 테스트 날짜
        LocalDate startD = LocalDate.parse("2016-07-17", DateTimeFormat.forPattern("yyyy-MM-dd"));
        String start = startD.toString(); //"2016-07-18";
        String end = startD.plusDays(1).toString();

        logger.debug("start : {}, end : {}", start, end);

        try {
            esService.generatePV(enumRCType.daily, start, end);

            esService.generateSvcPV(enumRCType.daily, start, end);

        } catch (IOException e) {

            logger.error(e.getLocalizedMessage());

            e.printStackTrace();
        }

    }


}
