package com.sktechx.palab.logx.repository;

import com.sktechx.palab.logx.model.SvcOption1RC;
import com.sktechx.palab.logx.model.SvcOption1RCPK;
import com.sktechx.palab.logx.model.enumOptionType;
import com.sktechx.palab.logx.model.enumRCType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by 1002382 on 2016. 7. 11..
 */
public interface SvcOption1RCRepository extends JpaRepository<SvcOption1RC, SvcOption1RCPK>{

    List<SvcOption1RC> findAll();

    @Query("select rc from SvcOption1RC rc where rc.id.opType=:opType and rc.id.rcType = :rcType and rc.id.reqDt between :start and :end order by rc.id.reqDt")
    List<SvcOption1RC> findByOpTypeAndRcTypeAndBetween(@Param("opType") enumOptionType opType, @Param("rcType")enumRCType rcType, @Param("start")Date start, @Param("end")Date end);

    @Query("select rc from SvcOption1RC rc where rc.id.svcId=:svcId and rc.id.opType=:opType and rc.id.rcType = :rcType and rc.id.reqDt between :start and :end order by rc.id.reqDt")
    List<SvcOption1RC> findBySvcIdAndOpTypeAndRcTypeAndBetween(@Param("svcId")String svc, @Param("opType") enumOptionType opType, @Param("rcType")enumRCType rcType, @Param("start")Date start, @Param("end")Date end);

}
