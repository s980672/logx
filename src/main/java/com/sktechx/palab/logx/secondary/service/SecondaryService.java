package com.sktechx.palab.logx.secondary.service;

import com.google.common.collect.Maps;
import com.sktechx.palab.logx.secondary.domain.SvcIdCall;
import com.sktechx.palab.logx.secondary.domain.SvcServiceIDRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by 1100299 on 2016-09-09.
 */
@Service
@Transactional("secondaryTransactionManager")
public class SecondaryService {
    @Autowired
    private SvcServiceIDRepository svcIDRepository;

    public  SecondaryService(){}

    @PostConstruct
    public Map<String, String> GetServiceId() throws IOException {

        Map<String, String> map = Maps.newTreeMap();

        List<SvcIdCall> rcs = null;
        rcs = this.svcIDRepository.findAll();
        List<SvcIdCall> ListData = (List<SvcIdCall>) rcs;

        for (int i=0; i<ListData.size(); i++){
            map.put(ListData.get(i).getCategoryId(),ListData.get(i).getSvcId());
        }

        return map;

    }
}
