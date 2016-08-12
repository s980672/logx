package com.sktechx.palab.logx.web;

import com.sktechx.palab.logx.model.enumRCType;
import com.sktechx.palab.logx.model.enumStatsType;
import com.sktechx.palab.logx.repository.RequestCallRepository;
import com.sktechx.palab.logx.service.ElasticsearchPVAnalysisService;
import com.sktechx.palab.logx.service.ElasticsearchUVAnalysisService;
import com.sktechx.palab.logx.service.ExportExcelService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;

/**
 * Created by sunny on 2016. 7. 7..
 */
@RestController
@RequestMapping("/stats/")
public class StatisticsController {

    Logger logger = LoggerFactory.getLogger(StatisticsController.class);

    @Autowired
    RequestCallRepository rcRepo;

    @Autowired
    ExportExcelService statisticsExcelExportService;

    @Autowired
    ElasticsearchPVAnalysisService pvSvc;

    @Autowired
    ElasticsearchUVAnalysisService uvSvc;



    //date ==> 2016-07-01
    @RequestMapping(value="{period}", method=RequestMethod.POST)
    public void generateBatchData(@PathVariable String period, @RequestParam(required=false) String date,
                                  @RequestParam(required=false) String startDate, @RequestParam(required=false) String endDate) throws IOException, ParseException {

        enumRCType rcType = enumRCType.valueOf(period);

        if(startDate != null && endDate != null ){
            LocalDate sDate = LocalDate.parse(startDate, DateTimeFormat.forPattern("yyyy-MM-dd"));
            LocalDate eDate = LocalDate.parse(endDate, DateTimeFormat.forPattern("yyyy-MM-dd"));

            if(enumRCType.monthly==rcType) {
                sDate = sDate.withDayOfMonth(1);
            }

            for (LocalDate d=sDate;d.equals(eDate)|| d.isBefore(eDate); d=(rcType==enumRCType.daily? d.plusDays(1):d.plusMonths(1))){

                String date1 = d.toString(DateTimeFormat.forPattern("yyyy-MM-dd"));
                String date2 = date1;
                if(enumRCType.daily==rcType){
                    date2 = d.plusDays(1).toString(DateTimeFormat.forPattern("yyyy-MM-dd"));

                }else{
                    date2 = d.plusMonths(1).toString(DateTimeFormat.forPattern("yyyy-MM-dd"));

                }

                pvSvc.generateAllPV(rcType, date1, date2);
                uvSvc.generateAllUV(rcType, date1, date2);

            }
        }
        else {

            LocalDate tmp = LocalDate.now();
            if (date != null) {
                tmp = LocalDate.parse(date, DateTimeFormat.forPattern("yyyy-MM-dd"));
            }

            LocalDate start = tmp;
            LocalDate end = start;

            if (rcType == null) {
                throw new IllegalArgumentException("Period is one of monthly/daily!!");
            }

            if (rcType == enumRCType.daily) {
                end = start.plusDays(1);
            } else if (rcType == enumRCType.monthly) {
                start = tmp.withDayOfMonth(1);
                end = start.plusMonths(1);
            }

            String date1 = start.toString(DateTimeFormat.forPattern("yyyy-MM-dd"));
            String date2 = end.toString(DateTimeFormat.forPattern("yyyy-MM-dd"));

            logger.debug("start date : {} - end date : {}", date1, date2);

            pvSvc.generateAllPV(rcType, date1, date2);
            uvSvc.generateAllUV(rcType, date1, date2);
        }
    }


    //period = {monthly, daily}
    @RequestMapping(value="{period}/{pvOrUv}", method= RequestMethod.GET)
    public void getStatisticsExcel(@PathVariable String period, @PathVariable String pvOrUv,
                                   @RequestParam String service,
                                   @RequestParam(required = false) String option1,
                                   @RequestParam(required=false) String option2,
                                   @RequestParam Integer year, @RequestParam Integer month, HttpServletResponse response) throws IOException {


        logger.debug("pvOrUv : {} ", pvOrUv);
        logger.debug("service : {} ", service);
        logger.debug("option1 : {} option2: {}", option1, option2);
        logger.debug("year : {} month: {}", year, month);
        logger.debug("monthly or daily : {}", period );

        enumRCType rcType = enumRCType.valueOf(period);

        LocalDate start = new LocalDate();
        LocalDate end;

        if ( rcType == enumRCType.daily ) {
            start = start.withYear(year);
            start = start.withMonthOfYear(month);
            start = start.withDayOfMonth(1);

            end = start.plusMonths(1).minusDays(1);
        }else{
            start = start.withYear(year);
            start = start.withMonthOfYear(1);
            start = start.withDayOfMonth(1);

            end = start.plusYears(1).minusDays(1);
        }

        logger.debug("start : {} - end: {}", start, end );

        pvOrUv = pvOrUv.toUpperCase();

        XSSFWorkbook workbook = statisticsExcelExportService.exportExcel(service, option1, option2, enumStatsType.valueOf(pvOrUv), rcType, start, end);

        //오늘 일자
        String date = LocalDate.now().toString("yyyyMMdd");
        //format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        String excelFileName = "stat-" + pvOrUv + "-"
                + period
                + (StringUtils.isEmpty(option1) ? "" : "-" + option1)
                + (StringUtils.isEmpty(option2) ? "" : "-" + option2)
                + (StringUtils.isEmpty(service) ? "" : "-" + service)
                + "-" + date + ".xlsx";

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + excelFileName);

        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
        workbook.write(outByteStream);
        byte[] outArray = outByteStream.toByteArray();

        response.setContentLength(outArray.length);
        response.setHeader("Expires:", "0"); // eliminates browser caching
        OutputStream outStream = response.getOutputStream();

        outStream.write(outArray);
        outStream.flush();
        response.flushBuffer();
        outStream.close();
        workbook.close();

    }







}
