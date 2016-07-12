package com.sktechx.palab.logx.web;

import com.sktechx.palab.logx.model.enumRCType;
import com.sktechx.palab.logx.repository.RequestCallRepository;
import com.sktechx.palab.logx.service.StatisticsExcelExportService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by sunny on 2016. 7. 7..
 */
@RestController
@RequestMapping("/stats/")
public class StatisticsController {

    @Autowired
    RequestCallRepository rcRepo;

    @Autowired
    StatisticsExcelExportService statisticsExcelExportService;

    //period = {monthly, daily}
    @RequestMapping(value="{period}/pv", method= RequestMethod.GET)
    public void getExcelForPV(@PathVariable String period,@RequestParam String service,
                                   @RequestParam(required = false) String option1,
                                   @RequestParam(required=false) String option2,
                                   @RequestParam String startDate, @RequestParam String endDate, HttpServletResponse response) throws IOException {


        enumRCType rcType = enumRCType.valueOf(period);

        XSSFWorkbook workbook = statisticsExcelExportService.exportExcel(service, option1, option2, rcType, startDate, endDate, true);

        Calendar today = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = sdf.format(today.getTime());

        String excelFileName = "stat_pv" + (StringUtils.isEmpty(service)?"":"_" + service )
                + (StringUtils.isEmpty(option1)?"":"_" + option1 )
                + (StringUtils.isEmpty(option2)?"":"_" + option2 ) + "_"+ date+".xlsx";

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=" + excelFileName);

        ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
        workbook.write(outByteStream);
        byte [] outArray = outByteStream.toByteArray();

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
