package com.sktechx.palab.logx.service;

import com.google.common.collect.Maps;
import com.sktechx.palab.logx.model.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Sunny on 8/7/15.
 */
@Service
public class ExportExcelUtil extends abstractExportExcel {

    Logger logger = LoggerFactory.getLogger(ExportExcelUtil.class);

    /*
    sheetName = {svcName}_2015-07_{daily}_pv_20160708
    sheetName = {svcName}_2015_{monthly}_pv_20160708
    tableName = 서비스별 Request Call/APP별 Request Call/APP별 UV ....
    dataStartCol = 데이터가 들어가기 시작하는 컬럼 번호 (옵션에 따라 달라짐)
     */
    public void createHeader(String sheetName, String tableName, enumRCType type,
                             LocalDate start, LocalDate end, Map<Integer, String> headers, boolean needSum){

        CellStyle headerStyle = getHeaderStyle2();

        Sheet sh = getSheet(sheetName);

        /* 조회하려는 달의 갯수 또는 일의 갯수*/
        int colNum = 0;

        int dataStartCol = headers.size() + 1;


        String dateFormat = null;

        if ( type == enumRCType.monthly )//월별
        {
            Months months = Months.monthsBetween(start, end);

            colNum = months.getMonths();

            //Years years = Years.yearsBetween(start, end);

            dateFormat = "yyyy년";

        }else if(type == enumRCType.daily ) {//일별

            Days days = Days.daysBetween(start, end);

            colNum = days.getDays();

            dateFormat = "yyyy년 MM월";

            //TODO 2달에 걸쳐있는 경우
            Months months = Months.monthsBetween(start, end);

            if ( months.getMonths() > 1){

            }

        }


        for (int i = 0; i <= colNum; i++) {
            if ( type == enumRCType.daily )
                setCellValue(sh, 3, dataStartCol + i, start.plusDays(i).getDayOfMonth() + "일", headerStyle );
            else
                setCellValue(sh, 3, dataStartCol + i, start.plusMonths(i).getMonthOfYear() + "월", headerStyle );

            setCellValue(sh, 2, dataStartCol + i, start.toString(dateFormat), headerStyle );

            //first column - table name
            setCellValue(sh, 1, dataStartCol + i, tableName, headerStyle);
        }


        setCellValue(sh, 1, dataStartCol+colNum + 1, tableName, headerStyle);

        //컬럼 헤더가 두개 이상인 경우 - 소계 필요
        if ( dataStartCol > 2 && needSum){

            setCellValue(sh, 2, dataStartCol+colNum + 1, "소계", headerStyle);
            setCellValue(sh, 3, dataStartCol+colNum + 1, "소계", headerStyle);

            setCellValue(sh, 1, dataStartCol+colNum + 2, tableName, headerStyle);
            setCellValue(sh, 2, dataStartCol+colNum + 2, "합계", headerStyle);
            setCellValue(sh, 3, dataStartCol+colNum + 2, "합계", headerStyle);

            sh.addMergedRegion(new CellRangeAddress(2, 3, dataStartCol + colNum + 2, dataStartCol + colNum + 2));
            //소계 까지 있는 경우 소계 컬럼 까지 추가로 병합
            sh.addMergedRegion(new CellRangeAddress(1, 1, dataStartCol, dataStartCol+colNum + 2)); //첫번째 row => 서비스별 Request Count


        }else{

            setCellValue(sh, 2, dataStartCol+colNum + 1, "합계", headerStyle);
            setCellValue(sh, 3, dataStartCol+colNum + 1, "합계", headerStyle);
            sh.addMergedRegion(new CellRangeAddress(1, 1, dataStartCol, dataStartCol + colNum + 1)); //첫번째 row => 서비스별 Request Count

        }

        //합계 컬럼 병합
        sh.addMergedRegion(new CellRangeAddress(2, 3, dataStartCol + colNum + 1, dataStartCol + colNum + 1));

        headers.forEach((col, h) -> {

            setCellValue(sh, 1, col, h, headerStyle); //service/app/api
            setCellValue(sh, 2, col, h, headerStyle); //service/app/api
            setCellValue(sh, 3, col, h, headerStyle); //service/app/api


        });

        for(int i = 1 ; i < dataStartCol ; i++ ){

            sh.addMergedRegion(new CellRangeAddress(1, 3, i, i)); //columns => service/app/api

            sh.autoSizeColumn(i);
        }


        sh.addMergedRegion(new CellRangeAddress(2, 2, dataStartCol, dataStartCol+colNum)); //두번째 row => 2015년 or 2015년 7월



    }

    @Override
    public void createData(String sheetName, Object object) {

    }


    private class ExcelRow{
        public int row;
        public int col;
        public long total;
        public long subTotal;
        public ExcelRow(int row, int col, long total){
            this.row = row; this.col=col; this.total=total;
        }

        public ExcelRow(int row, int col, long subTotal, long total){
            this.row = row; this.col=col; this.total=total; this.subTotal = subTotal;
        }

        public String toString() {
            return "ExcelRow{" +
                    "row='" + row + '\'' +
                    ", col='" + col + '\'' +
                    ", subTotal='" + subTotal + '\'' +
                    ", total='" + total + '\'' +
                    '}';
        }

    }


    public Boolean createData(String sheetName, Object dataLst, enumOptionType opType, enumRCType rcType, LocalDate startDate, LocalDate endDate){

        if ( startDate == null || endDate == null ) {
            logger.error("Set first StartDate and EndDate");
            throw new IllegalArgumentException("Set first StartDate and EndDate");
        }

        final Boolean[] needToMerge = {false};

        Sheet sheet = getSheet(sheetName);

        CellStyle style = getDataStyle();

        ExcelRow ex = new ExcelRow(4, 1, 0);

        ExcelRow mergeEx = new ExcelRow(4, 1, 0);
        ExcelRow mergeSvc = new ExcelRow(4, 1, 0);

        Map<Date, Long> map = Maps.newTreeMap();

        switch(opType){
            case SVC:
                List<ServiceRequestCall> lstSvcRc = (List<ServiceRequestCall>) dataLst;
                map.clear();

                lstSvcRc.stream().map(rc->rc.getId().getSvcId()).distinct().forEach(svc->{
                //svcs.stream().forEach(svc->{

                    lstSvcRc.stream().filter(svcRc->svcRc.getId().getSvcId().equals(svc)).forEach(rc -> {
                        map.put(rc.getId().getReqDt(), rc.getCount());
                    });

                    for (LocalDate date = startDate; map.size() > 0 &&
                            date.isBefore(endDate) || date.isEqual(endDate); date = (rcType == enumRCType.daily ? date.plusDays(1) : date.plusMonths(1))) {
                        Long aLong = map.get(date.toDate());
                        if (aLong == null) {
                            logger.debug("date : {} is not found", date.toString());
                            map.put(date.toDate(), 0l);
                        }
                    }

                    if (map.size() > 0) {
                        setCellValue(sheet, ex.row, ex.col++, /*TODO service name*/svc, style);
                        // count 출력
                        writeCountsInARow(sheet, ex, style, map);
                        map.clear();
                    }

                });

                break;

            case APP:
                List<SvcOption1RC> lst = (List<SvcOption1RC>) dataLst;

                map.clear();

                lst.stream().map(rc->rc.getId().getOption1()).distinct().forEach(op1 -> { //appKey
                    lst.stream().map(rc -> rc.getId().getSvcId()).distinct().forEach(svc -> { //svcId

                        lst.stream().filter(d -> d.getId().getOption1().equals(op1) && d.getId().getSvcId().equals(svc)).forEach(d -> {

                            map.put(d.getId().getReqDt(), d.getCount());
                        });

                        for (LocalDate date = startDate; map.size() > 0 &&
                                date.isBefore(endDate) || date.isEqual(endDate); date = (rcType == enumRCType.daily ? date.plusDays(1) : date.plusMonths(1))) {
                            Long aLong = map.get(date.toDate());
                            if (aLong == null) {
                                logger.debug("date : {} is not found", date.toString());
                                map.put(date.toDate(), 0l);
                            }
                        }

                        if (map.size() > 0) {

                            setCellValue(sheet, ex.row, ex.col++, /*TODO app Key to app NAME*/op1, style);
                            setCellValue(sheet, ex.row, ex.col++, /*TODO app id*/"APP ID", style);
                            setCellValue(sheet, ex.row, ex.col++, /*TODO service name*/svc, style);

                            // count 출력
                            writeCountsInARow(sheet, ex, style, map);

                            mergeEx.col = /*hearder*/ 3 + map.size() + 2/*소계*/;
                            map.clear();
                        }


                    }); //svc loop
                    //합계 - 서비스가 여러개인 경우 == 전체
                    if (mergeEx.row < ex.row-1) {

                        //합계
                        sheet.addMergedRegion(new CellRangeAddress(mergeEx.row, ex.row-1, mergeEx.col, mergeEx.col));

                        setCellValue(sheet, mergeEx.row, mergeEx.col, ex.total + "", style);

                        setCellStyle(sheet, mergeEx.row, ex.row-1, mergeEx.col, mergeEx.col, style);

                        //APP 명 merge
                        sheet.addMergedRegion(new CellRangeAddress(mergeEx.row, ex.row-1, 1, 1));

                        //APP ID merge
                        sheet.addMergedRegion(new CellRangeAddress(mergeEx.row, ex.row-1, 2, 2));

                        ex.total = 0;
                        needToMerge[0] = true;
                    }

                    mergeEx.row = ex.row;

                });

                break;

            case API: {
                //서비스 - API 명 ------------------------------- 합계
                List<SvcOption1RC> lstApis = (List<SvcOption1RC>) dataLst;
                map.clear();

                lstApis.stream().map(api -> api.getId().getSvcId()).distinct().forEach(svc -> { //svc
                    lstApis.stream().map(api -> api.getId().getOption1()).distinct().forEach(op1 -> { //api

                        lstApis.stream().filter(d -> d.getId().getOption1().equals(op1) && d.getId().getSvcId().equals(svc)).forEach(d -> {
                            map.put(d.getId().getReqDt(), d.getCount());

                        });

                        for (LocalDate date = startDate; map.size() > 0 &&
                                date.isBefore(endDate) || date.isEqual(endDate); date = (rcType == enumRCType.daily ? date.plusDays(1) : date.plusMonths(1))) {
                            Long aLong = map.get(date.toDate());
                            if (aLong == null) {
                                logger.debug("date : {} is not found", date.toString());
                                map.put(date.toDate(), 0l);
                            }
                        }

                        if (map.size() > 0 ){

                            setCellValue(sheet, ex.row, ex.col++, /*TODO service name*/svc, style);
                            setCellValue(sheet, ex.row, ex.col++, op1, style); //TODO API 명

                            // count 출력
                            writeCountsInARow(sheet, ex, style, map);

                            mergeEx.col = 2 + map.size() + 2;
                            map.clear();
                        }


                    });//svc loop

                    if ( mergeEx.row < ex.row - 1 ) {

                        //서비스 컬럼 병합
                        sheet.addMergedRegion(new CellRangeAddress(mergeEx.row, ex.row-1, 1/*서비스 컬럼*/, 1));
                        sheet.addMergedRegion(new CellRangeAddress(mergeEx.row, ex.row-1, mergeEx.col/*합계 컬럼*/, mergeEx.col));

                        //합계 -
                        setCellValue(sheet, mergeEx.row, mergeEx.col, ex.total + "", style);

                        setCellStyle(sheet, mergeEx.row, ex.row - 1, mergeEx.col, mergeEx.col, style);
                        ex.total = 0;
                        needToMerge[0] = true;
                    }
                    mergeEx.row = ex.row;
                });

            }

            break;
            case ERROR:

                //HADER : ERROR CODE - ------------------------------- 합계
                List<SvcOption1RC> lstErrs = (List<SvcOption1RC>) dataLst;
                map.clear();

                lstErrs.stream().map(err->err.getId().getOption1()).distinct().forEach(op1->{
                    //opt1.stream().forEach(op1->{
                    lstErrs.stream().map(err->err.getId().getSvcId()).distinct().forEach(svc->{
                        //svcs.stream().forEach(svc->{


                        lstErrs.stream().filter( d-> d.getId().getOption1().equals(op1) && d.getId().getSvcId().equals(svc)).forEach(d -> {

                            map.put(d.getId().getReqDt(), d.getCount());

                        });

                        for (LocalDate date = startDate; map.size() > 0 &&
                                date.isBefore(endDate) || date.isEqual(endDate); date = (rcType == enumRCType.daily ? date.plusDays(1) : date.plusMonths(1))) {
                            Long aLong = map.get(date.toDate());
                            if (aLong == null) {
                                logger.debug("date : {} is not found", date.toString());
                                map.put(date.toDate(), 0l);
                            }
                        }

                        if (map.size() > 0 ){

                            setCellValue(sheet, ex.row, ex.col++, op1, style); //에러 코드
                            setCellValue(sheet, ex.row, ex.col++, /*TODO service name*/svc, style);

                            // count 출력
                            writeCountsInARow(sheet, ex, style, map);

                            mergeEx.col = 2/*header size*/ + map.size() + 2/*소계 + 합계*/;
                            map.clear();
                        }
                    });

                    logger.debug("mergeEx.row : {}, ex.row : {}", mergeEx.row, ex.row-1);

                    //TODO 합계 - 병합된  Error code 단위의 합계 필요 - 서비스가 전체인 경우 - header 처리 필요
                    if(mergeEx.row < ex.row - 1  ) {

                        sheet.addMergedRegion(new CellRangeAddress(mergeEx.row, ex.row - 1, 1/*Error code column*/, 1));

                        //합계
                        sheet.addMergedRegion(new CellRangeAddress(mergeEx.row, ex.row - 1, mergeEx.col/*total sum*/, mergeEx.col));
                        setCellValue(sheet, mergeEx.row, mergeEx.col, ex.total + "", style);
                        setCellStyle(sheet, mergeEx.row, ex.row-1, mergeEx.col/*total sum*/, mergeEx.col, style );

                        ex.total = 0;
                        needToMerge[0] = true;
                    }

                    mergeEx.row = ex.row;

                });


                break;
            case APP_API:
                //HEADER : APP 명 - APP ID - API 명 -------------------------소계 - 합계
                //app 명, app id 병합
                List<SvcOption2RC> lstAppApi = (List<SvcOption2RC>) dataLst;

                createAppApi(sheet, lstAppApi, opType, rcType,startDate,endDate);

                needToMerge[0] = true;
                break;


            case API_APP:
                //HEADER : 서비스 - API 명 - APP 명 - APP ID-------------------------소계 - 합계
                // 서비스 병합, API 명 병합
                List<SvcOption2RC> lstApiApp = (List<SvcOption2RC>) dataLst;

                createApiApp(sheet, lstApiApp, opType, rcType, startDate, endDate);

                needToMerge[0] = true;
                break;
            case ERROR_APP:
                //HEADER : ERROR code - APP 명 - APP ID -----------------------  소계 - 합계
                //error code 병합

                List<SvcOption2RC> lstErrApp = (List<SvcOption2RC>) dataLst;
                createErrorOption2(sheet, lstErrApp, opType, rcType, startDate, endDate);

                needToMerge[0] = true;
                break;

            case ERROR_API:
                //HEADER : ERROR code - API 명 -----------------------  소계 - 합계
                //error code 병합
                List<SvcOption2RC> lstErrApi = (List<SvcOption2RC>) dataLst;

                createErrorOption2(sheet, lstErrApi, opType, rcType, startDate, endDate);

                needToMerge[0] = true;
                break;

        }

        return needToMerge[0];
    }

    private void writeCountsInARow(Sheet sheet, ExcelRow ex, CellStyle style, Map<Date, Long> map){

        Set<Map.Entry<Date, Long>> entries = map.entrySet();
        Iterator<Map.Entry<Date, Long>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry me = iterator.next();
            setCellValue(sheet, ex.row, ex.col++, me.getValue() + "", style);
            logger.debug("row:{} col:{} value:{} date: {}", ex.row, ex.col, me.getValue(), me.getKey().toString());

            ex.subTotal += (long) me.getValue();
        }

        //소계
        setCellValue(sheet, ex.row, ex.col++, ex.subTotal + "", style);
        ex.total += ex.subTotal;
        //한 라인 출력 후 초기화
        ex.subTotal = 0;
        ex.row++;
        ex.col=1;
    }

    private void createApiApp(Sheet sheet, List<SvcOption2RC> lst, enumOptionType opType, enumRCType rcType,
                              LocalDate startDate, LocalDate endDate) {

        final int[] mergeSvcRow = {4};


        lst.stream().forEach(l -> {
            logger.debug("{}", l);
        });


        lst.stream().map(rc->rc.getId().getSvcId()).distinct().forEach(svc -> {
            //lstSvc.stream().forEach(svc-> {

            logger.debug("=========================== {}", svc);

            mergeSvcRow[0] = createApiOption2(sheet, svc, mergeSvcRow[0], lst, opType, rcType, startDate, endDate);


        });

    }

    private void createAppApi(Sheet sheet, List<SvcOption2RC> lst, enumOptionType opType, enumRCType rcType, LocalDate startDate, LocalDate endDate){

        CellStyle style = getDataStyle();

        ExcelRow ex = new ExcelRow(4, 1, 0);

        ExcelRow mergeEx = new ExcelRow(4, 1, 0);


        Map<Date, Long> map = Maps.newTreeMap();

        lst.stream().map(aa->aa.getId().getOption1()).distinct().forEach(op1 -> { //APP KEY

            mergeEx.row = ex.row;

            lst.stream().map(aa -> aa.getId().getOption2()).distinct().forEach(op2 -> { //API KEY

                //한 행에 해당하는 데이터가 lstErr에 있음
                lst.stream().filter(d -> d.getId().getRcType() == rcType && d.getId().getOpType() == opType &&
                        d.getId().getOption1().equals(op1) &&
                        d.getId().getOption2().equals(op2)).forEach(d -> {
                    map.put(d.getId().getReqDt(), d.getCount());
                });

                for (LocalDate date = startDate; map.size() > 0 &&
                        date.isBefore(endDate) || date.isEqual(endDate); date = (rcType == enumRCType.daily ? date.plusDays(1) : date.plusMonths(1))) {
                    Long aLong = map.get(date.toDate());
                    if (aLong == null) {
                        logger.debug("date : {} is not found", date.toString());
                        map.put(date.toDate(), 0l);
                    }
                }

                //해당 row 에 데이터가 있는 경우 subtotal를 찍는다
                if (map.size() > 0) {

                    //헤더 출력
                    setCellValue(sheet, ex.row, ex.col++, /*TODO APP NAME*/ op1, style);
                    setCellValue(sheet, ex.row, ex.col++, /*TODO appKey를 APP ID 대체 필요*/ op1, style);
                    setCellValue(sheet, ex.row, ex.col++, /*TODO appKey인데 apiID로 대체 필요*/op2, style);


                    // count 출력
                    Set<Map.Entry<Date, Long>> entries = map.entrySet();
                    Iterator<Map.Entry<Date, Long>> iterator = entries.iterator();
                    while (iterator.hasNext()) {
                        Map.Entry me = iterator.next();
                        setCellValue(sheet, ex.row, ex.col++, me.getValue() + "", style);
                        logger.debug("row:{} col:{} op1 : {}, op2 : {}, value:{} date: {}", ex.row, ex.col, op1, op2, me.getValue(), me.getKey().toString());

                        ex.subTotal += (long) me.getValue();

                    }

                    map.clear();

                    //소계
                    setCellValue(sheet, ex.row, ex.col, ex.subTotal + "", style);
                    logger.debug("row:{} col:{} subtotal:{}", ex.row, ex.col, ex.subTotal);

                    ex.total += ex.subTotal;
                    ex.row++;
                    ex.subTotal = 0;
                    mergeEx.col = ex.col;
                    ex.col = 1;
                }

            });

            logger.debug("op1 : {} mergeEx.row : {} ex.row : {}", op1, mergeEx.row, ex.row);

            if (mergeEx.row <= ex.row - 1) {
                sheet.addMergedRegion(new CellRangeAddress(mergeEx.row, ex.row - 1, 1/*APP NAME column*/, 1));
                sheet.addMergedRegion(new CellRangeAddress(mergeEx.row, ex.row - 1, 2/*APP ID column*/, 2));

                //API 별 합계 필요
                mergeEx.col += 1;
                logger.debug("mergeEx.col : {} ex.total: {}", mergeEx.col, ex.total);
                sheet.addMergedRegion(new CellRangeAddress(mergeEx.row, ex.row - 1, mergeEx.col/*total sum column*/, mergeEx.col));
                setCellValue(sheet, mergeEx.row, mergeEx.col, ex.total + "", style);
                setCellStyle(sheet, mergeEx.row, ex.row - 1, mergeEx.col, mergeEx.col, style);

                //이 라인이 없으면 서비스 병합 시 마지막 행이 깨짐
                mergeEx.row = ex.row - 1;
                ex.total = 0;
            }
        });


        sheet.autoSizeColumn(1, true); //APP NAME
        sheet.autoSizeColumn(2, true); //APP ID
        sheet.autoSizeColumn(3, true); //API NAME

    }

    private int createApiOption2(Sheet sheet, String svc, int mergeSvcRow, List<SvcOption2RC> lst, enumOptionType opType,
                                 enumRCType rcType, LocalDate startDate, LocalDate endDate){

        CellStyle style = getDataStyle();

        ExcelRow ex = new ExcelRow(mergeSvcRow, 1, 0);

        ExcelRow mergeEx = new ExcelRow(mergeSvcRow, 1, 0);


        Map<Date, Long> map = Maps.newTreeMap();

        lst.stream().filter(rc -> rc.getId().getSvcId().equals(svc)).map(rc -> rc.getId().getOption1()).
                forEach(op1 -> { //API PATH

                    mergeEx.row = ex.row;

                    lst.stream().filter(rc -> rc.getId().getSvcId().equals(svc)).map(rc -> rc.getId().getOption2()).
                            forEach(op2 -> { //APP KEY

                                //한 행에 해당하는 데이터가 lstErr에 있음
                                lst.stream().filter(d -> d.getId().getRcType() == rcType && d.getId().getOpType() == opType &&
                                        d.getId().getOption1().equals(op1) &&
                                        d.getId().getOption2().equals(op2) && d.getId().getSvcId().equals(svc)).forEach(d -> {
                                    map.put(d.getId().getReqDt(), d.getCount());
                                });

                                for (LocalDate date = startDate; map.size() > 0 &&
                                        date.isBefore(endDate) || date.isEqual(endDate); date = (rcType == enumRCType.daily ? date.plusDays(1) : date.plusMonths(1))) {
                                    Long aLong = map.get(date.toDate());
                                    if (aLong == null) {
                                        logger.debug("date : {} is not found", date.toString());
                                        map.put(date.toDate(), 0l);
                                    }
                                }

                                //해당 row 에 데이터가 있는 경우 subtotal를 찍는다
                                if (map.size() > 0) {

                                    //헤더 출력
                                    setCellValue(sheet, ex.row, ex.col++, /*SERVICE*/ svc, style);
                                    setCellValue(sheet, ex.row, ex.col++, /*ERROR CODE*/ op1, style);
                                    setCellValue(sheet, ex.row, ex.col++, /*TODO appKey를 APP 명 대체 필요*/ op2, style);
                                    setCellValue(sheet, ex.row, ex.col++, /*TODO appKey인데 appID로 대체 필요*/op2, style);


                                    // count 출력
                                    Set<Map.Entry<Date, Long>> entries = map.entrySet();
                                    Iterator<Map.Entry<Date, Long>> iterator = entries.iterator();
                                    while (iterator.hasNext()) {
                                        Map.Entry me = iterator.next();
                                        setCellValue(sheet, ex.row, ex.col++, me.getValue() + "", style);
                                        logger.debug("row:{} col:{} op1 : {}, op2 : {}, value:{} date: {}", ex.row, ex.col, op1, op2, me.getValue(), me.getKey().toString());

                                        ex.subTotal += (long) me.getValue();

                                    }

                                    map.clear();

                                    //소계
                                    setCellValue(sheet, ex.row, ex.col, ex.subTotal + "", style);
                                    logger.debug("row:{} col:{} subtotal:{}", ex.row, ex.col, ex.subTotal);

                                    ex.total += ex.subTotal;
                                    ex.row++;
                                    ex.subTotal = 0;
                                    mergeEx.col = ex.col;
                                    ex.col = 1;
                                }

                            });

                    logger.debug("op1 : {} mergeEx.row : {} ex.row : {}", op1, mergeEx.row, ex.row);

                    if (mergeEx.row <= ex.row - 1) {
                        sheet.addMergedRegion(new CellRangeAddress(mergeEx.row, ex.row - 1, 2/*API NAME column*/, 2));

                        //API 별 합계 필요
                        mergeEx.col += 1;
                        logger.debug("mergeEx.col : {} ex.total: {}", mergeEx.col, ex.total);
                        sheet.addMergedRegion(new CellRangeAddress(mergeEx.row, ex.row - 1, mergeEx.col/*total sum column*/, mergeEx.col));
                        setCellValue(sheet, mergeEx.row, mergeEx.col, ex.total + "", style);
                        setCellStyle(sheet, mergeEx.row, ex.row - 1, mergeEx.col, mergeEx.col, style);

                        //이 라인이 없으면 서비스 병합 시 마지막 행이 깨짐
                        mergeEx.row = ex.row - 1;
                        ex.total = 0;
                    }
                });

        logger.debug("mergeSvcRow : {} mergeEx.row: {}", mergeSvcRow, mergeEx.row);
        sheet.addMergedRegion(new CellRangeAddress(mergeSvcRow, mergeEx.row, 1/*SERVICE column*/, 1));

        sheet.autoSizeColumn(1, true);
        sheet.autoSizeColumn(2, true);
        sheet.autoSizeColumn(3, true);
        sheet.autoSizeColumn(4, true);

        mergeSvcRow = mergeEx.row + 1;



        return mergeSvcRow;

    }


    private void createErrorOption2(Sheet sheet, List<SvcOption2RC> lstErr, enumOptionType opType,
                                    enumRCType rcType, LocalDate startDate, LocalDate endDate ){

        CellStyle style = getDataStyle();

        ExcelRow ex = new ExcelRow(4, 1, 0);

        ExcelRow mergeEx = new ExcelRow(4, 1, 0);


        Map<Date, Long> map = Maps.newTreeMap();

        lstErr.stream().map(err->err.getId().getOption1()).distinct().forEach(op1 -> { //ERROR CODE

            mergeEx.row = ex.row;

            lstErr.stream().map(err -> err.getId().getOption2()).distinct().forEach(op2 -> { //API/APP NAME

                //한 행에 해당하는 데이터가 lstErr에 있음
                lstErr.stream().filter(d -> d.getId().getRcType() == rcType && d.getId().getOpType() == opType &&
                        d.getId().getOption1().equals(op1) &&
                        d.getId().getOption2().equals(op2)).forEach(d -> {
                    map.put(d.getId().getReqDt(), d.getCount());
                });

                for (LocalDate date = startDate; map.size() > 0 &&
                        date.isBefore(endDate) || date.isEqual(endDate); date = (rcType == enumRCType.daily ? date.plusDays(1) : date.plusMonths(1))) {
                    Long aLong = map.get(date.toDate());
                    if (aLong == null) {
                        logger.debug("date : {} is not found", date.toString());
                        map.put(date.toDate(), 0l);
                    }
                }

                //해당 row 에 데이터가 있는 경우 subtotal를 찍는다
                if (map.size() > 0) {

                    //헤더 출력
                    setCellValue(sheet, ex.row, ex.col++, /*ERROR CODE*/ op1, style);
                    setCellValue(sheet, ex.row, ex.col++, /*TODO appKey를 APP 명 대체 필요*/ op2, style);

                    if (opType == enumOptionType.ERROR_APP)
                        setCellValue(sheet, ex.row, ex.col++, /*TODO appKey인데 appID로 대체 필요*/op2, style);

                    // count 출력
                    Set<Map.Entry<Date, Long>> entries = map.entrySet();
                    Iterator<Map.Entry<Date, Long>> iterator = entries.iterator();
                    while (iterator.hasNext()) {
                        Map.Entry me = iterator.next();
                        setCellValue(sheet, ex.row, ex.col++, me.getValue() + "", style);
                        logger.debug("row:{} col:{} op1 : {}, op2 : {}, value:{} date: {}", ex.row, ex.col, op1, op2, me.getValue(), me.getKey().toString());

                        ex.subTotal += (long) me.getValue();

                    }

                    map.clear();

                    //소계
                    setCellValue(sheet, ex.row, ex.col, ex.subTotal + "", style);
                    logger.debug("row:{} col:{} subtotal:{}", ex.row, ex.col, ex.subTotal);

                    ex.total += ex.subTotal;
                    ex.row++;
                    ex.subTotal = 0;
                    mergeEx.col = ex.col;
                    ex.col = 1;
                }

            });

            logger.debug("mergeEx.row : {} ex.row : {}", mergeEx.row, ex.row);

            if (mergeEx.row <= ex.row - 1) {
                //ERROR CODE 컬럼 병합 필요
                sheet.addMergedRegion(new CellRangeAddress(mergeEx.row, ex.row - 1, 1/*ERROR CODE column*/, 1));

                //ERROR CODE 별 합계 필요
                mergeEx.col += 1;
                logger.debug("mergeEx.col : {} ex.total: {}", mergeEx.col, ex.total);
                sheet.addMergedRegion(new CellRangeAddress(mergeEx.row, ex.row - 1, mergeEx.col/*total sum column*/, mergeEx.col));
                setCellValue(sheet, mergeEx.row, mergeEx.col, ex.total + "", style);
                setCellStyle(sheet, mergeEx.row, ex.row - 1, mergeEx.col, mergeEx.col, style);

                ex.total = 0;
            }
        });

        sheet.autoSizeColumn(2, true);

        if (opType == enumOptionType.ERROR_APP){
            sheet.autoSizeColumn(3, true);
        }

    }
}
