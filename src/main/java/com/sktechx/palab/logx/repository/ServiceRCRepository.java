package com.sktechx.palab.logx.repository;

import com.sktechx.palab.logx.model.ServiceRCPK;
import com.sktechx.palab.logx.model.ServiceRequestCall;
import com.sktechx.palab.logx.model.enumRCType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by 1002382 on 2016. 7. 6..
 */
public interface ServiceRCRepository extends JpaRepository<ServiceRequestCall, ServiceRCPK> {

    @Query("select rc from ServiceRequestCall rc where rc.id.rcType = :rcType and rc.id.reqDt between :start and :end order by rc.id.reqDt")
    List<ServiceRequestCall> findByRcTypeAndBetweenDates(@Param("rcType") enumRCType rcType, @Param("start") Date start, @Param("end") Date end);

    @Query("select distinct rc.id.svcId from ServiceRequestCall rc")
    List<String> findDistinctSvcId();

    List<ServiceRequestCall> findAll();

    @Query("select rc from ServiceRequestCall rc where rc.id.svcId = :svcId and rc.id.rcType = :rcType and rc.id.reqDt between :start and :end order by rc.id.reqDt")
    List<ServiceRequestCall> findBySvcIdAndRcTypeAndBetween(@Param("svcId")String svcId, @Param("rcType") enumRCType rcType, @Param("start") Date start, @Param("end") Date end);

    @Query("select distinct rc.id.svcId from ServiceRequestCall rc where rc.id.rcType = :rcType")
    List<String> findDistinctSvcIdByRcType(@Param("rcType") enumRCType rcType);
}
