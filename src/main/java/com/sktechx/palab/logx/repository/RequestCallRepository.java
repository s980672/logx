package com.sktechx.palab.logx.repository;

import com.sktechx.palab.logx.model.ReqCall;
import com.sktechx.palab.logx.model.ReqCallPK;
import com.sktechx.palab.logx.model.enumRCType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by 1002382 on 2016. 7. 6..
 */
public interface RequestCallRepository extends JpaRepository<ReqCall, ReqCallPK> {

    //List<RequestCall> findByReqDt(Date date);


    List<ReqCall> findByKey(ReqCallPK key);

    @Query("select rc from ReqCall rc where rc.id.rcType = :rcType and rc.id.reqDt between :start and :end order by rc.id.reqDt desc")
    List<ReqCall> findByRcTypeAndBetween( @Param("rcType")enumRCType rcType, @Param("start")Date date1, @Param("end")Date date2);

    //List<RequestCall> findByRcTypeAndLikeReqDt(enumRCType rcType, String date);
}
