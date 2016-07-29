package com.sktechx.palab.logx.repository;

import com.sktechx.palab.logx.model.SvcOption2RC;
import com.sktechx.palab.logx.model.SvcOption2RCPK;
import com.sktechx.palab.logx.model.enumRCType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by 1002382 on 2016. 7. 11..
 */
public interface SvcOption2RCRepository extends JpaRepository<SvcOption2RC, SvcOption2RCPK>{

    @Query("select rc from SvcOption2RC rc where rc.id.rcType = :rcType and rc.id.reqDt between :start and :end order by rc.id.reqDt")
    List<SvcOption2RC> findByRcTypeAndBetween(@Param("rcType")enumRCType rcType, @Param("start")Date start, @Param("end")Date end);
//
//    @Query("select distinct rc.id.svcId from SvcOption2RC rc where rc.id.rcType= :rcType order by rc.id.svcId")
//    List<String> findDistinctSvcIdByRcType(@Param("rcType")enumRCType rcType);
//
//    @Query("select distinct rc.id.option1 from SvcOption2RC rc where rc.id.rcType= :rcType order by rc.id.option1")
//    List<String> findDistinctAppIdByRcType(@Param("rcType")enumRCType rcType);
//    

}
