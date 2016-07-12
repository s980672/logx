package com.sktechx.palab.logx.service;

import com.sktechx.palab.logx.model.ServiceRequestCall;
import com.sktechx.palab.logx.model.SvcAppRC;
import com.sktechx.palab.logx.model.enumRCType;
import com.sktechx.palab.logx.repository.RequestCallRepository;
import com.sktechx.palab.logx.repository.ServiceRCRepository;
import com.sktechx.palab.logx.repository.SvcAppRCRepository;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 1002382 on 2016. 7. 8..
 */
@Service
public class StatisticsExcelExportService {

    Logger logger = LoggerFactory.getLogger(StatisticsExcelExportService.class);

    @Autowired
    RequestCallRepository rcRepo;

    @Autowired
    ServiceRCRepository svcRCRepo;

    @Autowired
    SvcAppRCRepository svcAppRcRepo;

    /*
    option1 : none/app/api/error
    option2 : none/api/app
    app-app과 같이 중복된 기준값은 사용 불가


    sheetName = {svcName}_2015-07_{daily}_pv_20160708
    sheetName = {svcName}_2015_{monthly}_pv_20160708

     */
    public XSSFWorkbook exportExcel(String svc, String option1, String option2, enumRCType rcType, String start, String end, boolean isPV) {


        LocalDate startDate;
        LocalDate endDate;

        if ( rcType == enumRCType.daily ) {
            startDate = LocalDate.parse(start, DateTimeFormat.forPattern("yyyyMMdd"));
            endDate = LocalDate.parse(end, DateTimeFormat.forPattern("yyyyMMdd"));
        }else{
            startDate = LocalDate.parse(start, DateTimeFormat.forPattern("yyyyMM"));
            endDate = LocalDate.parse(end, DateTimeFormat.forPattern("yyyyMM"));
        }


        String sheetName = null;
        ExportExcelUtil excelUtil = new ExportExcelUtil();


        sheetName = svc + "_" + (StringUtils.isEmpty(option1) ? "":option1+"_") + rcType + "_pv";

        logger.debug("================================");
        logger.debug("sheet name : {}", sheetName);
        logger.debug("startDate - endDate : {} ~ {}", startDate, endDate);

        if (isPV) {

            Map<Integer, String> headers = new HashMap<>();

            String tableName = "서비스 Request Call";


            if (StringUtils.isEmpty(option1) && StringUtils.isEmpty(option2)) {
                headers.put(1, "서비스");

                excelUtil.createHeader(sheetName, tableName, rcType, startDate, endDate, headers);

                logger.debug("start : {} , end : {}", startDate.toDate(), endDate.toDate());

                List<ServiceRequestCall> rcs = svcRCRepo.findByRcTypeAndBetweenDates(rcType, startDate.toDate(), endDate.toDate());
                List<String> svcIds = svcRCRepo.findDistinctSvcId();
                excelUtil.createDataForSvcPV(sheetName, rcs, svcIds);


            } else if (option1.equals("app") && StringUtils.isEmpty(option2)) {
                //headers.put(1, "APP 명");
                headers.put(1, "APP ID");
                headers.put(2, "서비스");
                tableName = "APP별 Request Call";

                excelUtil.createHeader(sheetName, tableName, rcType, startDate, endDate, headers);
                List<SvcAppRC> rcs = svcAppRcRepo.findByRcTypeAndBetween(rcType, startDate.toDate(), endDate.toDate());


                int dataCnt = 0;
                if ( rcType == enumRCType.daily ) {
                    Days days = Days.daysBetween(startDate, endDate);
                    dataCnt = days.getDays() + 1;
                }else {
                    Months months = Months.monthsBetween(startDate, endDate);
                    dataCnt = months.getMonths() + 1;
                }



                excelUtil.createDataForSvcAppPV(sheetName, rcs, dataCnt, svcAppRcRepo.findDistinctAppIdByRcType(rcType), svcAppRcRepo.findDistinctSvcIdByRcType(rcType) );


            } else if (option1.equals("app") && option2.equals("api")) {
                headers.put(1, "APP 명");
                headers.put(2, "APP ID");
                headers.put(3, "서비스");
                headers.put(4, "API 명");

                tableName = "APP별 Request Call";
                excelUtil.createHeader(sheetName, tableName, rcType, startDate, endDate, headers);


            } else if (option1.equals("api") && StringUtils.isEmpty(option2)) {
                headers.put(1, "서비스");
                headers.put(2, "API 명");
                tableName = "API별 Request Call";
                excelUtil.createHeader(sheetName, tableName, rcType, startDate, endDate, headers);


            } else if (option1.equals("api") && option2.equals("app")) {
                headers.put(1, "서비스");
                headers.put(2, "API 명");
                headers.put(3, "APP 명");
                headers.put(4, "APP ID");
                tableName = "API별 Request Call";
                excelUtil.createHeader(sheetName, tableName, rcType, startDate, endDate, headers);


            } else if (option1.equals("error")) {

                //TODO
            }

            return excelUtil.getWorkBook();

        } //UV TODO
        else{

        }

        return null;
    }
}
