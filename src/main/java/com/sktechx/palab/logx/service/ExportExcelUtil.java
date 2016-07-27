package com.sktechx.palab.logx.service;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
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

        Sheet sh = addSheet(sheetName);

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

    }

    public void createDataForSvcPV(String sheetName, List<ServiceRequestCall> lst, List<com.sktechx.palab.logx.model.Service> lstSvc) {

        Sheet sheet = getSheet(sheetName);

        CellStyle style = getDataStyle();

        ExcelRow ex = new ExcelRow(3, 1, 0);

        //row당 data 갯수
        int nPeriod = lst.size()/lstSvc.size();

        lstSvc.stream().forEach(s -> {

            logger.debug("svc : {}", s);

            ex.col = 1;

            //엑셀에 출력 시에는 서비스 이름으로 출력
            setCellValue(sheet, ++ex.row, ex.col, s.getName(), style);

            lst.stream().filter(r -> r.getId().getSvcId().equals(s.getSvcId())).forEach(r -> {

                ex.total += r.getCount();

                setCellValue(sheet, ex.row, ++ex.col, r.getCount() + "", style);

                logger.debug("row-col-value: {}-{}-{}", ex.row, ex.col, r.getCount());

                //TODO +1은 헤더가 서비스 만 있을 경우임 변수화 필요
                //합계 처리
                if (ex.col == nPeriod + 1) {

                    setCellValue(sheet, ex.row, ++ex.col, ex.total + "", style);

                    logger.debug("row-col-total: {}-{}-{}", ex.row, ex.col, ex.total);

                    ex.total = 0l;
                }

            });
        });
    }


    LocalDate startDate = null;

    LocalDate endDate = null;

    enumRCType rcType = enumRCType.daily;

    public void setRcTypeAndStartEndDate(enumRCType rcType, LocalDate start, LocalDate end){
        startDate = start;
        endDate = end;
        rcType = rcType;
    }

    private boolean compareDate(enumRCType rcType, LocalDate date1, LocalDate date2){

        if (date1.getYear() != date2.getYear()) return false;
        if (date1.getMonthOfYear() != date2.getMonthOfYear()) return false;

        if ( rcType == enumRCType.daily ) {
            if (date1.getDayOfMonth() != date2.getDayOfMonth()) return false;
        }

        return true;

    }
    public void createDate2(String sheetName, Object dataLst, enumOptionType opType, List<String> opt1, List<String> opt2, List<String> svcs){

        if ( startDate == null || endDate == null ) {
            logger.error("Set first StartDate and EndDate");
        }

        Sheet sheet = getSheet(sheetName);
        if ( sheet == null ) {
            logger.error("excel sheet is null");
            throw new NullPointerException("Excel sheet is null!");
        }

        CellStyle style = getDataStyle();

        ExcelRow ex = new ExcelRow(4, 1, 0);

        ExcelRow mergeEx = new ExcelRow(4, 1, 0);
        ExcelRow mergeSvc = new ExcelRow(4, 1, 0);

        switch(opType){
            case SVC:
                List<ServiceRequestCall> lstSvcRc = (List<ServiceRequestCall>) dataLst;

                svcs.stream().forEach(svc->{
                    ex.row++;
                    ex.col = 1;
                    ex.total = 0;
                    mergeEx.row = ex.row;
                    setCellValue(sheet, ex.row, ex.col++, svc, style);

                    lstSvcRc.stream().filter(svcRc->svcRc.getId().getSvcId().equals(svc)).forEach(rc -> {
                        setCellValue(sheet, ex.row, ex.col++, rc.getCount() + "", style);
                        ex.total += rc.getCount();
                    });

                    setCellValue(sheet, ex.row, ex.col, ex.total + "", style);
                });

                break;

            case APP:
                List<SvcOption1RC> lst = (List<SvcOption1RC>) dataLst;

                opt1.stream().forEach(op1 -> {

                    svcs.stream().forEach(svc -> {
                        ex.row++;
                        ex.col = 1;
                        ex.subTotal = 0;


                        setCellValue(sheet, ex.row, ex.col++, op1, style);
                        setCellValue(sheet, ex.row, ex.col++, /*TODO app id*/"APP ID", style);
                        setCellValue(sheet, ex.row, ex.col++, /*TODO service name*/svc, style);

                        lst.stream().filter(d -> d.getId().getOption1().equals(op1) && d.getId().getSvcId().equals(svc)).forEach(d -> {
                            setCellValue(sheet, ex.row, ex.col++, d.getCount() + "", style);
                            ex.subTotal += d.getCount();
                        });

                        //소계
                        setCellValue(sheet, ex.row, ex.col, ex.subTotal + "", style);

                        ex.total += ex.subTotal;

                    });
                    //합계 - 서비스가 여러개인 경우 == 전체
                    if (svcs.size() > 1) {

                        //합계
                        mergeEx.col = ++ex.col;
                        sheet.addMergedRegion(new CellRangeAddress(mergeEx.row, ex.row, mergeEx.col, mergeEx.col));
                        setCellValue(sheet, mergeEx.row, mergeEx.col, ex.total + "", style);
                        setCellStyle(sheet, mergeEx.row, ex.row, mergeEx.col, mergeEx.col, style);

                        //APP 명 merge
                        sheet.addMergedRegion(new CellRangeAddress(mergeEx.row, ex.row, 1, 1));

                        //APP ID merge
                        sheet.addMergedRegion(new CellRangeAddress(mergeEx.row, ex.row, 2, 2));

                        mergeEx.row = ex.row + 1;
                    }

                });

                break;

            case API:
                //서비스 - API 명 ------------------------------- 합계
                List<SvcOption1RC> lstApis = (List<SvcOption1RC>) dataLst;

                svcs.stream().forEach(svc->{
                    opt1.stream().forEach(op1 -> {

                        //한 행이 돌고 초기화
                        ex.total = 0;
                        ex.row++;
                        ex.col = 1;
                        mergeEx.row = ex.row;

                        setCellValue(sheet, ex.row, ex.col++, /*TODO service name*/svc, style);
                        setCellValue(sheet, ex.row, ex.col++, op1, style); //TODO API 명

                        lstApis.stream().filter(d -> d.getId().getOption1().equals(op1) && d.getId().getSvcId().equals(svc)).forEach(d -> {
                            setCellValue(sheet, ex.row, ex.col++, d.getCount() + "", style);
                            ex.total += d.getCount();
                        });

                        //합계 -
                        setCellValue(sheet, ex.row, ex.col, ex.total+"", style);
                    });

                    //서비스 컬럼 병합
                    sheet.addMergedRegion(new CellRangeAddress(mergeEx.row, ex.row, 1/*서비스 컬럼*/, 1));

                });



                break;
            case ERROR:

                //HADER : ERROR CODE - ------------------------------- 합계
                List<SvcOption1RC> lstErrs = (List<SvcOption1RC>) dataLst;

                opt1.stream().forEach(op1->{
                    svcs.stream().forEach(svc->{

                        //한 행이 돌고 초기화
                        ex.subTotal = 0;
                        ex.row++;
                        ex.col = 1;
                        mergeEx.row = ex.row;

                        setCellValue(sheet, ex.row, ex.col++, op1 ,style); //에러 코드

                        setCellValue(sheet, ex.row, ex.col++, /*TODO service name*/svc ,style);

                        lstErrs.stream().filter( d-> d.getId().getOption1().equals(op1) && d.getId().getSvcId().equals(svc)).forEach(d -> {
                            setCellValue(sheet, ex.row, ex.col++, d.getCount() + "", style);
                            ex.subTotal += d.getCount();

                        });

                        //합계 - 에러 상세코드가 빠지면서 소계 제외
                        setCellValue(sheet, ex.row, ex.col, ex.subTotal+"", style);

                        ex.total += ex.subTotal;

                    });


                    //TODO 합계 - 병합된  Error code 단위의 합계 필요 - 서비스가 전체인 경우
                    if(svcs.size() > 1 ) {
                        sheet.addMergedRegion(new CellRangeAddress(mergeEx.row, ex.row, 1/*Error code column*/, 1));

                        //합계
                        mergeEx.col = ex.col++;
                        sheet.addMergedRegion(new CellRangeAddress(mergeEx.row, ex.row, mergeEx.col/*Error code column*/, mergeEx.col));
                        setCellValue(sheet, mergeEx.row, mergeEx.col, ex.total + "", style);

                        ex.total = 0;


                    }

                });


                break;
            case APP_API:
                //HEADER : APP 명 - APP ID - API 명 -------------------------소계 - 합계
                //app 명, app id 병합
                List<SvcOption2RC> lstAppApi = (List<SvcOption2RC>) dataLst;


                opt1.stream().forEach(op1 -> {

                    mergeEx.row = ex.row + 1;

                    opt2.stream().forEach(op2 -> {

                        //한 행이 돌고 초기화
                        ex.subTotal = 0;
                        ex.row++;
                        ex.col = 1;

                        setCellValue(sheet, ex.row, ex.col++, op1 /*TODO APP 명*/, style);
                        setCellValue(sheet, ex.row, ex.col++, "APP ID" /*TODO app id */, style);
                        setCellValue(sheet, ex.row, ex.col++, /*TODO API 명*/op2, style);

                        //check Service - lstAppApi에 이미 service가 걸러진 채로 들어옴
                        for (LocalDate date=startDate; date.isBefore(endDate) || date.isEqual(endDate); date=(rcType==enumRCType.daily?date.plusDays(1):date.plusMonths(1)) ){

                            final LocalDate finalDate = date;

                            //날짜가 없는 경우 디폴트로 0이 들어가게
                            setCellValue(sheet, ex.row, ex.col, "0", style);

                            lstAppApi.stream().filter(d -> d.getId().getOption1().equals(op1)
                                            && d.getId().getOption2().equals(op2) &&
                                            d.getId().getReqDt().equals(finalDate.toDate())
                            ).forEach(d -> {

                                setCellValue(sheet, ex.row, ex.col, d.getCount() + "", style);
                                ex.subTotal += d.getCount();

                            });

                            ex.col++;

                        }

                        //소계
                        setCellValue(sheet, ex.row, ex.col++, ex.subTotal + "", style);

                        ex.total += ex.subTotal;
                    });

                    logger.debug("ex.row : {}, mergeEx.row : {}", ex.row, mergeEx.row);
                    //합계
                    if ( ex.row > mergeEx.row ){
                        sheet.addMergedRegion(new CellRangeAddress(mergeEx.row, ex.row, 1/*APP NAME column*/, 1));
                        sheet.addMergedRegion(new CellRangeAddress(mergeEx.row, ex.row, 2/*APP ID column*/, 2));

                        //합계
                        mergeEx.col = ex.col++;
                        sheet.addMergedRegion(new CellRangeAddress(mergeEx.row, ex.row, mergeEx.col/*total sum column*/, mergeEx.col));
                        setCellValue(sheet, mergeEx.row, mergeEx.col, ex.total + "", style);

                        //테두리
                        setCellStyle(sheet, mergeEx.row, ex.row, mergeEx.col, mergeEx.col, style);

                        ex.total = 0;
                    }
                });

                break;


            case API_APP:
                //HEADER : 서비스 - API 명 - APP 명 - APP ID-------------------------소계 - 합계
                // 서비스 병합, API 명 병합
                List<SvcOption2RC> lstApiApp = (List<SvcOption2RC>) dataLst;

                createAppOption2(sheet, svcs, lstApiApp, opType, rcType);

                break;
            case ERROR_APP:
                //HEADER : ERROR code - APP 명 - APP ID -----------------------  소계 - 합계
                //error code 병합

                List<SvcOption2RC> lstErrApp = (List<SvcOption2RC>) dataLst;
                createErrorOption2(sheet, lstErrApp, opt1, opt2, opType, rcType);

                break;

            case ERROR_API:
                //HEADER : ERROR code - API 명 -----------------------  소계 - 합계
                //error code 병합
                List<SvcOption2RC> lstErrApi = (List<SvcOption2RC>) dataLst;

                createErrorOption2(sheet, lstErrApi, opt1, opt2, opType, rcType);

                break;

        }
    }

    private void createAppOption2(Sheet sheet, List<String> lstSvc, List<SvcOption2RC> lst, enumOptionType opType, enumRCType rcType ) {

        final int[] mergeSvcRow = {4};


        lst.stream().forEach(l-> {
            logger.debug("{}", l);
        });

        Set lstOp1 = Sets.newHashSet();
        Set lstOp2 = Sets.newHashSet();

        lstSvc.stream().forEach(svc-> {

            logger.debug("=========================== {}", svc);
            lst.stream().filter(rc -> rc.getId().getSvcId().equals(svc)).map(rc -> rc.getId().getOption1()).
                    forEach(op1-> { lstOp1.add(op1); });

            lst.stream().filter(rc -> rc.getId().getSvcId().equals(svc)).map(rc -> rc.getId().getOption2()).forEach(op2-> lstOp2.add(op2));

            mergeSvcRow[0] = createApiOption2(sheet, svc, mergeSvcRow[0], lst, lstOp1, lstOp2, opType, rcType);


        });

    }
    private int createApiOption2(Sheet sheet, String svc, int mergeSvcRow, List<SvcOption2RC> lst, Set<String> opt1, Set<String> opt2, enumOptionType opType, enumRCType rcType ){

        CellStyle style = getDataStyle();

        ExcelRow ex = new ExcelRow(mergeSvcRow, 1, 0);

        ExcelRow mergeEx = new ExcelRow(mergeSvcRow, 1, 0);


        Map<Date, Long> map = Maps.newTreeMap();

        opt1.stream().forEach(op1 -> { //API PATH

            mergeEx.row = ex.row;

            opt2.stream().forEach(op2 -> { //APP KEY

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


    private void createErrorOption2(Sheet sheet, List<SvcOption2RC> lstErr, List<String> opt1, List<String> opt2, enumOptionType opType, enumRCType rcType ){

        CellStyle style = getDataStyle();

        ExcelRow ex = new ExcelRow(4, 1, 0);

        ExcelRow mergeEx = new ExcelRow(4, 1, 0);


        Map<Date, Long> map = Maps.newTreeMap();

        opt1.stream().forEach(op1 -> { //ERROR CODE

            mergeEx.row = ex.row;

            opt2.stream().forEach(op2 -> { //API/APP NAME

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
    }

    @Override
    public void createData(String sheetName, Object object) {

    }



}
