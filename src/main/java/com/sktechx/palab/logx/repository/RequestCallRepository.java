package com.sktechx.palab.logx.repository;

        import com.sktechx.palab.logx.model.RequestCall;
import com.sktechx.palab.logx.model.RequestCallPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by 1002382 on 2016. 7. 6..
 */
public interface RequestCallRepository extends JpaRepository<RequestCall, RequestCallPK> {

    //List<RequestCall> findByReqDt(Date date);


    List<RequestCall> findByKey(RequestCallPK key);

    //List<RequestCall> findByRcTypeAndLikeReqDt(enumRCType rcType, String date);
}
