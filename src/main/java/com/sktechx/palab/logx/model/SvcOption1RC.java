package com.sktechx.palab.logx.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="svc_option1_pv")
public class SvcOption1RC implements Serializable {


    @EmbeddedId
    SvcOption1RCPK id;


    public SvcOption1RC() {
    }

    public SvcOption1RC(enumRCType rcType, enumOption1Type opType, Date reqDt, String svcId, String option1, long count) {

        id = new SvcOption1RCPK(rcType, opType, reqDt, svcId, option1);

        this.count = count;
    }


    public SvcOption1RCPK getId() {
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