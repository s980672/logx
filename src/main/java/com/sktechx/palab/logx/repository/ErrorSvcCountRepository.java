package com.sktechx.palab.logx.repository;

import com.sktechx.palab.logx.model.ErrSvcCntPK;
import com.sktechx.palab.logx.model.ErrSvcCount;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by 1002382 on 2016. 7. 21..
 */
public interface ErrorSvcCountRepository extends JpaRepository<ErrSvcCount, ErrSvcCntPK>{

}
