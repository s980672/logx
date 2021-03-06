package com.sktechx.palab.logx.service;

import com.sktechx.palab.logx.model.*;
import com.sktechx.palab.logx.repository.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sktechx.palab.logx.model.enumOptionType.*;

/**
 * Created by 1002382 on 2016. 7. 8..
 */
@Service
public class ExportExcelService {

    Logger logger = LoggerFactory.getLogger(ExportExcelService.class);

    @Autowired
    RequestCallRepository rcRepo;

    @Autowired
    ServiceRCRepository svcRCRepo;

    @Autowired
    SvcOption1RCRepository svcOption1RcRepo;


    @Autowired
    ChangeNameIdService changeNameIdService;

    @Autowired
    SvcOption2RCRepository svcOption2RcRepo;


    private enumOptionType ConvertOption1Option2ToOpType(String op1, String op2) {

        if ( StringUtils.isEmpty(op1) && StringUtils.isEmpty(op2)){
            return enumOptionType.SVC;
        }
        else if (op1.equals("APP") && StringUtils.isEmpty(op2)) {
            return APP;
        } else if (op1.equals("API") && StringUtils.isEmpty(op2)) {
            return API;
        } else if (op1.equals("ERROR") && StringUtils.isEmpty(op2)) {
            return ERROR;
        } else if (op1.equals("APP") && op2.equals("API")){
            return APP_API;
        } else if (op1.equals("API") && op2.equals("APP")){
            return API_APP;
        } else if (op1.equals("ERROR") && op2.equals("APP")){
            return ERROR_APP;
        } else if (op1.equals("ERROR") && op2.equals("API")){
            return ERROR_API;
        }
        return SVC;

    }


    public XSSFWorkbook exportExcel(String svc, String option1, String option2, enumStatsType stsType, enumRCType rcType, LocalDate start, LocalDate end) {
        ExportExcelUtil util = new ExportExcelUtil();

        exportExcel(util, svc, option1, option2, stsType, rcType, start, end);

        return util.getWorkBook();
    }

    /*
    option1 : none/app/api/error
    option2 : none/api/app
    app-app과 같이 중복된 기준값은 사용 불가


    sheetName = {svcName}_2015-07_{daily}_pv_20160708
    sheetName = {svcName}_2015_{monthly}_pv_20160708

     */
    public XSSFWorkbook exportExcel(ExportExcelUtil excelUtil, String svc, String option1, String option2, enumStatsType stsType, enumRCType rcType, LocalDate startDate, LocalDate endDate) {

//        LocalDate startDate = LocalDate.parse(start, DateTimeFormat.forPattern("yyyyMMdd"));
//        LocalDate endDate = LocalDate.parse(end, DateTimeFormat.forPattern("yyyyMMdd"));

        if ( excelUtil == null ) {
            excelUtil = new ExportExcelUtil();
        }

        //svcID를 이름으로 변경 - 엑셀 상단의 서비스명 출력
        String svcText = changeNameIdService.getSvcName(svc);

        enumOptionType opType = ConvertOption1Option2ToOpType(option1, option2);

        String sheetName = opType +"_" + rcType + "_pv";
        logger.debug("================================");
        logger.debug("sheet name : {}", sheetName);
        logger.debug("startDate - endDate : {} ~ {}", startDate, endDate);

        String tableType = "Request Count";
        if ( stsType == enumStatsType.UV )
            tableType = "UV";

        Boolean needTotalSum = false;
        switch(opType){
            case APP:
                Map<Integer, String> headers = new HashMap<>();
                headers.put(1, "APP 명");
                headers.put(2, "APP ID");
                headers.put(3, "서비스");

                List<SvcOption1RC> rcs = null;
                if(svc.equals("ALL")) {
                    rcs = svcOption1RcRepo.findByStsTypeAndOpTypeAndRcTypeAndBetween(stsType, opType, rcType, startDate.toDate(), endDate.toDate());

                }else{
                    rcs = svcOption1RcRepo.findBySvcIdAndStsTypeAndOpTypeAndRcTypeAndBetween(svc, stsType, opType, rcType, startDate.toDate(), endDate.toDate());
                }

                changeNameIdService.fillNameOrIdOfAppOrSvc(opType, rcs);
                needTotalSum = excelUtil.createData(sheetName, rcs, APP, rcType, startDate, endDate, svcText);

                excelUtil.createHeader(sheetName, "APP별 " + tableType, rcType, startDate, endDate, headers, needTotalSum);

                break;
            case API:
                headers = new HashMap<>();
                headers.put(1, "서비스");
                headers.put(2, "API 명");

                if(svc.equals("ALL")) {
                    rcs = svcOption1RcRepo.findByStsTypeAndOpTypeAndRcTypeAndBetween(stsType, opType, rcType, startDate.toDate(), endDate.toDate());

                }else{
                    rcs = svcOption1RcRepo.findBySvcIdAndStsTypeAndOpTypeAndRcTypeAndBetween(svc, stsType, opType, rcType, startDate.toDate(), endDate.toDate());
                }

                changeNameIdService.fillNameOrIdOfAppOrSvc(opType, rcs);
                needTotalSum = excelUtil.createData(sheetName, rcs, API, rcType, startDate, endDate, svcText);

                excelUtil.createHeader(sheetName, "API별 " + tableType, rcType, startDate, endDate, headers, needTotalSum);

                break;
            case ERROR:
                headers = new HashMap<>();
                //ERROR_NONE
                headers.put(1, "Error Code");
                headers.put(2, "서비스");


                if(svc.equals("ALL")) {
                    rcs = svcOption1RcRepo.findByStsTypeAndOpTypeAndRcTypeAndBetween(stsType, opType, rcType, startDate.toDate(), endDate.toDate());
                }else{
                    rcs = svcOption1RcRepo.findBySvcIdAndStsTypeAndOpTypeAndRcTypeAndBetween(svc, stsType, opType, rcType, startDate.toDate(), endDate.toDate());
                }

                changeNameIdService.fillNameOrIdOfAppOrSvc(opType, rcs);
                needTotalSum = excelUtil.createData(sheetName, rcs, ERROR, rcType, startDate, endDate, svcText);

                excelUtil.createHeader(sheetName, "Error Count", rcType, startDate, endDate, headers, needTotalSum);
                break;
            case APP_API:
                headers = new HashMap<>();
                headers.put(1, "APP 명");
                headers.put(2, "APP ID");
                headers.put(3, "API 명");

                //ALL 인경우 svc별로 api가 겹치는 경우가 없으므로 ALL타입의 서비스를 만들 필요없음
                List<SvcOption2RC> rcsAppApi = null;
                if (svc.equals("ALL")) {
                    rcsAppApi = svcOption2RcRepo.findByStsTypeAndOpTypeAndRcTypeAndBetween(stsType, opType, rcType, startDate.toDate(), endDate.toDate());

                }else {
                    rcsAppApi = svcOption2RcRepo.findBySvcIdAndStsTypeAndOpTypeAndRcTypeAndBetween(svc, stsType, opType, rcType, startDate.toDate(), endDate.toDate());
                }
                changeNameIdService.fillNameOrIdOfAppOrSvc(opType, rcsAppApi);
                needTotalSum = excelUtil.createData(sheetName, rcsAppApi, APP_API, rcType, startDate, endDate, svcText);

                excelUtil.createHeader(sheetName, "APP별 " + tableType, rcType, startDate, endDate, headers, needTotalSum);

                break;
            case API_APP:
                headers = new HashMap<>();
                headers.put(1, "서비스");
                headers.put(2, "API 명");
                headers.put(3, "APP 명");
                headers.put(4, "APP ID");

                List<SvcOption2RC> rcsApiApp ;

                if (svc.equals("ALL")) {

                    rcsApiApp = svcOption2RcRepo.findByStsTypeAndOpTypeAndRcTypeAndBetween(stsType, opType, rcType, startDate.toDate(), endDate.toDate());
                }else {
                    rcsApiApp = svcOption2RcRepo.findBySvcIdAndStsTypeAndOpTypeAndRcTypeAndBetween(svc, stsType, opType, rcType, startDate.toDate(), endDate.toDate());
                }

                changeNameIdService.fillNameOrIdOfAppOrSvc(opType, rcsApiApp);
                needTotalSum = excelUtil.createData(sheetName, rcsApiApp, API_APP, rcType, startDate, endDate, svcText);

                excelUtil.createHeader(sheetName, "API별 " + tableType, rcType, startDate, endDate, headers, needTotalSum);

                break;
            case ERROR_APP:
                headers = new HashMap<>();
                //ERROR_APP
                headers.put(1, "Error Code");
                headers.put(2, "APP 명");
                headers.put(3, "APP ID");
                //ALL 타입의 서비스 존재
                List<SvcOption2RC> rcsErrApp = svcOption2RcRepo.findBySvcIdAndStsTypeAndOpTypeAndRcTypeAndBetween(svc, stsType, opType, rcType, startDate.toDate(), endDate.toDate());

                changeNameIdService.fillNameOrIdOfAppOrSvc(opType, rcsErrApp);
                needTotalSum = excelUtil.createData(sheetName, rcsErrApp, ERROR_APP, rcType, startDate, endDate, svcText);

                excelUtil.createHeader(sheetName, "Error Count", rcType, startDate, endDate, headers, needTotalSum);
                break;
            case ERROR_API:
                headers = new HashMap<>();
                //ERROR_API
                headers.put(1, "Error Code");
                headers.put(2, "API 명");

                //ALL 타입의 서비스 존재
                List<SvcOption2RC> rcsErrApi = svcOption2RcRepo.findBySvcIdAndStsTypeAndOpTypeAndRcTypeAndBetween(svc, stsType, opType, rcType, startDate.toDate(), endDate.toDate());

                changeNameIdService.fillNameOrIdOfAppOrSvc(opType, rcsErrApi);

                needTotalSum = excelUtil.createData(sheetName, rcsErrApi, ERROR_API, rcType, startDate, endDate, svcText);

                excelUtil.createHeader(sheetName, "Error Count", rcType, startDate, endDate, headers, needTotalSum);

                break;

            case SVC: //서비스별 Request Count option1/option2가 없는 경우
                headers = new HashMap<>();
                headers.put(1, "서비스");
                List<ServiceRequestCall> listSvcRC;
                if( svc.equals("ALL")) {
                    listSvcRC = svcRCRepo.findSumGroupBySvcIdByStsTypeAndRcTypeAndBetween(stsType, rcType, startDate.toDate(), endDate.toDate());
                }else {
                    listSvcRC = svcRCRepo.findSumGroupBySvcIdBySvcIdAndStsTypeAndRcTypeAndBetween(svc, stsType, rcType, startDate.toDate(), endDate.toDate());
                }

                changeNameIdService.fillNameOrIdOfAppOrSvc(opType, listSvcRC);
                needTotalSum = excelUtil.createData(sheetName, listSvcRC, SVC, rcType, startDate, endDate, svcText);
                excelUtil.createHeader(sheetName, "서비스 별 " + tableType, rcType, startDate, endDate, headers, needTotalSum);


                break;
        }

        return excelUtil.getWorkBook();

    }
}
