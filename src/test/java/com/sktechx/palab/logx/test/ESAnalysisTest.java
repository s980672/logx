package com.sktechx.palab.logx.test;

import com.sktechx.palab.logx.service.AggReqDSLs;
import com.sktechx.palab.logx.service.ElasticsearchAnalysisService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by 1002382 on 2016. 7. 6..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class ESAnalysisTest extends AbstractJUnit4SpringMvcTests{
    Logger logger = LoggerFactory.getLogger(ESAnalysisTest.class);

    @Autowired
    ElasticsearchAnalysisService esService;

    @Before
    public void init(){

    }

    @Test
    public void getServicePV(){
        esService.getResult(AggReqDSLs.getQueryPV("1h"));
    }




}
