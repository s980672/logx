package com.sktechx.palab.logx.web;

import com.sktechx.palab.logx.model.enumRCType;
import com.sktechx.palab.logx.repository.RequestCallRepository;
import com.sktechx.palab.logx.service.StatisticsExcelExportService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

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
    StatisticsExcelExportService statisticsExcelExportService;

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
            start = start.withDayOfYear(year);
            start = start.withMonthOfYear(month);
            start = start.withDayOfMonth(1);

            end = start.plusMonths(1).minusDays(1);
        }else{
            start = start.withDayOfYear(year);
            start = start.withMonthOfYear(1);
            start = start.withDayOfMonth(1);

            end = start.plusYears(1).minusDays(1);
        }

        logger.debug("start : {} - end: {}", start, end );

        XSSFWorkbook workbook = statisticsExcelExportService.exportExcel(service, option1, option2, rcType, start, end, pvOrUv.equals("pv"));

        //오늘 일자
        String date = LocalDate.now().toString("yyyyMMdd");
                //format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        String excelFileName = "stat_pv" + (StringUtils.isEmpty(service) ? "" : "_" + service)
                + (StringUtils.isEmpty(option1) ? "" : "_" + option1)
                + (StringUtils.isEmpty(option2) ? "" : "_" + option2) + "_" + date + ".xlsx";

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
