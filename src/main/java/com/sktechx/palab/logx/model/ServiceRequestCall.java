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
        id = new ServiceRCPK();

    }

    public ServiceRequestCall(enumRCType rcType, Date reqDt, String svcId, long count) {

        id = new ServiceRCPK(rcType, reqDt, svcId);

        this.count = count;
    }



    private long count;

    public ServiceRCPK getId() {
        return id;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }


    @Override
    public String toString() {
        return "ServiceRequestCall{" +
                "id=" + id +
                ", count=" + count +
                '}';
    }
}