package com.sktechx.palab.logx.repository;

import com.sktechx.palab.logx.model.*;
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

    @Query("select rc from SvcOption1RC rc where rc.id.stsType=:stsType and rc.id.opType=:opType and rc.id.rcType = :rcType and rc.id.reqDt between :start and :end order by rc.id.reqDt")
    List<SvcOption1RC> findByStsTypeAndOpTypeAndRcTypeAndBetween(@Param("stsType") enumStatsType stsType, @Param("opType") enumOptionType opType, @Param("rcType")enumRCType rcType, @Param("start")Date start, @Param("end")Date end);

    @Query("select rc from SvcOption1RC rc where rc.id.svcId=:svcId and rc.id.stsType=:stsType and rc.id.opType=:opType and rc.id.rcType = :rcType and rc.id.reqDt between :start and :end order by rc.id.reqDt")
    List<SvcOption1RC> findBySvcIdAndStsTypeAndOpTypeAndRcTypeAndBetween(@Param("svcId")String svc, @Param("stsType") enumStatsType stsType, @Param("opType") enumOptionType opType, @Param("rcType")enumRCType rcType, @Param("start")Date start, @Param("end")Date end);

}
