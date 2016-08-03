package com.sktechx.palab.logx.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="svc_option1_view")
public class SvcOption1RC implements Serializable {


    @EmbeddedId
    SvcOption1RCPK id;


    public SvcOption1RC() {
    }

    public SvcOption1RC(enumStatsType stsType, enumRCType rcType, enumOptionType opType, Date reqDt, String svcId, String option1, long count) {

        id = new SvcOption1RCPK(stsType,rcType, opType, reqDt, svcId, option1);

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SvcOption1RC)) return false;

        SvcOption1RC that = (SvcOption1RC) o;

        return getId().equals(that.getId());

    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public String toString() {
        return "SvcOption1RC{" +
                "id=" + id +
                ", count=" + count +
                '}';
    }
}