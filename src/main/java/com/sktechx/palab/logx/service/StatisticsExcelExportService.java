package com.sktechx.palab.logx.service;

import com.google.common.collect.Lists;
import com.sktechx.palab.logx.model.ServiceRequestCall;
import com.sktechx.palab.logx.model.SvcOption1RC;
import com.sktechx.palab.logx.model.enumOptionType;
import com.sktechx.palab.logx.model.enumRCType;
import com.sktechx.palab.logx.repository.RequestCallRepository;
import com.sktechx.palab.logx.repository.ServiceRCRepository;
import com.sktechx.palab.logx.repository.SvcOption1RCRepository;
import com.sktechx.palab.logx.repository.SvcRepository;
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
public class StatisticsExcelExportService {

    Logger logger = LoggerFactory.getLogger(StatisticsExcelExportService.class);

    @Autowired
    RequestCallRepository rcRepo;

    @Autowired
    ServiceRCRepository svcRCRepo;

    @Autowired
    SvcOption1RCRepository svcAppRcRepo;

    @Autowired
    SvcRepository svcRepo;


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

        ExportExcelUtil excelUtil = new ExportExcelUtil();


        String sheetName = svc + "_" + (StringUtils.isEmpty(option1) ? "":option1+"_") + rcType + "_pv";

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

                List<com.sktechx.palab.logx.model.Service> svcs = Lists.newArrayList();

                svcRCRepo.findDistinctSvcId().stream().forEach(s -> {

                    logger.debug("s : {}", s);

                    svcs.add(svcRepo.findOne(s));

                    logger.debug("svcs : {}", svcs);

                });

                excelUtil.createDataForSvcPV(sheetName, rcs, svcs);


            }
            else if (option1.equals("app")){

                tableName = "APP별 Request Count";
                headers.put(1, "APP 명");
                headers.put(2, "APP ID");

                if( option2.equals("api")){
                    headers.put(3, "API 명");

                }else{ //option2가 없는 경우
                    headers.put(3, "서비스");

                    List<SvcOption1RC> rcs = svcAppRcRepo.findByRcTypeAndBetween(rcType, startDate.toDate(), endDate.toDate());
                    excelUtil.createDate2(sheetName, rcs ,enumOptionType.APP, svcAppRcRepo.findDistinctOption1ByRcType(rcType),
                            null, svcAppRcRepo.findDistinctSvcIdByRcType(rcType));

                }
                excelUtil.createHeader(sheetName, tableName, rcType, startDate, endDate, headers);
            }
            else if ( option1.equals("api")){
                tableName = "API별 Request Count";
                headers.put(1, "서비스");
                headers.put(2, "API 명");

                //API_APP
                if( option2.equals("app")){
                    headers.put(3, "APP 명");
                    headers.put(4, "APP ID");


                }
                else{ //API   //option2가 없는 경우

                    List<SvcOption1RC> rcs = svcAppRcRepo.findByRcTypeAndBetween(rcType, startDate.toDate(), endDate.toDate());
                    excelUtil.createDate2(sheetName, rcs ,enumOptionType.API, svcAppRcRepo.findDistinctOption1ByRcType(rcType),
                            null, svcAppRcRepo.findDistinctSvcIdByRcType(rcType));
                }

                excelUtil.createHeader(sheetName, tableName, rcType, startDate, endDate, headers);



            }
            else if (option1.equals("error")) {


                tableName = "Error Count";

                if ( option2.equals("app")) {
                    //ERROR_APP
                    headers.put(1, "Error Code");
                    headers.put(2, "APP 명");
                    headers.put(2, "APP ID");
                }else if ( option2.equals("api")){
                    //ERROR_API
                    headers.put(1, "Error Code");
                    headers.put(2, "API 명");
                }else{
                    //ERROR_NONE
                    headers.put(1, "Error Code");
                    headers.put(2, "서비스");

                    List<SvcOption1RC> rcs = svcAppRcRepo.findByRcTypeAndBetween(rcType, startDate.toDate(), endDate.toDate());
                    excelUtil.createDate2(sheetName, rcs, enumOptionType.ERROR, svcAppRcRepo.findDistinctOption1ByRcType(rcType),
                            null, svcAppRcRepo.findDistinctSvcIdByRcType(rcType));


                }

                excelUtil.createHeader(sheetName, tableName, rcType, startDate, endDate, headers);
                //TODO excel 상단에 [서비스: tmap] 또는 [서비스 : 전체 서비스] 찍기
                //error는 상세코드가 없으므로 소계는 찍지 않는다
            }

            return excelUtil.getWorkBook();

        } //UV TODO
        else{

        }

        return null;
    }
}
