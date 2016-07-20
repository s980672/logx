package com.sktechx.palab.logx.repository;

import com.sktechx.palab.logx.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by 1002382 on 2016. 7. 20..
 */
public interface SvcRepository extends JpaRepository<Service, String>{

    Service findBySvcId(String svcId);
}
