package com.sktechx.palab.logx.repository;

import com.sktechx.palab.logx.model.ErrorCount;
import com.sktechx.palab.logx.model.ErrorCountPK;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by 1002382 on 2016. 7. 21..
 */
public interface ErrorCountRepository extends JpaRepository<ErrorCount, ErrorCountPK>{

}
