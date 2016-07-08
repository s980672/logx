package com.sktechx.palab.logx.service;

import com.sktechx.palab.logx.model.ServiceRequestCall;
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

import java.util.ArrayList;
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
            setCellValue(sh, 3, dataStartCol + i, start.plusDays(i).getDayOfMonth() + "일", headerStyle );

            setCellValue(sh, 2, dataStartCol + i, start.toString(dateFormat), headerStyle );

            //first column - table name
            setCellValue(sh, 1, dataStartCol + i, tableName, headerStyle);
        }

        setCellValue(sh, 1, dataStartCol+colNum + 1, tableName, headerStyle);
        setCellValue(sh, 2, dataStartCol+colNum + 1, "합계", headerStyle);
        setCellValue(sh, 3, dataStartCol+colNum + 1, "합계", headerStyle);

        sh.addMergedRegion(new CellRangeAddress(2, 3, dataStartCol+colNum + 1, dataStartCol+colNum + 1));

        headers.forEach( (col, h)->{

            setCellValue(sh, 1, col, h, headerStyle); //service/app/api
            setCellValue(sh, 2, col, h, headerStyle); //service/app/api
            setCellValue(sh, 3, col, h, headerStyle); //service/app/api


        });

        for(int i = 1 ; i < dataStartCol ; i++ ){

            sh.addMergedRegion(new CellRangeAddress(1, 3, i, i)); //columns => service/app/api

            sh.autoSizeColumn(i);
        }


        sh.addMergedRegion(new CellRangeAddress(1, 1, dataStartCol, dataStartCol+colNum + 1)); //첫번째 row => 서비스별 Request Count
        sh.addMergedRegion(new CellRangeAddress(2, 2, dataStartCol, dataStartCol+colNum)); //두번째 row => 2015년 or 2015년 7월



    }



    public void createDataForSvcPV(String sheetName, List<ServiceRequestCall> lst, List<String> lstSvc) {

        Sheet sheet = getSheet(sheetName);

        CellStyle style = getDataStyle();



        final Integer[] nRow = {3};
        final Integer[] nCol = {1};
        final Long[] total = {0l};


        //row당 data 갯수
        int nPeriod = lst.size()/lstSvc.size();

        lstSvc.stream().forEach(s -> {


            nCol[0]=1;

            setCellValue(sheet, ++nRow[0], nCol[0], s, style);

            lst.stream().filter(r -> r.getId().getSvcId().equals(s)).forEach(r -> {


                total[0] += r.getCount();

                setCellValue(sheet, nRow[0], ++nCol[0], r.getCount() + "", style);

                logger.debug("row-col-value: {}-{}-{}", nRow[0], nCol[0], r.getCount());

                //TODO +1은 헤더가 서비스 만 있을 경우임 변수화 필요
                if ( nCol[0] == nPeriod+1 ){

                    logger.debug("row-col-total: {}-{}-{}", nRow[0], ++nCol[0], total[0]);

                    setCellValue(sheet, nRow[0], nCol[0] , total[0]+"", style);

                    total[0]=0l;
                }

            });
        });
    }


    @Override
    public void createData(String sheetName, Object object) {

        Sheet sheet = getSheet(sheetName);

        CellStyle style = getDataStyle();

        int nRow = 4;
        int nCol = 1;
        List<ServiceRequestCall> lst = (ArrayList< ServiceRequestCall >) object;


    }


}
