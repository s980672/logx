package com.sktechx.palab.logx.test;

import com.google.common.collect.Lists;
import com.sktechx.palab.logx.config.AbstractJUnit4SpringMvcTests;
import com.sktechx.palab.logx.config.Application;
import com.sktechx.palab.logx.model.ServiceRequestCall;
import com.sktechx.palab.logx.model.SvcOption1RC;
import com.sktechx.palab.logx.model.enumOptionType;
import com.sktechx.palab.logx.model.enumRCType;
import com.sktechx.palab.logx.repository.ServiceRCRepository;
import com.sktechx.palab.logx.repository.SvcOption1RCRepository;
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
    SvcOption1RCRepository svcAppRcRepo;

    static XSSFWorkbook wb;



    @Before
    public void init(){
        //create a workbook

        wb = new XSSFWorkbook();


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

        svcAppRcs.add(new SvcOption1RC(enumRCType.daily, enumOptionType.APP, date.toDate(), "10004", "400001020",1l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily,enumOptionType.APP, date.toDate(), "10004", "400001540",2l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily,enumOptionType.APP, date.toDate(), "10004", "400002323",3l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily,enumOptionType.APP, date.toDate(), "10004", "400023201",4l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily,enumOptionType.APP, date.toDate(), "10004", "400102021",5l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily,enumOptionType.APP, date.toDate(), "10004", "400102030", 6l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily,enumOptionType.APP, date.toDate(), "10020", "400001020",201l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily,enumOptionType.APP, date.toDate(), "10020", "400001540",202l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily,enumOptionType.APP, date.toDate(), "10020", "400002323",203l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily,enumOptionType.APP, date.toDate(), "10020", "400023201",204l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily,enumOptionType.APP, date.toDate(), "10020", "400102021", 205l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily,enumOptionType.APP, date.toDate(), "10020", "400102030", 206l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily,enumOptionType.APP, date.toDate(), "10073", "400001020",731l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily,enumOptionType.APP, date.toDate(), "10073", "400001540",732l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily,enumOptionType.APP, date.toDate(), "10073", "400002323",733l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily,enumOptionType.APP, date.toDate(), "10073", "400023201",734l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily,enumOptionType.APP, date.toDate(), "10073", "400102021",735l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily,enumOptionType.APP,date.toDate(), "10073", "400102030",736l));

        date = date.plusDays(1);

        svcAppRcs.add(new SvcOption1RC(enumRCType.daily,enumOptionType.APP, date.toDate(), "10004", "400001020",11l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily,enumOptionType.APP, date.toDate(), "10004", "400001540",12l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily,enumOptionType.APP, date.toDate(), "10004", "400002323",13l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily,enumOptionType.APP, date.toDate(), "10004", "400023201",14l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily,enumOptionType.APP, date.toDate(), "10004", "400102021",15l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily,enumOptionType.APP, date.toDate(), "10004", "400102030", 16l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily,enumOptionType.APP, date.toDate(), "10020", "400001020",1201l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily,enumOptionType.APP, date.toDate(), "10020", "400001540",1202l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily,enumOptionType.APP, date.toDate(), "10020", "400002323",1203l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily,enumOptionType.APP, date.toDate(), "10020", "400023201",1204l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily,enumOptionType.APP, date.toDate(), "10020", "400102021", 1205l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily,enumOptionType.APP, date.toDate(), "10020", "400102030", 1206l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily,enumOptionType.APP, date.toDate(), "10073", "400001020",1731l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily,enumOptionType.APP, date.toDate(), "10073", "400001540",1732l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily,enumOptionType.APP, date.toDate(), "10073", "400002323",1733l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily,enumOptionType.APP, date.toDate(), "10073", "400023201",1734l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily,enumOptionType.APP, date.toDate(), "10073", "400102021",1735l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.daily,enumOptionType.APP, date.toDate(), "10073", "400102030",1736l));

        svcAppRcRepo.save(svcAppRcs);


        ///////////////////////////////////////////////////////////////////////////////////
        // montly svc app pv

        svcAppRcs.clear();
        date = LocalDate.parse("201607", DateTimeFormat.forPattern("yyyyMM"));

        svcAppRcs.add(new SvcOption1RC(enumRCType.monthly,enumOptionType.APP, date.toDate(), "10004", "400001020", 18l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.monthly,enumOptionType.APP, date.toDate(), "10002", "400001020", 2l));
        date = date.plusMonths(1);//8                     enumOptionType.APP,
        svcAppRcs.add(new SvcOption1RC(enumRCType.monthly,enumOptionType.APP, date.toDate(), "10004", "400001020", 19l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.monthly,enumOptionType.APP, date.toDate(), "10002", "400001020", 2l));
        date = date.plusMonths(1);//9                     enumOptionType.APP,
        svcAppRcs.add(new SvcOption1RC(enumRCType.monthly,enumOptionType.APP, date.toDate(), "10004", "400001020", 20l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.monthly,enumOptionType.APP, date.toDate(), "10002", "400001020", 2l));
        date = date.plusMonths(1);//10                    enumOptionType.APP,
        svcAppRcs.add(new SvcOption1RC(enumRCType.monthly,enumOptionType.APP, date.toDate(), "10004", "400001020",21l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.monthly,enumOptionType.APP, date.toDate(), "10002", "400001020", 2l));
        date = date.plusMonths(1);//11                    enumOptionType.APP,
        svcAppRcs.add(new SvcOption1RC(enumRCType.monthly,enumOptionType.APP, date.toDate(), "10004", "400001020",22l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.monthly,enumOptionType.APP, date.toDate(), "10002", "400001020", 2l));
        date = date.plusMonths(1);//12                    enumOptionType.APP,
        svcAppRcs.add(new SvcOption1RC(enumRCType.monthly,enumOptionType.APP, date.toDate(), "10004", "400001020", 23l));
        svcAppRcs.add(new SvcOption1RC(enumRCType.monthly,enumOptionType.APP, date.toDate(), "10002", "400001020", 2l));


        svcAppRcRepo.save(svcAppRcs);


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

        FileOutputStream fileOut = new FileOutputStream("stat_daily_svc_pv.xlsx");

        wb = excelExportService.exportExcel("weather", null, null, enumRCType.daily, "20160706", "20160707", true);



        wb.write(fileOut);
    }


    @Test
    public void getSvcAppPv() throws IOException {

        FileOutputStream fileOut = new FileOutputStream("stat_daily_svc_app_pv.xlsx");

        wb = excelExportService.exportExcel("weather", "app", null, enumRCType.daily, "20160706", "20160707", true);

        wb.write(fileOut);
    }


}
