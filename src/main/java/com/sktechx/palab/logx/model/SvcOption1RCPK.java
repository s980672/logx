package com.sktechx.palab.logx.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by 1002382 on 2016. 7. 7..
 */

@Embeddable
public class SvcOption1RCPK implements Serializable{

    public SvcOption1RCPK(){};

    public SvcOption1RCPK(enumStatsType stsType, enumRCType rcType, enumOptionType opType, Date reqDt, String svcId, String categoryId, String option1) {
    	this.stsType = stsType;
        this.opType = opType;
        this.rcType = rcType;
        this.reqDt = reqDt;
        this.option1 = option1;
        this.svcId = svcId;
        this.categoryId = categoryId;
    }
    

    @Column(length=10)
    @Enumerated(EnumType.STRING)
    private enumStatsType stsType;

    public enumStatsType getStsType() {
        return stsType;
    }

    public void setStsType(enumStatsType StsType) {
        this.stsType = stsType;
    }

    @Column(length=10)
    @Enumerated(EnumType.STRING)
    private enumOptionType opType;


    public enumOptionType getOpType() {
        return opType;
    }

    public void setOpType(enumOptionType opType) {
        this.opType = opType;
    }

    @Enumerated(EnumType.STRING)
    private enumRCType rcType;

    @Temporal(TemporalType.DATE)
    private Date reqDt;

    private String option1;

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    private String svcId;

    @Column(length=100)
    private String categoryId;

    public String getSvcId() {
        return svcId;
    }

    public void setSvcId(String svcId) {
        this.svcId = svcId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }


    public enumRCType getRcType() {
        return rcType;
    }

    public void setRcType(enumRCType rcType) {
        this.rcType = rcType;
    }

    public Date getReqDt() {
        return reqDt;
    }

    public void setReqDt(Date reqDt) {
        this.reqDt = reqDt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SvcOption1RCPK)) return false;

        SvcOption1RCPK that = (SvcOption1RCPK) o;

        if (getStsType() != that.getStsType()) return false;
        if (getOpType() != that.getOpType()) return false;
        if (getRcType() != that.getRcType()) return false;
        if (!getReqDt().equals(that.getReqDt())) return false;
        if (!getOption1().equals(that.getOption1())) return false;
        if (!getCategoryId().equals(that.getCategoryId())) return false;
        return getSvcId().equals(that.getSvcId());

    }

    @Override
    public int hashCode() {
        int result = getStsType().hashCode();
        result = 31 * result + getOpType().hashCode();
        result = 31 * result + getRcType().hashCode();
        result = 31 * result + getReqDt().hashCode();
        result = 31 * result + getOption1().hashCode();
        result = 31 * result + getCategoryId().hashCode();
        result = 31 * result + getSvcId().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "SvcOption1RCPK{" +
    			"stsType=" + stsType +
                "opType=" + opType +
                ", rcType=" + rcType +
                ", reqDt=" + reqDt +
                ", option1='" + option1 + '\'' +
                ", svcId='" + svcId + '\'' +
                ", categoryId='" + categoryId + '\'' +
                '}';
    }
}
