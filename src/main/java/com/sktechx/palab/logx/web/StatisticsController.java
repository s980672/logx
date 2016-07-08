package com.sktechx.palab.logx.web;

import com.sktechx.palab.logx.model.RequestCall;
import com.sktechx.palab.logx.model.RequestCallPK;
import com.sktechx.palab.logx.model.enumRCType;
import com.sktechx.palab.logx.repository.RequestCallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created by sunny on 2016. 7. 7..
 */
@RestController
@RequestMapping("/stats")
public class StatisticsController {

    @Autowired
    RequestCallRepository rcRepo;


    //period = {monthly, daily}
    @RequestMapping(value="/{period}/pv", method= RequestMethod.GET)
    public List<RequestCall> getPV(@PathVariable String period, @RequestParam Integer year, @RequestParam(required = false) Integer month) {

        //period가 monthly이면 다음 파라미터가 년도가 들어와서 한해의 달 간격의 pv가 조회된다
        //period가 daily이면 다음 파라미터가 달이 들어와서 한달 간의 매일 간격의 pv가 조회된다
        enumRCType rcType = enumRCType.valueOf(period);

        String date = year + "-" + month;

        RequestCallPK key = new RequestCallPK(rcType, new Date(date));

        return rcRepo.findByKey(key);
    }



}
