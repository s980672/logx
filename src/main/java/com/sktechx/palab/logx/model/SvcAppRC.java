package com.sktechx.palab.logx.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="svc_app_pv")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class SvcAppRC implements Serializable {


    @EmbeddedId
    SvcAppRCPK id;


    public SvcAppRC() {
    }

    public SvcAppRC(enumRCType rcType, Date reqDt, String svcId, String appId, long count) {

        id = new SvcAppRCPK(rcType, reqDt, svcId, appId);

        this.count = count;
    }


    public SvcAppRCPK getId() {
        return id;
    }

    private long count;

    public Long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }


    @Override
    public String toString() {
        return "SvcAppRC{" +
                "id=" + id +
                ", count=" + count +
                '}';
    }
}