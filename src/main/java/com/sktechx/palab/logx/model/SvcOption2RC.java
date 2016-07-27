package com.sktechx.palab.logx.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="svc_option2_pv")
public class SvcOption2RC implements Serializable {


    @EmbeddedId
    SvcOption2RCPK id;


    public SvcOption2RC() {
    }

    public SvcOption2RC(enumRCType rcType, enumOptionType opType, Date reqDt, String svcId, String option1, String option2, long count) {

        id = new SvcOption2RCPK(rcType, opType, reqDt, svcId, option1, option2);

        this.count = count;
    }


    public SvcOption2RCPK getId() {
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
        if (!(o instanceof SvcOption2RC)) return false;

        SvcOption2RC that = (SvcOption2RC) o;

        return getId().equals(that.getId());

    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public String toString() {
        return "SvcOption2RC{" +
                "id=" + id +
                ", count=" + count +
                '}';
    }
}