package com.sktechx.palab.logx.repository;

import com.sktechx.palab.logx.model.SvcOption2RC;
import com.sktechx.palab.logx.model.SvcOption2RCPK;
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
public interface SvcOption2RCRepository extends JpaRepository<SvcOption2RC, SvcOption2RCPK>{

    @Query("select rc from SvcOption2RC rc where rc.id.rcType = :rcType and rc.id.reqDt between :start and :end order by rc.id.reqDt")
    List<SvcOption2RC> findByRcTypeAndBetween(@Param("rcType") enumRCType rcType, @Param("start") Date start, @Param("end") Date end);

    @Query("select rc from SvcOption2RC rc where rc.id.svcId = :svcId and rc.id.rcType = :rcType and rc.id.reqDt between :start and :end order by rc.id.reqDt")
    List<SvcOption2RC> findBySvcIdAndRcTypeAndBetween(@Param("svcId") String svcId,
    @Param("rcType") enumRCType rcType, @Param("start") Date start, @Param("end") Date end);

    @Query("select rc from SvcOption2RC rc where rc.id.opType=:opType and rc.id.svcId = :svcId and" +
            " rc.id.rcType = :rcType and rc.id.reqDt between :start and :end order by rc.id.reqDt")
    List<SvcOption2RC> findBySvcIdAndOpTypeAndRcTypeAndBetween(@Param("svcId") String svcId, @Param("opType") enumOptionType opType,
                                                      @Param("rcType") enumRCType rcType, @Param("start") Date start, @Param("end") Date end);


    @Query("select distinct rc.id.svcId from SvcOption2RC rc where rc.id.rcType= :rcType order by rc.id.svcId")
    List<String> findDistinctSvcIdByRcType(@Param("rcType") enumRCType rcType);

    @Query("select distinct rc.id.option1 from SvcOption2RC rc where rc.id.rcType= :rcType order by rc.id.option1")
    List<String> findDistinctOption1ByRcType(@Param("rcType") enumRCType rcType);

    List<SvcOption2RC> findAll();

    @Query("select distinct rc.id.option2 from SvcOption2RC rc where rc.id.rcType= :rcType order by rc.id.option2")
    List<String> findDistinctOption2ByRcType(@Param("rcType") enumRCType rcType);

    @Query("select distinct rc.id.svcId from SvcOption2RC rc where rc.id.rcType= :rcType and rc.id.opType=:opType order by rc.id.svcId")
    List<String> findDistinctSvcIdByRcTypeAndOpType(@Param("rcType") enumRCType rcType, @Param("opType") enumOptionType opType );

    @Query("select distinct rc.id.option2 from SvcOption2RC rc where rc.id.rcType= :rcType and rc.id.opType=:opType order by rc.id.option2")
    List<String> findDistinctOption2ByRcTypeAndOpType(@Param("rcType") enumRCType rcType, @Param("opType") enumOptionType opType );

    @Query("select distinct rc.id.option1 from SvcOption2RC rc where rc.id.rcType= :rcType and rc.id.opType=:opType order by rc.id.option1")
    List<String> findDistinctOption1ByRcTypeAndOpType(@Param("rcType") enumRCType rcType, @Param("opType") enumOptionType opType );


    @Query("select rc from SvcOption2RC rc where rc.id.opType=:opType and" +
            " rc.id.rcType = :rcType and rc.id.reqDt between :start and :end order by rc.id.reqDt")
    List<SvcOption2RC> findByOpTypeAndRcTypeAndBetween(@Param("opType") enumOptionType opType,
                                                               @Param("rcType") enumRCType rcType, @Param("start") Date start, @Param("end") Date end);

    @Query("select distinct rc.id.option1 from SvcOption2RC rc where rc.id.opType=:opType and rc.id.svcId = :svcId and" +
            " rc.id.rcType = :rcType and rc.id.reqDt between :start and :end")
    List<String> findDistinctOption1BySvcIdAndRcTypeAndOpTypeAndBetween(@Param("svcId") String svcId, @Param("rcType") enumRCType rcType, @Param("opType") enumOptionType opType,
                                                                 @Param("start") Date start, @Param("end") Date end);

    @Query("select distinct rc.id.option2 from SvcOption2RC rc where rc.id.opType=:opType and rc.id.svcId = :svcId and" +
            " rc.id.rcType = :rcType and rc.id.reqDt between :start and :end")
    List<String> findDistinctOption2BySvcIdAndRcTypeAndOpTypeAndBetween(@Param("svcId") String svcId, @Param("rcType") enumRCType rcType, @Param("opType") enumOptionType opType,
                                                                        @Param("start") Date start, @Param("end") Date end);

    @Query("select distinct rc.id.option1 from SvcOption2RC rc where rc.id.opType=:opType and " +
            " rc.id.rcType = :rcType and rc.id.reqDt between :start and :end")
    List<String> findDistinctOption1ByRcTypeAndOpTypeAndBetween(@Param("rcType") enumRCType rcType, @Param("opType") enumOptionType opType,  @Param("start") Date start, @Param("end") Date end);

    @Query("select distinct rc.id.svcId from SvcOption2RC rc where rc.id.opType=:opType and " +
            " rc.id.rcType = :rcType and rc.id.reqDt between :start and :end")
    List<String> findDistinctSvcIdByRcTypeAndOpTypeAndBetween(@Param("rcType") enumRCType rcType, @Param("opType") enumOptionType opType,  @Param("start") Date start, @Param("end") Date end);

    @Query("select distinct rc.id.option2 from SvcOption2RC rc where rc.id.opType=:opType and " +
            " rc.id.rcType = :rcType and rc.id.reqDt between :start and :end")
    List<String> findDistinctOption2ByRcTypeAndOpTypeAndBetween(@Param("rcType") enumRCType rcType, @Param("opType") enumOptionType opType,  @Param("start") Date start, @Param("end") Date end);
}
