package com.sktechx.palab.logx.test;

import com.google.common.collect.Lists;
import com.sktechx.palab.logx.model.ServiceRequestCall;
import com.sktechx.palab.logx.model.enumRCType;
import com.sktechx.palab.logx.repository.ServiceRCRepository;
import com.sktechx.palab.logx.service.ExcelExportService;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by 1002382 on 2016. 7. 6..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class ExcelExportTest extends AbstractJUnit4SpringMvcTests{
    Logger logger = LoggerFactory.getLogger(ExcelExportTest.class);

    @Autowired
    ExcelExportService excelExportService;


    @Autowired
    ServiceRCRepository svcRcRepo;




    XSSFWorkbook wb;
    Sheet sh;



    @Before
    public void init(){
        //create a workbook

        wb = new XSSFWorkbook();

        //create a sheet with a name
        sh = wb.createSheet("상세");


        List<ServiceRequestCall> rcs = Lists.newArrayList();

        LocalDate date = LocalDate.parse("20160706", DateTimeFormat.forPattern("yyyyMMdd"));

        ServiceRequestCall rc = new ServiceRequestCall(enumRCType.daily, date.toDate(), "10004", 100046l);
        rcs.add(rc);
        rcs.add(new ServiceRequestCall(enumRCType.daily, date.toDate(), "70004", 700046l));

        date = date.plusDays(1);

        rcs.add(new ServiceRequestCall(enumRCType.daily, date.toDate(), "10004", 100047l));
        rcs.add(new ServiceRequestCall(enumRCType.daily, date.toDate(), "70004", 700047l));

        svcRcRepo.save(rcs);

    }

    @Test
    public void getExcel() throws IOException {

        FileOutputStream fileOut = new FileOutputStream("stat.xlsx");

        wb = excelExportService.exportExcel("weather", null, null, enumRCType.daily, "20160706", "20160707", true);



        wb.write(fileOut);
    }




}
