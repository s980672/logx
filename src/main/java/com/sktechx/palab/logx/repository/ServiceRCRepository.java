package com.sktechx.palab.logx.repository;

import com.sktechx.palab.logx.model.ServiceRCPK;
import com.sktechx.palab.logx.model.ServiceRequestCall;
import com.sktechx.palab.logx.model.enumRCType;
import com.sktechx.palab.logx.model.enumStatsType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by 1002382 on 2016. 7. 6..
 */
public interface ServiceRCRepository extends JpaRepository<ServiceRequestCall, ServiceRCPK> {

    @Query("select rc from ServiceRequestCall rc where rc.id.stsType=:stsType and rc.id.rcType = :rcType and rc.id.reqDt between :start and :end order by rc.id.reqDt")
    List<ServiceRequestCall> findByStsTypeAndRcTypeAndBetween(@Param("stsType") enumStatsType stsType, @Param("rcType") enumRCType rcType, @Param("start") Date start, @Param("end") Date end);

    List<ServiceRequestCall> findAll();

    @Query("select rc from ServiceRequestCall rc where rc.id.svcId = :svcId and rc.id.stsType=:stsType and rc.id.rcType = :rcType and rc.id.reqDt between :start and :end order by rc.id.reqDt")
    List<ServiceRequestCall> findBySvcIdAndStsTypeAndRcTypeAndBetween(@Param("svcId")String svcId, @Param("stsType") enumStatsType stsType,  @Param("rcType") enumRCType rcType, @Param("start") Date start, @Param("end") Date end);

    @Query("select NEW ServiceRequestCall(rc.id.stsType, rc.id.rcType, rc.id.reqDt, rc.id.svcId, '', sum(rc.count)) from ServiceRequestCall rc where rc.id.svcId = :svcId and rc.id.stsType=:stsType " +
            "and rc.id.rcType = :rcType and rc.id.reqDt between :start and :end group by rc.id.svcId, rc.id.reqDt order by rc.id.reqDt")
    List<ServiceRequestCall> findSumGroupBySvcIdBySvcIdAndStsTypeAndRcTypeAndBetween(@Param("svcId")String svcId, @Param("stsType") enumStatsType stsType,  @Param("rcType") enumRCType rcType, @Param("start") Date start, @Param("end") Date end);

    @Query("select NEW ServiceRequestCall(rc.id.stsType, rc.id.rcType, rc.id.reqDt, rc.id.svcId, '', sum(rc.count)) from ServiceRequestCall rc" +
            " where rc.id.stsType=:stsType and rc.id.rcType = :rcType and rc.id.reqDt between :start and :end " +
            "group by rc.id.svcId, rc.id.reqDt order by rc.id.reqDt")
    List<ServiceRequestCall> findSumGroupBySvcIdByStsTypeAndRcTypeAndBetween(@Param("stsType") enumStatsType stsType,  @Param("rcType") enumRCType rcType, @Param("start") Date start, @Param("end") Date end);

    @Query("select rc.id.svcId from ServiceRequestCall rc where rc.id.stsType='PV' and rc.id.rcType = :rcType and rc.id.reqDt between :start and :end " +
            " group by rc.id.svcId order by sum(rc.count) desc")
    List<String> findTop5Svc(@Param("rcType")enumRCType rcType, @Param("start") Date start, @Param("end") Date end);



}
