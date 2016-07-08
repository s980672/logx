package com.sktechx.palab.logx.service;

import com.sktechx.palab.logx.model.ServiceRequestCall;
import com.sktechx.palab.logx.model.enumRCType;
import com.sktechx.palab.logx.repository.RequestCallRepository;
import com.sktechx.palab.logx.repository.ServiceRCRepository;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.LocalDate;
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
public class ExcelExportService {

    Logger logger = LoggerFactory.getLogger(ExcelExportService.class);

    @Autowired
    RequestCallRepository rcRepo;

    @Autowired
    ServiceRCRepository svcRCRepo;

    /*
    option1 : none/app/api/error
    option2 : none/api/app
    app-app과 같이 중복된 기준값은 사용 불가


    sheetName = {svcName}_2015-07_{daily}_pv_20160708
    sheetName = {svcName}_2015_{monthly}_pv_20160708




     */
    public XSSFWorkbook exportExcel(String svc, String option1, String option2, enumRCType rcType, String start, String end, boolean isPV) {

        LocalDate startDate = LocalDate.parse(start, DateTimeFormat.forPattern("yyyyMMdd"));
        LocalDate endDate = LocalDate.parse(end, DateTimeFormat.forPattern("yyyyMMdd"));



        String sheetName = null;

        sheetName = svc + "_" + startDate + "_" + rcType + "_pv";

        logger.debug("================================");
        logger.debug("startDate - endDate : {} ~ {}", startDate, endDate);

        if (isPV) {

            Map<Integer, String> headers = new HashMap<>();

            String tableName = "서비스 Request Call";


            if (StringUtils.isEmpty(option1) && StringUtils.isEmpty(option2)) {
                headers.put(1, "서비스");

            } else if (option1.equals("app") && StringUtils.isEmpty(option2)) {
                headers.put(1, "APP 명");
                headers.put(2, "APP ID");
                headers.put(3, "서비스");

                tableName = "APP별 Request Call";
            } else if (option1.equals("app") && option2.equals("api")) {
                headers.put(1, "APP 명");
                headers.put(2, "APP ID");
                headers.put(3, "서비스");
                headers.put(4, "API 명");

                tableName = "APP별 Request Call";

            } else if (option1.equals("api") && StringUtils.isEmpty(option2)) {
                headers.put(1, "서비스");
                headers.put(2, "API 명");
                tableName = "API별 Request Call";

            } else if (option1.equals("api") && option2.equals("app")) {
                headers.put(1, "서비스");
                headers.put(2, "API 명");
                headers.put(3, "APP 명");
                headers.put(4, "APP ID");
                tableName = "API별 Request Call";

            } else if (option1.equals("error")) {

                //TODO
            }

            ExportExcelUtil excelUtil = new ExportExcelUtil();

            excelUtil.createHeader(sheetName, tableName, rcType, startDate, endDate, headers);

            List<ServiceRequestCall> rcs = svcRCRepo.findByRcTypeAndBetweenDates(rcType, startDate.toDate(), endDate.toDate());

            List<String> svcIds = svcRCRepo.findDistinctKeySvcId();

            excelUtil.createDataForSvcPV(sheetName, rcs, svcIds);


            return excelUtil.getWorkBook();

        }

        return null;
    }
}
