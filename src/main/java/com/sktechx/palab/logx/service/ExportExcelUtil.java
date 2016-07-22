package com.sktechx.palab.logx.service;

import com.sktechx.palab.logx.model.ServiceRequestCall;
import com.sktechx.palab.logx.model.SvcOption1RC;
import com.sktechx.palab.logx.model.enumOptionType;
import com.sktechx.palab.logx.model.enumRCType;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
                             LocalDate start, LocalDate end, Map<Integer, String> headers){

        CellStyle headerStyle = getHeaderStyle2();

        Sheet sh = addSheet(sheetName);

        /* 조회하려는 달의 갯수 또는 일의 갯수*/
        int colNum = 0;

        int dataStartCol = headers.size() + 1;


//        LocalDate start = LocalDate.parse(startDate, DateTimeFormat.forPattern("yyyyMMdd"));
//        LocalDate end = LocalDate.parse(endDate, DateTimeFormat.forPattern("yyyyMMdd"));
//        LocalDate date = new LocalDate(startDate);


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
        if ( dataStartCol > 2 ){

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
        public ExcelRow(int row, int col, long total){
            this.row = row; this.col=col; this.total=total;
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


    /*
    nDataCnt : 엑셀의 기간 갯수 - 실제 데이터 갯수
     */

    public void createDate2(String sheetName, Object dataLst, enumOptionType opType, List<String> opt1, List<String> opt2, List<String> svcs){


        Sheet sheet = getSheet(sheetName);
        if ( sheet == null ) {
            logger.error("excel sheet is null");
            throw new NullPointerException("Excel sheet is null!");
        }

        CellStyle style = getDataStyle();

        ExcelRow ex = new ExcelRow(4, 1, 0);

        switch(opType){
            case APP:
                List<SvcOption1RC> lst = (List<SvcOption1RC>) dataLst;

                opt1.stream().forEach(op1 -> {
                    svcs.stream().forEach(svc -> {

                        setCellValue(sheet, ex.row, ex.col++, op1, style);
                        setCellValue(sheet, ex.row, ex.col++, /*TODO app id*/"APP ID", style);
                        setCellValue(sheet, ex.row, ex.col++, /*TODO service name*/svc, style);

                        lst.stream().filter(d -> d.getId().getOption1().equals(op1) && d.getId().getSvcId().equals(svc)).forEach(d -> {
                            setCellValue(sheet, ex.row, ex.col++, d.getCount() + "", style);
                            ex.total += d.getCount();
                        });

                        //합계 - app만 기준1인 경우 소계가 없음
                        setCellValue(sheet, ex.row, ex.col, ex.total + "", style);

                        //한 행이 돌고 초기화
                        ex.total = 0;
                        ex.row++;
                        ex.col = 1;

                    });
                });

                //TODO 앱명과 앱 id 머지 필요
                break;

            case API:

                List<SvcOption1RC> lstApis = (List<SvcOption1RC>) dataLst;

                svcs.stream().forEach(svc->{
                    opt1.stream().forEach(op1->{

                        setCellValue(sheet, ex.row, ex.col++, /*TODO service name*/svc ,style);
                        setCellValue(sheet, ex.row, ex.col++, op1 ,style); //TODO API 명

                        lstApis.stream().filter( d-> d.getId().getOption1().equals(op1) && d.getId().getSvcId().equals(svc)).forEach(d -> {
                            setCellValue(sheet, ex.row, ex.col++, d.getCount() + "", style);
                            ex.total += d.getCount();
                        });

                        //합계 - app만 기준1인 경우 소계가 없음
                        setCellValue(sheet, ex.row, ex.col, ex.total+"", style);

                        //한 행이 돌고 초기화
                        ex.total = 0;
                        ex.row++;
                        ex.col = 1;

                    });
                });

                break;
            case ERROR:

                List<SvcOption1RC> lstErrs = (List<SvcOption1RC>) dataLst;

                opt1.stream().forEach(op1->{
                    svcs.stream().forEach(svc->{
                        setCellValue(sheet, ex.row, ex.col++, /*TODO service name*/svc ,style);

                        setCellValue(sheet, ex.row, ex.col++, op1 ,style); //에러 코드

                        lstErrs.stream().filter( d-> d.getId().getOption1().equals(op1) && d.getId().getSvcId().equals(svc)).forEach(d -> {
                            setCellValue(sheet, ex.row, ex.col++, d.getCount() + "", style);
                            ex.total += d.getCount();
                        });

                        //합계 - 에러 상세코드가 빠지면서 소계 제외
                        setCellValue(sheet, ex.row, ex.col, ex.total+"", style);

                        //한 행이 돌고 초기화
                        ex.total = 0;
                        ex.row++;
                        ex.col = 1;

                    });

                    //서비스 컬럼 병합 - 같은 서비스 끼리 병합함
                    sheet.addMergedRegion(new CellRangeAddress(4, ex.row-1, 2, 2));

                });


                break;
            case APP_API:
                //HEADER : APP 명 - APP ID - API 명 -------------------------소계 - 합계
                //app 명, app id 병합
                break;
            case API_APP:
                //HEADER : 서비스 - API 명 - APP 명 - APP ID-------------------------소계 - 합계
                // 서비스 병합, API 명 병합
                break;
            case ERROR_APP:
                //HEADER : ERROR code - APP 명 - APP ID -----------------------  소계 - 합계
                //error code 병합
                break;

            case ERROR_API:
                //HEADER : ERROR code - API 명 -----------------------  소계 - 합계
                //error code 병합

                break;

        }
    }

    public void createDataForSvcAppPV(String sheetName, List<SvcOption1RC> lst, int nDataCnt, List<String> appIds, List<String> svcIds) {

        Sheet sheet = getSheet(sheetName);
        if ( sheet == null ) {
            logger.error("excel sheet is null");
            throw new NullPointerException("Excel sheet is null!");
        }

        CellStyle style = getDataStyle();

        //header 마지막 컬럼
        final Integer[] nRow = {3};
        final Integer[] nCol = {1};
        final Integer[] nIdx = {0};

        Long[] total = new Long[appIds.size()];

        for(int j = 0 ; j < appIds.size() ; j++ ){
            total[j]=0l;
        }

        final Integer[] nMergeStartRow = {nRow[0]+1};



        appIds.stream().forEach(app->{
            svcIds.stream().forEach(svc -> {

                setCellValue(sheet, ++nRow[0], nCol[0], app, style);
                setCellValue(sheet, nRow[0], ++nCol[0], svc, style);

                lst.stream().filter(rc -> rc.getId().getOption1().equals(app) && rc.getId().getSvcId().equals(svc)).forEach(r -> {

                    total[nIdx[0]] += r.getCount();

                    setCellValue(sheet, nRow[0], ++nCol[0], r.getCount() + "", style);

                    logger.debug("row-col-value: {}-{}-{}", nRow[0], nCol[0], r.getCount());

                    //합계 소계
                    //컬럼이 데이터의 마지막 컬럼 온 경우
                    if (nCol[0] == nDataCnt + 2) {


                        setCellValue(sheet, nRow[0], ++nCol[0], total[nIdx[0]] + "", style);
                        logger.debug("row-col-total: {}-{}-{}", nRow[0], nCol[0], total[nIdx[0]]);

                        //cell 서식(테두리를 위해) 더미 데이터 설정
                        setCellValue(sheet, nRow[0], ++nCol[0], total[nIdx[0]] + "", style);


                        nIdx[0]++;

                        nCol[0] = 1;
                    }

                });
            });

            long sum = 0;

            nIdx[0] = 0;

            for ( int i = 0 ; i < total.length ; i++ ) {

                sum += total[i];

                total[i]=0l;
            }

            logger.debug("nDataCnt : {}", nDataCnt);

            sheet.autoSizeColumn(1);

            //합계 컬럼 병합
            sheet.addMergedRegion(new CellRangeAddress(nMergeStartRow[0], nRow[0], nDataCnt + 4, nDataCnt + 4)); //columns => service/app/api

            setCellValue(sheet, nMergeStartRow[0], nDataCnt + 4, sum + "", style);

            //app id 컬럼 병함
            sheet.addMergedRegion(new CellRangeAddress(nMergeStartRow[0], nRow[0], 1, 1)); //columns => service/app/api
            nMergeStartRow[0] = nRow[0] + 1;

        });

    }

    @Override
    public void createData(String sheetName, Object object) {

    }



}
