package com.sktechx.palab.logx.test;

import com.google.common.collect.Lists;
import com.sktechx.palab.logx.config.AbstractJUnit4SpringMvcTests;
import com.sktechx.palab.logx.config.Application;
import com.sktechx.palab.logx.model.*;
import com.sktechx.palab.logx.repository.*;
import com.sktechx.palab.logx.service.ElasticsearchPVAnalysisService;
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

import java.util.List;

/**
 * Created by 1002382 on 2016. 7. 6..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class StatsDataTest extends AbstractJUnit4SpringMvcTests {
    Logger logger = LoggerFactory.getLogger(StatsDataTest.class);


    @Autowired
    RequestCallRepository rcRepo;

    @Autowired
    ServiceRCRepository  svcRcRepo;

    @Autowired
    SvcViewRepository svcViewRepo;

    @Autowired
    AppViewRepository appRepo;



    @Autowired
    SvcOption1RCRepository svcOption1RcRepo;

    @Autowired
    SvcOption2RCRepository svcOption2RcRepo;



    @Autowired
    ElasticsearchPVAnalysisService esService;

    @Before
    public void init() {

        rcRepo.save(new ReqCall(enumRCType.monthly, LocalDate.parse("20160501", DateTimeFormat.forPattern("yyyyMMdd")).toDate(), 100005));
        rcRepo.save(new ReqCall(enumRCType.monthly, LocalDate.parse("20160601", DateTimeFormat.forPattern("yyyyMMdd")).toDate(), 100006));
        rcRepo.save(new ReqCall(enumRCType.monthly, LocalDate.parse("20160701", DateTimeFormat.forPattern("yyyyMMdd")).toDate(), 100007));
        rcRepo.save(new ReqCall(enumRCType.monthly, LocalDate.parse("20160801", DateTimeFormat.forPattern("yyyyMMdd")).toDate(), 100008));






        /////////////////////////////////////////////////////////////
        //test table for svc/app view
        appRepo.save(new AppView("1", "test app1", "400001020"));
        appRepo.save(new AppView("2", "weather pong", "400001540"));
        appRepo.save(new AppView("3", "test app2", "400002323"));

        svcViewRepo.save(new SvcView("10004", "weather"));
        svcViewRepo.save(new SvcView("70004", "tmap"));
        svcViewRepo.save(new SvcView("10002", "weather_cate1"));
        svcViewRepo.save(new SvcView("10020", "weather_cate2"));

        ///////////////////////////////////////////////////////////
        //Test data for service request call

        List<ServiceRequestCall> rcs = Lists.newArrayList();

        LocalDate date = LocalDate.parse("20160706", DateTimeFormat.forPattern("yyyyMMdd"));

        ServiceRequestCall rc = new ServiceRequestCall(enumStatsType.PV, enumRCType.daily, date.toDate(), "10004", 100046l);
        rcs.add(rc);
        rcs.add(new ServiceRequestCall(enumStatsType.PV, enumRCType.daily, date.toDate(), "70004", 700046l));

        date = date.plusDays(1);

        rcs.add(new ServiceRequestCall(enumStatsType.PV, enumRCType.daily, date.toDate(), "10004", 100047l));
        rcs.add(new ServiceRequestCall(enumStatsType.PV, enumRCType.daily, date.toDate(), "70004", 700047l));

        svcRcRepo.save(rcs);

        //////////////////////////////////////////////////////////////////////
        // monthly

        rcs.clear();

        date = LocalDate.parse("20160701", DateTimeFormat.forPattern("yyyyMMdd"));

        rcs.add(new ServiceRequestCall(enumStatsType.PV, enumRCType.monthly, date.toDate(), "10004", 100047l));
        rcs.add(new ServiceRequestCall(enumStatsType.PV, enumRCType.monthly, date.toDate(), "70004", 700047l));

        date = date.plusMonths(1); //8월

        rcs.add(new ServiceRequestCall(enumStatsType.PV, enumRCType.monthly, date.toDate(), "10004", 100048l));
        rcs.add(new ServiceRequestCall(enumStatsType.PV, enumRCType.monthly, date.toDate(), "70004", 700048l));

        date = date.plusMonths(1); //9월

        rcs.add(new ServiceRequestCall(enumStatsType.PV, enumRCType.monthly, date.toDate(), "10004", 100049l));
        rcs.add(new ServiceRequestCall(enumStatsType.PV, enumRCType.monthly, date.toDate(), "70004", 700049l));

        svcRcRepo.save(rcs);


        ///////////////////////////////////////////////////////////
        //Test data for service app request call

        date = LocalDate.parse("20160706", DateTimeFormat.forPattern("yyyyMMdd"));
        //date = date.minusDays(1);

        List<SvcOption1RC> svcAppRcs = Lists.newArrayList();
        List<SvcOption2RC> svcOption2 = Lists.newArrayList();


        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP, date.toDate(), "10004", "400001020", 1l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP, date.toDate(), "10004", "400001540", 2l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP, date.toDate(), "10004", "400002323", 3l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP, date.toDate(), "10004", "400023201", 4l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP, date.toDate(), "10004", "400102021", 5l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP, date.toDate(), "10004", "400102030", 6l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP, date.toDate(), "10020", "400001020", 201l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP, date.toDate(), "10020", "400001540", 202l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP, date.toDate(), "10020", "400002323", 203l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP, date.toDate(), "10020", "400023201", 204l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP, date.toDate(), "10020", "400102021", 205l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP, date.toDate(), "10020", "400102030", 206l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP, date.toDate(), "10073", "400001020", 731l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP, date.toDate(), "10073", "400001540", 732l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP, date.toDate(), "10073", "400002323", 733l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP, date.toDate(), "10073", "400023201", 734l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP, date.toDate(), "10073", "400102021", 735l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP, date.toDate(), "10073", "400102030", 736l));
        /////////////////////////////////
        //API
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.API, date.toDate(), "10004", "400001020", 1l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.API, date.toDate(), "10004", "400001540", 2l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.API, date.toDate(), "10004", "400002323", 3l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.API, date.toDate(), "10004", "400023201", 4l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.API, date.toDate(), "10004", "400102021", 5l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.API, date.toDate(), "10004", "400102030", 6l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.API, date.toDate(), "10020", "400001020", 201l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.API, date.toDate(), "10020", "400001540", 202l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.API, date.toDate(), "10020", "400002323", 203l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.API, date.toDate(), "10020", "400023201", 204l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.API, date.toDate(), "10020", "400102021", 205l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.API, date.toDate(), "10020", "400102030", 206l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.API, date.toDate(), "10073", "400001020", 731l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.API, date.toDate(), "10073", "400001540", 732l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.API, date.toDate(), "10073", "400002323", 733l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.API, date.toDate(), "10073", "400023201", 734l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.API, date.toDate(), "10073", "400102021", 735l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.API, date.toDate(), "10073", "400102030", 736l));


        //ERROR
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.ERROR, date.toDate(), "10004", "500", 11l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.ERROR, date.toDate(), "10004", "401", 12l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.ERROR, date.toDate(), "10004", "402", 13l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.ERROR, date.toDate(), "10004", "403", 14l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.ERROR, date.toDate(), "10004", "404", 15l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.ERROR, date.toDate(), "10004", "406", 16l));


        //APP_API
        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP_API, date.toDate(), "100004", "400102030", "/weather/dust", 10));
        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP_API, date.toDate(), "100004", "400102030", "/weather/clean", 20));
        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP_API, date.toDate(), "100004", "400102030", "/weather/rainy", 30));
        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP_API, date.toDate(), "100004", "400102030", "/weather/windy", 40));

        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP_API, date.toDate(), "700004", "400102030", "/weather/dust", 10));
        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP_API, date.toDate(), "700004", "400102030", "/weather/clean", 20));
        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP_API, date.toDate(), "700004", "400102030", "/weather/rainy", 30));
        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP_API, date.toDate(), "700004", "400102030", "/weather/windy", 40));

        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP_API, date.toDate(), "ALL", "400102030", "/weather/dust", 20));
        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP_API, date.toDate(), "ALL", "400102030", "/weather/clean", 40));
        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP_API, date.toDate(), "ALL", "400102030", "/weather/rainy", 60));
        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP_API, date.toDate(), "ALL", "400102030", "/weather/windy", 80));

        //ERROR_API///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.ERROR_API, date.toDate(), "100004", "400", "/weather/dust", 1));
        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.ERROR_API, date.toDate(), "100004", "400", "/weather/windy", 1));
        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.ERROR_API, date.toDate(), "100004", "500", "/weather/dust", 2));


        date = date.plusDays(1);

        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP, date.toDate(), "10004", "400001020", 11l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP, date.toDate(), "10004", "400001540", 12l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP, date.toDate(), "10004", "400002323", 13l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP, date.toDate(), "10004", "400023201", 14l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP, date.toDate(), "10004", "400102021", 15l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP, date.toDate(), "10004", "400102030", 16l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP, date.toDate(), "10020", "400001020", 1201l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP, date.toDate(), "10020", "400001540", 1202l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP, date.toDate(), "10020", "400002323", 1203l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP, date.toDate(), "10020", "400023201", 1204l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP, date.toDate(), "10020", "400102021", 1205l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP, date.toDate(), "10020", "400102030", 1206l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP, date.toDate(), "10073", "400001020", 1731l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP, date.toDate(), "10073", "400001540", 1732l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP, date.toDate(), "10073", "400002323", 1733l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP, date.toDate(), "10073", "400023201", 1734l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP, date.toDate(), "10073", "400102021", 1735l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP, date.toDate(), "10073", "400102030", 1736l));


        //////////////////////////////////
        //API
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.API, date.toDate(), "10004", "400001020", 11l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.API, date.toDate(), "10004", "400001540", 12l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.API, date.toDate(), "10004", "400002323", 13l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.API, date.toDate(), "10004", "400023201", 14l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.API, date.toDate(), "10004", "400102021", 15l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.API, date.toDate(), "10004", "400102030", 16l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.API, date.toDate(), "10020", "400001020", 1201l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.API, date.toDate(), "10020", "400001540", 1202l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.API, date.toDate(), "10020", "400002323", 1203l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.API, date.toDate(), "10020", "400023201", 1204l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.API, date.toDate(), "10020", "400102021", 1205l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.API, date.toDate(), "10020", "400102030", 1206l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.API, date.toDate(), "10073", "400001020", 1731l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.API, date.toDate(), "10073", "400001540", 1732l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.API, date.toDate(), "10073", "400002323", 1733l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.API, date.toDate(), "10073", "400023201", 1734l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.API, date.toDate(), "10073", "400102021", 1735l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.API, date.toDate(), "10073", "400102030", 1736l));


        //ERROR
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.ERROR, date.toDate(), "10004", "500", 11l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.ERROR, date.toDate(), "10004", "401", 12l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.ERROR, date.toDate(), "10004", "402", 13l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.ERROR, date.toDate(), "10004", "403", 14l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.ERROR, date.toDate(), "10004", "404", 15l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.ERROR, date.toDate(), "10004", "406", 16l));

        //ERROR
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.ERROR, date.toDate(), "70004", "500", 711l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.ERROR, date.toDate(), "70004", "401", 712l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.ERROR, date.toDate(), "70004", "402", 713l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.ERROR, date.toDate(), "70004", "403", 714l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.ERROR, date.toDate(), "70004", "404", 715l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.daily, enumOptionType.ERROR, date.toDate(), "70004", "406", 716l));


        //APP_API//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP_API, date.toDate(), "100004", "400102030", "/weather/dust", 1));
        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP_API, date.toDate(), "100004", "400102030", "/weather/clean", 2));
        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP_API, date.toDate(), "100004", "400102030", "/weather/rainy", 3));
        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP_API, date.toDate(), "100004", "400102030", "/weather/windy", 4));

        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP_API, date.toDate(), "700004", "400102030", "/weather/dust", 1));
        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP_API, date.toDate(), "700004", "400102030", "/weather/clean", 2));
        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP_API, date.toDate(), "700004", "400102030", "/weather/rainy", 3));
        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP_API, date.toDate(), "700004", "400102030", "/weather/windy", 4));

        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP_API, date.toDate(), "ALL", "400102030", "/weather/dust", 2));
        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP_API, date.toDate(), "ALL", "400102030", "/weather/clean", 4));
        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP_API, date.toDate(), "ALL", "400102030", "/weather/rainy", 6));
        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.APP_API, date.toDate(), "ALL", "400102030", "/weather/windy", 8));


        //API_APP///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.API_APP, date.toDate(), "100004", "/weather/dust", "5d1577fc692843aa6f49c0cf49b1ee15", 1));
        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.API_APP, date.toDate(), "100004", "/weather/dust", "692843aa692843aa6f49c0cf49b1ee15", 11));
        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.API_APP, date.toDate(), "100004", "/weather/clean", "5d1577fc692843aa6f49c0cf49b1ee15", 2));
        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.API_APP, date.toDate(), "100004", "/weather/rainy", "5d1577fc692843aa6f49c0cf49b1ee15", 3));
        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.API_APP, date.toDate(), "100004", "/weather/windy", "5d1577fc692843aa6f49c0cf49b1ee15", 4));

        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.API_APP, date.toDate(), "700004", "/tmap/route", "5d1577fc692843aa6f49c0cf49b1ee15", 1));
        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.API_APP, date.toDate(), "700004", "/tmap/traffic", "5d1577fc692843aa6f49c0cf49b1ee15", 2));
        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.API_APP, date.toDate(), "700004", "/tmap/traffic", "692843aa692843aa6f49c0cf49b1ee15", 22));
        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.API_APP, date.toDate(), "700004", "/tmap/poi", "5d1577fc692843aa6f49c0cf49b1ee15", 3));
        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.API_APP, date.toDate(), "700004", "/tmp/landmark", "5d1577fc692843aa6f49c0cf49b1ee15", 4));


        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.API_APP, date.toDate(), "800004", "/tmap2/route", "5d1577fc692843aa6f49c0cf49b1ee15", 1));
        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.API_APP, date.toDate(), "800004", "/tmap2/traffic", "5d1577fc692843aa6f49c0cf49b1ee15", 2));
        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.API_APP, date.toDate(), "800004", "/tmap2/poi", "5d1577fc692843aa6f49c0cf49b1ee15", 3));
        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.API_APP, date.toDate(), "800004", "/tmp2/landmark", "5d1577fc692843aa6f49c0cf49b1ee15", 4));


        //ERROR_API///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.ERROR_API, date.toDate(), "100004", "400", "/weather/dust", 1));
        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.ERROR_API, date.toDate(), "100004", "400", "/weather/windy", 1));
        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.ERROR_API, date.toDate(), "100004", "500", "/weather/dust", 2));
        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.ERROR_API, date.toDate(), "100004", "500", "/weather/sunny", 2));
        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.ERROR_API, date.toDate(), "100004", "401", "/weather/dust", 3));
        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.ERROR_API, date.toDate(), "100004", "401", "/weather/clean", 3));
        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.ERROR_API, date.toDate(), "100004", "404", "/weather/rainy", 4));


        //ERROR_APP///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.ERROR_APP, date.toDate(), "100004", "400", "5d1577fc692843aa6f49c0cf49b1ee15", 1));
        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.ERROR_APP, date.toDate(), "100004", "400", "692843aa692843aa6f49c0cf49b1ee15y", 1));
        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.ERROR_APP, date.toDate(), "100004", "500", "5d1577fc692843aa6f49c0cf49b1ee15", 2));
        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.ERROR_APP, date.toDate(), "100004", "500", "692843aa692843aa6f49c0cf49b1ee15", 2));
        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.ERROR_APP, date.toDate(), "100004", "401", "5d1577fc692843aa6f49c0cf49b1ee15", 3));
        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.ERROR_APP, date.toDate(), "100004", "401", "692843aa692843aa6f49c0cf49b1ee15", 3));
        svcOption2.add(new SvcOption2RC(enumStatsType.PV, enumRCType.daily, enumOptionType.ERROR_APP, date.toDate(), "100004", "404", "5d1577fc692843aa6f49c0cf49b1ee15", 4));


//        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.APP_API, date.toDate(), "ALL", "400102030", "/weather/dust", 2));
//        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.APP_API, date.toDate(), "ALL", "400102030", "/weather/clean", 4));
//        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.APP_API, date.toDate(), "ALL", "400102030", "/weather/rainy", 6));
//        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.APP_API, date.toDate(), "ALL", "400102030", "/weather/windy", 8));


        svcOption1RcRepo.save(svcAppRcs);

        svcOption2RcRepo.save(svcOption2);

        ///////////////////////////////////////////////////////////////////////////////////
        // montly svc app pv

        svcAppRcs.clear();
        date = LocalDate.parse("20160501", DateTimeFormat.forPattern("yyyyMMdd"));

        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.monthly, enumOptionType.APP, date.toDate(), "10004", "400001020", 18l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.monthly, enumOptionType.APP, date.toDate(), "10002", "400001020", 2l));
        date = date.plusMonths(1);//8                     enumOption1Type.APP,
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.monthly, enumOptionType.APP, date.toDate(), "10004", "700001020", 19l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.monthly, enumOptionType.APP, date.toDate(), "10002", "800001020", 2l));
        date = date.plusMonths(1);//9                     enumOption1Type.APP,
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.monthly, enumOptionType.APP, date.toDate(), "10004", "400001020", 20l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.monthly, enumOptionType.APP, date.toDate(), "10002", "500001020", 2l));
        date = date.plusMonths(1);//10                    enumOption1Type.APP,
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.monthly, enumOptionType.APP, date.toDate(), "10004", "400001020", 21l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.monthly, enumOptionType.APP, date.toDate(), "10002", "300001020", 2l));
        date = date.plusMonths(1);//11                    enumOption1Type.APP,
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.monthly, enumOptionType.APP, date.toDate(), "10004", "400001020", 22l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.monthly, enumOptionType.APP, date.toDate(), "10002", "400001020", 2l));
        date = date.plusMonths(1);//12                    enumOption1Type.APP,
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.monthly, enumOptionType.APP, date.toDate(), "10004", "400001020", 23l));
        svcAppRcs.add(new SvcOption1RC(enumStatsType.PV, enumRCType.monthly, enumOptionType.APP, date.toDate(), "10002", "400001020", 2l));


        svcOption1RcRepo.save(svcAppRcs);


    }


    @Test
    public void getPV() {

        LocalDate date = LocalDate.parse("20160501", DateTimeFormat.forPattern("yyyyMMdd"));
        LocalDate date2 = date.plusMonths(4);
        List<ReqCall> pv = esService.getPV(enumRCType.monthly, date.toDate(), date2.toDate());
        logger.debug("{}",pv);

    }

    @Test
    public void getSvcPV(){

        LocalDate date = LocalDate.parse("20160501", DateTimeFormat.forPattern("yyyyMMdd"));
        LocalDate date2 = date.plusMonths(4);

        List<ServiceRequestCall> svcPVTop5 = esService.getSvcPVTop5(enumRCType.monthly, date.toDate(), date2.toDate());
        logger.debug("{}", svcPVTop5);
    }

    @Test
    public void getRCPerAPIOrAppTop10() {

        LocalDate date = LocalDate.parse("20160501", DateTimeFormat.forPattern("yyyyMMdd"));
        LocalDate date2 = date.plusMonths(4);

        List<SvcOption1RC> rcPerApiTop10 = esService.getRCPerApiTop10(enumRCType.monthly, date.toDate(), date2.toDate());
        logger.debug("{}", rcPerApiTop10);

        logger.debug("========================================");
        rcPerApiTop10 = esService.getRCPerAppTop10(enumRCType.monthly, date.toDate(), date2.toDate());
        logger.debug("{}", rcPerApiTop10);
    }

}
