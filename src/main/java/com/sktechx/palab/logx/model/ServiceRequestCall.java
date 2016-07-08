package com.sktechx.palab.logx.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="service_pv")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class ServiceRequestCall implements Serializable {


    @EmbeddedId
    ServiceRCPK id;


    public ServiceRequestCall() {
    }

    public ServiceRequestCall(enumRCType rcType, Date reqDt, String svcId, Long count) {

        id = new ServiceRCPK(rcType, reqDt, svcId);

        this.count = count;
    }



    private Long count;

    public ServiceRCPK getId() {
        return id;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}