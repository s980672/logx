package com.sktechx.palab.logx.secondary.service;

import com.sktechx.palab.logx.secondary.domain.SvcIdCall;
import com.sktechx.palab.logx.secondary.domain.SvcServiceIDRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by 1100299 on 2016-09-09.
 */
@Service
public class CategoryService {
    Logger logger = LoggerFactory.getLogger(CategoryService.class);

    @Autowired
    private SvcServiceIDRepository svcIDRepository;

    public CategoryService(){}

    public String getServiceId(String categoryId){
        SvcIdCall distinctByCategoryKey = svcIDRepository.findDistinctByCategoryKey(categoryId);
        if ( distinctByCategoryKey != null )
            return distinctByCategoryKey.getSvcId();

        logger.error("categoryId({}) is invalid!", categoryId);
        return "ERROR";

    }
}
