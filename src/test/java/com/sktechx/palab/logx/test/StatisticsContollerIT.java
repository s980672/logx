package com.sktechx.palab.logx.test;

import com.google.common.collect.Lists;
import com.jayway.restassured.RestAssured;
import com.sktechx.palab.logx.config.Application;
import com.sktechx.palab.logx.model.*;
import com.sktechx.palab.logx.repository.ServiceRCRepository;
import com.sktechx.palab.logx.repository.SvcOption1RCRepository;
import com.sktechx.palab.logx.web.StatisticsController;
import org.apache.http.HttpStatus;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static org.hamcrest.Matchers.is;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:9101")
public class StatisticsContollerIT {

	Logger logger = LoggerFactory.getLogger(StatisticsContollerIT.class);


	@Value("${server.port}")
	int port;

    @Autowired
    ServiceRCRepository svcRcRepo;

    @Autowired
    SvcOption1RCRepository svcAppRcRepo;


    @Autowired
    StatisticsController statisticsController;

	@Before
	public void setUp() throws UnsupportedEncodingException {

		RestAssured.port=port;


        ///////////////////////////////////////////////////////////
        //Test data for service request call

        List<ServiceRequestCall> rcs = Lists.newArrayList();

        LocalDate date = LocalDate.parse("20160706", DateTimeFormat.forPattern("yyyyMMdd"));

        ServiceRequestCall rc = new ServiceRequestCall(enumStatsType.PV,enumRCType.daily, date.toDate(),"11" , "10004", 100046l);
        rcs.add(rc);
        rcs.add(new ServiceRequestCall(enumStatsType.PV, enumRCType.daily, date.toDate(), "11" ,"70004", 700046l));

        date = date.plusDays(1);

        rcs.add(new ServiceRequestCall(enumStatsType.PV,enumRCType.daily, date.toDate(), "11" ,"10004", 100047l));
        rcs.add(new ServiceRequestCall(enumStatsType.PV,enumRCType.daily, date.toDate(),"1", "70004", 700047l));

        svcRcRepo.save(rcs);

        ///////////////////////////////////////////////////////////
        //Test data for service app request call


        date = date.minusDays(1);

        List<SvcOption1RC> svcAppRcs = Lists.newArrayList();

        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV,enumRCType.daily, enumOptionType.APP, date.toDate(),"1", "10004", "400001020",1l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV,enumRCType.daily, enumOptionType.APP,date.toDate(), "1","10004", "400001540",2l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV,enumRCType.daily, enumOptionType.APP,date.toDate(), "1","10004", "400002323",3l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV,enumRCType.daily, enumOptionType.APP,date.toDate(), "1","10004", "400023201",4l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV,enumRCType.daily, enumOptionType.APP,date.toDate(), "1","10004", "400102021",5l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV,enumRCType.daily, enumOptionType.APP,date.toDate(), "1","10004", "400102030", 6l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV,enumRCType.daily, enumOptionType.APP,date.toDate(), "1","10020", "400001020",201l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV,enumRCType.daily, enumOptionType.APP,date.toDate(), "1","10020", "400001540",202l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV,enumRCType.daily, enumOptionType.APP,date.toDate(), "1","10020", "400002323",203l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV,enumRCType.daily, enumOptionType.APP,date.toDate(), "1","10020", "400023201",204l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV,enumRCType.daily, enumOptionType.APP,date.toDate(), "1","10020", "400102021", 205l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV,enumRCType.daily, enumOptionType.APP,date.toDate(), "1","10020", "400102030", 206l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV,enumRCType.daily, enumOptionType.APP,date.toDate(), "1","10073", "400001020",731l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV,enumRCType.daily, enumOptionType.APP,date.toDate(), "1","10073", "400001540",732l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV,enumRCType.daily, enumOptionType.APP,date.toDate(), "1","10073", "400002323",733l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV,enumRCType.daily, enumOptionType.APP,date.toDate(), "1","10073", "400023201",734l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV,enumRCType.daily, enumOptionType.APP,date.toDate(), "1","10073", "400102021",735l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV,enumRCType.daily, enumOptionType.APP,date.toDate(), "1","10073", "400102030",736l));

        date = date.plusDays(1);

        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV,enumRCType.daily, enumOptionType.APP, date.toDate(),"1", "10004", "400001020",11l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV,enumRCType.daily, enumOptionType.APP, date.toDate(),"1", "10004", "400001540",12l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV,enumRCType.daily, enumOptionType.APP, date.toDate(),"1", "10004", "400002323",13l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV,enumRCType.daily, enumOptionType.APP, date.toDate(),"1", "10004", "400023201",14l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV,enumRCType.daily, enumOptionType.APP, date.toDate(),"1", "10004", "400102021",15l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV,enumRCType.daily, enumOptionType.APP, date.toDate(),"1", "10004", "400102030", 16l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV,enumRCType.daily, enumOptionType.APP, date.toDate(),"1", "10020", "400001020",1201l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV,enumRCType.daily, enumOptionType.APP, date.toDate(),"1", "10020", "400001540",1202l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV,enumRCType.daily, enumOptionType.APP, date.toDate(),"1", "10020", "400002323",1203l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV,enumRCType.daily, enumOptionType.APP, date.toDate(),"1", "10020", "400023201",1204l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV,enumRCType.daily, enumOptionType.APP, date.toDate(),"1", "10020", "400102021", 1205l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV,enumRCType.daily, enumOptionType.APP, date.toDate(),"1", "10020", "400102030", 1206l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV,enumRCType.daily, enumOptionType.APP, date.toDate(),"1", "10073", "400001020",1731l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV,enumRCType.daily, enumOptionType.APP, date.toDate(),"1", "10073", "400001540",1732l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV,enumRCType.daily, enumOptionType.APP, date.toDate(),"1", "10073", "400002323",1733l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV,enumRCType.daily, enumOptionType.APP, date.toDate(),"1", "10073", "400023201",1734l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV,enumRCType.daily, enumOptionType.APP, date.toDate(),"1", "10073", "400102021",1735l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV,enumRCType.daily, enumOptionType.APP,date.toDate(), "1","10073", "400102030",1736l));


        svcAppRcRepo.save(svcAppRcs);


	}

	@Test
	public void healthEndpoint_isAvailableToEveryone() {
		RestAssured.given().
				header("Content-Type", "application/json").
				when().get("/health").
				then().statusCode(HttpStatus.SC_OK).body("status", is("UP"));
	}


	@Test
	public void canDownloadExcelFile() {

		RestAssured.given().
				//header("Content-Type", "application/json").
                param("startDate", "20160706").
                param("endDate", "20160707").
                param("option1","app").
                param("service","").
                when().
				get("/stats/{period}/pv","daily").
                then().statusCode(HttpStatus.SC_OK);

	}

}
