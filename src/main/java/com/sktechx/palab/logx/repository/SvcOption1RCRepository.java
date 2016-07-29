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

    @Query("select rc from SvcOption1RC rc where rc.id.rcType = :rcType and rc.id.reqDt between :start and :end order by rc.id.reqDt")
    List<SvcOption1RC> findByRcTypeAndBetween(@Param("rcType")enumRCType rcType, @Param("start")Date start, @Param("end")Date end);

    @Query("select distinct rc.id.svcId from SvcOption1RC rc where rc.id.rcType= :rcType order by rc.id.svcId")
    List<String> findDistinctSvcIdByRcType(@Param("rcType")enumRCType rcType);

    @Query("select distinct rc.id.option1 from SvcOption1RC rc where rc.id.rcType= :rcType order by rc.id.option1")
    List<String> findDistinctOption1ByRcType(@Param("rcType") enumRCType rcType);

    List<SvcOption1RC> findAll();

    @Query("select rc from SvcOption1RC rc where rc.id.opType=:opType and rc.id.rcType = :rcType and rc.id.reqDt between :start and :end order by rc.id.reqDt")
    List<SvcOption1RC> findByOpTypeAndRcTypeAndBetween(@Param("opType") enumOptionType opType, @Param("rcType")enumRCType rcType, @Param("start")Date start, @Param("end")Date end);

    @Query("select distinct rc.id.option1 from SvcOption1RC rc where rc.id.opType=:opType and rc.id.rcType= :rcType order by rc.id.option1")
    List<String> findDistinctOption1ByRcTypeAndOpType(@Param("rcType")enumRCType rcType, @Param("opType") enumOptionType opType);
}
