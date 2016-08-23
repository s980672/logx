package com.sktechx.palab.logx.repository;

import com.sktechx.palab.logx.model.SvcView;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by 1002382 on 2016. 7. 20..
 */
public interface SvcViewRepository extends JpaRepository<SvcView, String>{

    SvcView findBySvcId(String svcId);
}
