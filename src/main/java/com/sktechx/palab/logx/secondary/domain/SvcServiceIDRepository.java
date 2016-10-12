package com.sktechx.palab.logx.secondary.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by 1002382 on 2016. 7. 20..
 */
@Repository
public interface SvcServiceIDRepository extends JpaRepository<SvcIdCall, String>{

    List<SvcIdCall> findAll();
    SvcIdCall findDistinctByCategoryKey(String categoryKey);

}
