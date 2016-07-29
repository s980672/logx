package com.sktechx.palab.logx.test;

import com.google.common.collect.Lists;
import com.sktechx.palab.logx.config.AbstractJUnit4SpringMvcTests;
import com.sktechx.palab.logx.config.Application;
import com.sktechx.palab.logx.model.*;
import com.sktechx.palab.logx.repository.ServiceRCRepository;
import com.sktechx.palab.logx.repository.SvcOption1RCRepository;
import com.sktechx.palab.logx.repository.SvcOption2RCRepository;
import com.sktechx.palab.logx.repository.SvcRepository;
import com.sktechx.palab.logx.service.StatisticsExcelExportService;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1002382 on 2016. 7. 6..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class ExcelExportTest extends AbstractJUnit4SpringMvcTests {
    Logger logger = LoggerFactory.getLogger(ExcelExportTest.class);

    @Autowired
    StatisticsExcelExportService excelExportService;


    @Autowired
    ServiceRCRepository  svcRcRepo;

    @Autowired
    SvcRepository svcRepo;



    @Autowired
    SvcOption1RCRepository svcOption1RcRepo;

    @Autowired
    SvcOption2RCRepository svcOption2RcRepo;

    static XSSFWorkbook wb;



    @Before
    public void init(){
        //create a workbook

        wb = new XSSFWorkbook();



        /////////////////////////////////////////////////////////////
        // service table
        ArrayList<Service> services = Lists.newArrayList(new Service("10004", "weather"), new Service("70004", "tmap"));
        svcRepo.save(services);

        ///////////////////////////////////////////////////////////
        //Test data for service request call

        List<ServiceRequestCall> rcs = Lists.newArrayList();

        LocalDate date = LocalDate.parse("20160706", DateTimeFormat.forPattern("yyyyMMdd"));

        ServiceRequestCall rc = new ServiceRequestCall(enumRCType.daily, date.toDate(), "10004", 100046l);
        rcs.add(rc);
        rcs.add(new ServiceRequestCall(enumRCType.daily, date.toDate(), "70004", 700046l));

        date = date.plusDays(1);

        rcs.add(new ServiceRequestCall(enumRCType.daily, date.toDate(), "10004", 100047l));
        rcs.add(new ServiceRequestCall(enumRCType.daily, date.toDate(), "70004", 700047l));

        svcRcRepo.save(rcs);

        //////////////////////////////////////////////////////////////////////
        // monthly

        rcs.clear();

        date = LocalDate.parse("201607", DateTimeFormat.forPattern("yyyyMM"));

        rcs.add(new ServiceRequestCall(enumRCType.monthly, date.toDate(), "10004", 100047l));
        rcs.add(new ServiceRequestCall(enumRCType.monthly, date.toDate(), "70004", 700047l));

        date = date.plusMonths(1); //8월

        rcs.add(new ServiceRequestCall(enumRCType.monthly, date.toDate(), "10004", 100048l));
        rcs.add(new ServiceRequestCall(enumRCType.monthly, date.toDate(), "70004", 700048l));

        date = date.plusMonths(1); //9월

        rcs.add(new ServiceRequestCall(enumRCType.monthly, date.toDate(), "10004", 100049l));
        rcs.add(new ServiceRequestCall(enumRCType.monthly, date.toDate(), "70004", 700049l));

        svcRcRepo.save(rcs);


        ///////////////////////////////////////////////////////////
        //Test data for service app request call

        date = LocalDate.parse("20160706", DateTimeFormat.forPattern("yyyyMMdd"));
        //date = date.minusDays(1);

        List<SvcOption1RC> svcAppRcs = Lists.newArrayList();
        List<SvcOption2RC> svcOption2 = Lists.newArrayList();


        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.APP, date.toDate(), "10004", "400001020",1l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.APP, date.toDate(), "10004", "400001540",2l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.APP, date.toDate(), "10004", "400002323",3l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.APP, date.toDate(), "10004", "400023201",4l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.APP, date.toDate(), "10004", "400102021",5l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.APP, date.toDate(), "10004", "400102030", 6l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.APP, date.toDate(), "10020", "400001020",201l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.APP, date.toDate(), "10020", "400001540",202l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.APP, date.toDate(), "10020", "400002323",203l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.APP, date.toDate(), "10020", "400023201",204l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.APP, date.toDate(), "10020", "400102021", 205l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.APP, date.toDate(), "10020", "400102030", 206l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.APP, date.toDate(), "10073", "400001020",731l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.APP, date.toDate(), "10073", "400001540",732l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.APP, date.toDate(), "10073", "400002323",733l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.APP, date.toDate(), "10073", "400023201",734l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.APP, date.toDate(), "10073", "400102021",735l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.APP, date.toDate(), "10073", "400102030",736l));
        /////////////////////////////////
        //API
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.API, date.toDate(), "10004", "400001020",1l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.API, date.toDate(), "10004", "400001540",2l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.API, date.toDate(), "10004", "400002323",3l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.API, date.toDate(), "10004", "400023201",4l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.API, date.toDate(), "10004", "400102021",5l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.API, date.toDate(), "10004", "400102030", 6l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.API, date.toDate(), "10020", "400001020",201l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.API, date.toDate(), "10020", "400001540",202l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.API, date.toDate(), "10020", "400002323",203l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.API, date.toDate(), "10020", "400023201",204l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.API, date.toDate(), "10020", "400102021", 205l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.API, date.toDate(), "10020", "400102030", 206l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.API, date.toDate(), "10073", "400001020",731l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.API, date.toDate(), "10073", "400001540",732l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.API, date.toDate(), "10073", "400002323",733l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.API, date.toDate(), "10073", "400023201",734l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.API, date.toDate(), "10073", "400102021",735l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.API, date.toDate(), "10073", "400102030",736l));


        //ERROR
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.ERROR, date.toDate(), "10004", "500",11l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.ERROR, date.toDate(), "10004", "401",12l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.ERROR, date.toDate(), "10004", "402",13l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.ERROR, date.toDate(), "10004", "403",14l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.ERROR, date.toDate(), "10004", "404",15l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.ERROR, date.toDate(), "10004", "406", 16l));


        //APP_API
        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.APP_API, date.toDate(), "100004", "400102030", "/weather/dust", 10));
        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.APP_API, date.toDate(), "100004", "400102030", "/weather/clean", 20));
        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.APP_API, date.toDate(), "100004", "400102030", "/weather/rainy", 30));
        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.APP_API, date.toDate(), "100004", "400102030", "/weather/windy", 40));

        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.APP_API, date.toDate(), "700004", "400102030", "/weather/dust", 10));
        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.APP_API, date.toDate(), "700004", "400102030", "/weather/clean", 20));
        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.APP_API, date.toDate(), "700004", "400102030", "/weather/rainy", 30));
        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.APP_API, date.toDate(), "700004", "400102030", "/weather/windy", 40));

        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.APP_API, date.toDate(), "ALL", "400102030", "/weather/dust", 20));
        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.APP_API, date.toDate(), "ALL", "400102030", "/weather/clean", 40));
        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.APP_API, date.toDate(), "ALL", "400102030", "/weather/rainy", 60));
        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.APP_API, date.toDate(), "ALL", "400102030", "/weather/windy", 80));

        //ERROR_API///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.ERROR_API, date.toDate(), "100004",  "400","/weather/dust",1));
        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.ERROR_API, date.toDate(), "100004",  "400","/weather/windy",1));
        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.ERROR_API, date.toDate(), "100004",  "500","/weather/dust",2));






        date = date.plusDays(1);

        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.APP, date.toDate(), "10004", "400001020",11l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.APP, date.toDate(), "10004", "400001540",12l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.APP, date.toDate(), "10004", "400002323",13l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.APP, date.toDate(), "10004", "400023201",14l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.APP, date.toDate(), "10004", "400102021",15l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.APP, date.toDate(), "10004", "400102030", 16l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.APP, date.toDate(), "10020", "400001020",1201l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.APP, date.toDate(), "10020", "400001540",1202l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.APP, date.toDate(), "10020", "400002323",1203l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.APP, date.toDate(), "10020", "400023201",1204l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.APP, date.toDate(), "10020", "400102021", 1205l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.APP, date.toDate(), "10020", "400102030", 1206l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.APP, date.toDate(), "10073", "400001020",1731l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.APP, date.toDate(), "10073", "400001540",1732l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.APP, date.toDate(), "10073", "400002323",1733l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.APP, date.toDate(), "10073", "400023201",1734l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.APP, date.toDate(), "10073", "400102021",1735l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.APP, date.toDate(), "10073", "400102030",1736l));


        //////////////////////////////////
        //API
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.API, date.toDate(), "10004", "400001020",11l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.API, date.toDate(), "10004", "400001540",12l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.API, date.toDate(), "10004", "400002323",13l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.API, date.toDate(), "10004", "400023201",14l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.API, date.toDate(), "10004", "400102021",15l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.API, date.toDate(), "10004", "400102030", 16l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.API, date.toDate(), "10020", "400001020",1201l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.API, date.toDate(), "10020", "400001540",1202l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.API, date.toDate(), "10020", "400002323",1203l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.API, date.toDate(), "10020", "400023201",1204l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.API, date.toDate(), "10020", "400102021", 1205l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.API, date.toDate(), "10020", "400102030", 1206l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.API, date.toDate(), "10073", "400001020",1731l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.API, date.toDate(), "10073", "400001540",1732l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.API, date.toDate(), "10073", "400002323",1733l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.API, date.toDate(), "10073", "400023201",1734l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.API, date.toDate(), "10073", "400102021",1735l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.API, date.toDate(), "10073", "400102030",1736l));


        //ERROR
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.ERROR, date.toDate(), "10004", "500",11l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.ERROR, date.toDate(), "10004", "401",12l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.ERROR, date.toDate(), "10004", "402",13l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.ERROR, date.toDate(), "10004", "403",14l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.ERROR, date.toDate(), "10004", "404",15l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.ERROR, date.toDate(), "10004", "406", 16l));

        //APP_API//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.APP_API, date.toDate(), "100004", "400102030", "/weather/dust", 1));
        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.APP_API, date.toDate(), "100004", "400102030", "/weather/clean", 2));
        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.APP_API, date.toDate(), "100004", "400102030", "/weather/rainy", 3));
        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.APP_API, date.toDate(), "100004", "400102030", "/weather/windy", 4));

        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.APP_API, date.toDate(), "700004", "400102030", "/weather/dust", 1));
        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.APP_API, date.toDate(), "700004", "400102030", "/weather/clean", 2));
        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.APP_API, date.toDate(), "700004", "400102030", "/weather/rainy", 3));
        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.APP_API, date.toDate(), "700004", "400102030", "/weather/windy", 4));

        //API_APP///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.API_APP, date.toDate(), "100004",  "/weather/dust" ,"5d1577fc692843aa6f49c0cf49b1ee15",1));
        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.API_APP, date.toDate(), "100004",  "/weather/dust" ,"692843aa692843aa6f49c0cf49b1ee15",11));
        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.API_APP, date.toDate(), "100004",  "/weather/clean","5d1577fc692843aa6f49c0cf49b1ee15",2));
        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.API_APP, date.toDate(), "100004",  "/weather/rainy","5d1577fc692843aa6f49c0cf49b1ee15",3));
        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.API_APP, date.toDate(), "100004",  "/weather/windy","5d1577fc692843aa6f49c0cf49b1ee15",4));

        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.API_APP, date.toDate(), "700004",  "/tmap/route" ,"5d1577fc692843aa6f49c0cf49b1ee15",1));
        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.API_APP, date.toDate(), "700004",  "/tmap/traffic","5d1577fc692843aa6f49c0cf49b1ee15",2));
        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.API_APP, date.toDate(), "700004",  "/tmap/traffic","692843aa692843aa6f49c0cf49b1ee15",22));
        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.API_APP, date.toDate(), "700004",  "/tmap/poi","5d1577fc692843aa6f49c0cf49b1ee15",3));
        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.API_APP, date.toDate(), "700004",  "/tmp/landmark","5d1577fc692843aa6f49c0cf49b1ee15",4));


        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.API_APP, date.toDate(), "800004",  "/tmap2/route" ,"5d1577fc692843aa6f49c0cf49b1ee15",1));
        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.API_APP, date.toDate(), "800004",  "/tmap2/traffic","5d1577fc692843aa6f49c0cf49b1ee15",2));
        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.API_APP, date.toDate(), "800004",  "/tmap2/poi","5d1577fc692843aa6f49c0cf49b1ee15",3));
        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.API_APP, date.toDate(), "800004",  "/tmp2/landmark","5d1577fc692843aa6f49c0cf49b1ee15",4));



        //ERROR_API///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.ERROR_API, date.toDate(), "100004",  "400","/weather/dust",1));
        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.ERROR_API, date.toDate(), "100004",  "400","/weather/windy",1));
        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.ERROR_API, date.toDate(), "100004",  "500","/weather/dust",2));
        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.ERROR_API, date.toDate(), "100004",  "500","/weather/sunny",2));
        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.ERROR_API, date.toDate(), "100004",  "401","/weather/dust",3));
        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.ERROR_API, date.toDate(), "100004",  "401","/weather/clean",3));
        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.ERROR_API, date.toDate(), "100004",  "404","/weather/rainy",4));


        //ERROR_APP///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.ERROR_APP, date.toDate(), "100004",  "400","5d1577fc692843aa6f49c0cf49b1ee15",1));
        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.ERROR_APP, date.toDate(), "100004",  "400","692843aa692843aa6f49c0cf49b1ee15y",1));
        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.ERROR_APP, date.toDate(), "100004",  "500","5d1577fc692843aa6f49c0cf49b1ee15",2));
        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.ERROR_APP, date.toDate(), "100004",  "500","692843aa692843aa6f49c0cf49b1ee15",2));
        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.ERROR_APP, date.toDate(), "100004",  "401","5d1577fc692843aa6f49c0cf49b1ee15",3));
        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.ERROR_APP, date.toDate(), "100004",  "401","692843aa692843aa6f49c0cf49b1ee15",3));
        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.ERROR_APP, date.toDate(), "100004",  "404","5d1577fc692843aa6f49c0cf49b1ee15",4));


//        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.APP_API, date.toDate(), "ALL", "400102030", "/weather/dust", 2));
//        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.APP_API, date.toDate(), "ALL", "400102030", "/weather/clean", 4));
//        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.APP_API, date.toDate(), "ALL", "400102030", "/weather/rainy", 6));
//        svcOption2.add(new SvcOption2RC(enumRCType.daily, enumOptionType.APP_API, date.toDate(), "ALL", "400102030", "/weather/windy", 8));


        svcOption1RcRepo.save(svcAppRcs);

        svcOption2RcRepo.save(svcOption2);

        ///////////////////////////////////////////////////////////////////////////////////
        // montly svc app pv

        svcAppRcs.clear();
        date = LocalDate.parse("201607", DateTimeFormat.forPattern("yyyyMM"));

        svcAppRcs.add(new SvcOption1RC(enumRCType.monthly, enumOptionType.APP, date.toDate(), "10004", "400001020", 18l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.monthly, enumOptionType.APP, date.toDate(), "10002", "400001020", 2l));
        date = date.plusMonths(1);//8                     enumOption1Type.APP,
        svcAppRcs.add(new SvcOption1RC(enumRCType.monthly, enumOptionType.APP, date.toDate(), "10004", "400001020", 19l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.monthly, enumOptionType.APP, date.toDate(), "10002", "400001020", 2l));
        date = date.plusMonths(1);//9                     enumOption1Type.APP,
        svcAppRcs.add(new SvcOption1RC(enumRCType.monthly, enumOptionType.APP, date.toDate(), "10004", "400001020", 20l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.monthly, enumOptionType.APP, date.toDate(), "10002", "400001020", 2l));
        date = date.plusMonths(1);//10                    enumOption1Type.APP,
        svcAppRcs.add(new SvcOption1RC(enumRCType.monthly, enumOptionType.APP, date.toDate(), "10004", "400001020",21l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.monthly, enumOptionType.APP, date.toDate(), "10002", "400001020", 2l));
        date = date.plusMonths(1);//11                    enumOption1Type.APP,
        svcAppRcs.add(new SvcOption1RC(enumRCType.monthly, enumOptionType.APP, date.toDate(), "10004", "400001020",22l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.monthly, enumOptionType.APP, date.toDate(), "10002", "400001020", 2l));
        date = date.plusMonths(1);//12                    enumOption1Type.APP,
        svcAppRcs.add(new SvcOption1RC(enumRCType.monthly, enumOptionType.APP, date.toDate(), "10004", "400001020", 23l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.monthly, enumOptionType.APP, date.toDate(), "10002", "400001020", 2l));


        svcOption1RcRepo.save(svcAppRcs);


    }

    @Test
    public void getOption2Pv() throws IOException {

        FileOutputStream fileOut = new FileOutputStream("stat_daily_option2_pv.xlsx");

//        wb = excelExportService.exportExcel("ALL", "APP", "API", enumRCType.daily, "20160706", "20160707", true);
        //TODO API_APP
        wb = excelExportService.exportExcel("ALL", "API", "APP", enumRCType.daily, "20160706", "20160808", true);
//        wb = excelExportService.exportExcel("100004", "ERROR", "API", enumRCType.daily, "20160706", "20160707", true);
//        wb = excelExportService.exportExcel("100004", "ERROR", "APP", enumRCType.daily, "20160706", "20160707", true);

        wb.write(fileOut);
        wb.close();
    }

    @After
    public void afterAllTests() throws IOException {
        wb.close();
    }

    @Test
    public void getMontlySvcAppPv() throws IOException {

        FileOutputStream fileOut = new FileOutputStream("stat_monthly_svc_app_pv.xlsx");

        wb = excelExportService.exportExcel("weather", "app", null, enumRCType.monthly, "201607", "201612", true);

        wb.write(fileOut);
    }

    @Test
    public void getMonthlySvcPV() throws IOException {

        FileOutputStream fileOut = new FileOutputStream("stat_monthly_svc_pv.xlsx");

        wb = excelExportService.exportExcel("weather", null, null, enumRCType.monthly, "201607", "201609", true);



        wb.write(fileOut);
    }


    @Test
    public void getSvcPV() throws IOException {

        FileOutputStream fileOut = new FileOutputStream("stat_daily_svc_pv_10004.xlsx");

        wb = excelExportService.exportExcel("10004", null, null, enumRCType.daily, "20160706", "20160707", true);

        wb.write(fileOut);

        FileOutputStream fileOut2 = new FileOutputStream("stat_daily_svc_pv_all.xlsx");

        wb = excelExportService.exportExcel("ALL", null, null, enumRCType.daily, "20160706", "20160707", true);

        wb.write(fileOut2);
    }




        //TODO 현재는 한파일에 여러 시트를 넣을 수 없게 되어 있음. 추후 수정 필요
    @Test
    public void getOption1Pv() throws IOException {

        FileOutputStream fileOut = new FileOutputStream("stat_전체서비스_daily_svc_app_pv.xlsx");

        wb = excelExportService.exportExcel("ALL", "APP", null, enumRCType.daily, "20160706", "20160707", true);

        wb.write(fileOut);
        wb.close();

        FileOutputStream fileOut2 = new FileOutputStream("stat_daily_svc_app_pv.xlsx");

        wb = excelExportService.exportExcel("10004", "APP", null, enumRCType.daily, "20160706", "20160707", true);

        wb.write(fileOut2);
        wb.close();
        FileOutputStream fileOut3 = new FileOutputStream("stat_daily_svc_api_pv.xlsx");

        wb = excelExportService.exportExcel("10004", "API", null, enumRCType.daily, "20160706", "20160707", true);

        wb.write(fileOut3);
        wb.close();
        FileOutputStream fileOut4 = new FileOutputStream("stat_daily_svc_err.xlsx");

        wb = excelExportService.exportExcel("10004", "ERROR", null, enumRCType.daily, "20160706", "20160707", true);

        wb.write(fileOut4);
        wb.close();
    }


}
