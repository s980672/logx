package com.sktechx.palab.logx.repository;

        import com.sktechx.palab.logx.model.ReqCall;
import com.sktechx.palab.logx.model.ReqCallPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by 1002382 on 2016. 7. 6..
 */
public interface RequestCallRepository extends JpaRepository<ReqCall, ReqCallPK> {

    //List<RequestCall> findByReqDt(Date date);


    List<ReqCall> findByKey(ReqCallPK key);

    //List<RequestCall> findByRcTypeAndLikeReqDt(enumRCType rcType, String date);
}
