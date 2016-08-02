package com.sktechx.palab.logx.web;

import com.sktechx.palab.logx.model.enumRCType;
import com.sktechx.palab.logx.repository.RequestCallRepository;
import com.sktechx.palab.logx.service.StatisticsExcelExportService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
                                   @RequestParam String startDate, @RequestParam String endDate, HttpServletResponse response) throws IOException {


        logger.debug("pvOrUv : {} ", pvOrUv);
        logger.debug("service : {} ", service);
        logger.debug("option1 : {} option2: {}", option1, option2);
        logger.debug("startDate : {} endDate: {}", startDate, endDate);

        enumRCType rcType = enumRCType.valueOf(period);

        XSSFWorkbook workbook = statisticsExcelExportService.exportExcel(service, option1, option2, rcType, startDate, endDate, pvOrUv.equals("pv"));

        //오늘 일자
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

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
