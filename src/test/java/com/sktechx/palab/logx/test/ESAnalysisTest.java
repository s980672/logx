package com.sktechx.palab.logx.test;

import com.sktechx.palab.logx.config.AbstractJUnit4SpringMvcTests;
import com.sktechx.palab.logx.config.Application;
import com.sktechx.palab.logx.repository.SvcAppRCRepository;
import com.sktechx.palab.logx.service.ElasticsearchAnalysisService;
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
public class ESAnalysisTest extends AbstractJUnit4SpringMvcTests {
    Logger logger = LoggerFactory.getLogger(ESAnalysisTest.class);

    @Autowired
    ElasticsearchAnalysisService esService;


    @Autowired
    SvcAppRCRepository svcAppRCRepo;


    @Before
    public void init(){

    }



    @Test
    public void getServicePV() throws IOException, ParseException {


        //////////////////////////////////
        //특정일에만 데이터가 있어서 테스트 날짜
        String start = "2016-07-14";
        String end = "2016-07-15";


        try {
            esService.generatePV(start, end);

            esService.generateSVCPV(start, end);

            esService.generateSvcAppPV(start, end);


            logger.debug("==============================");
            svcAppRCRepo.findAll().stream().forEach(pv -> logger.debug(pv.toString()));
            logger.debug("==============================");

        } catch (IOException e) {

            logger.error(e.getLocalizedMessage());

            e.printStackTrace();
        }

    }


}
