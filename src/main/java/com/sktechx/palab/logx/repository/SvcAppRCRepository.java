package com.sktechx.palab.logx.repository;

import com.sktechx.palab.logx.model.ServiceRequestCall;
import com.sktechx.palab.logx.model.SvcAppRC;
import com.sktechx.palab.logx.model.SvcAppRCPK;
import com.sktechx.palab.logx.model.enumRCType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by 1002382 on 2016. 7. 11..
 */
public interface SvcAppRCRepository extends JpaRepository<SvcAppRC, SvcAppRCPK>{

    @Query("select rc from SvcAppRC rc where rc.id.rcType = :rcType and rc.id.reqDt between :start and :end order by rc.id.reqDt")
    List<SvcAppRC> findByRcTypeAndBetween(@Param("rcType")enumRCType rcType, @Param("start")Date start, @Param("end")Date end);

    @Query("select distinct rc.id.svcId from SvcAppRC rc where rc.id.rcType= :rcType order by rc.id.svcId")
    List<String> findDistinctSvcIdByRcType(@Param("rcType")enumRCType rcType);

    @Query("select distinct rc.id.appId from SvcAppRC rc where rc.id.rcType= :rcType order by rc.id.appId")
    List<String> findDistinctAppIdByRcType(@Param("rcType")enumRCType rcType);

    List<SvcAppRC> findAll();

}
