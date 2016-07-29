package com.sktechx.palab.logx.model;

import org.joda.time.LocalDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by 1002382 on 2016. 7. 7..
 */

@Embeddable
public class SvcOption2RCPK implements Serializable{

    public SvcOption2RCPK(){};

    public SvcOption2RCPK(enumRCType rcType, enumOptionType opType, Date reqDt, String svcId, String option1, String option2) {
        this.opType = opType;
        this.rcType = rcType;
        this.reqDt = reqDt;
        this.option1 = option1;
        this.svcId = svcId;
        this.option2 = option2;
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

    @Column(length=10)
    @Enumerated(EnumType.STRING)
    private enumRCType rcType;

    @Temporal(TemporalType.DATE)
    private Date reqDt;

    private String option1;

    private String option2;

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    private String svcId;

    public String getSvcId() {
        return svcId;
    }

    public void setSvcId(String svcId) {
        this.svcId = svcId;
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
        if (!(o instanceof SvcOption2RCPK)) return false;

        SvcOption2RCPK that = (SvcOption2RCPK) o;

        if (getOpType() != that.getOpType()) return false;
        if (getRcType() != that.getRcType()) return false;
        LocalDate ldReqDt = new LocalDate(getReqDt());
        LocalDate ldReqDt2 = new LocalDate(that.getReqDt());

        if ( ldReqDt.getYear() != ldReqDt2.getYear()) return false;
        if ( ldReqDt.getMonthOfYear() != ldReqDt2.getMonthOfYear()) return false;
        if ( ldReqDt.getDayOfMonth() != ldReqDt2.getDayOfMonth()) return false;
//        if (!getReqDt().equals(that.getReqDt())) return false;
        if (!getOption1().equals(that.getOption1())) return false;
        if (!getOption2().equals(that.getOption2())) return false;
        return getSvcId().equals(that.getSvcId());

    }

    @Override
    public int hashCode() {
        int result = getOpType().hashCode();
        result = 31 * result + getRcType().hashCode();
        result = 31 * result + getReqDt().hashCode();
        result = 31 * result + getOption1().hashCode();
        result = 31 * result + getOption2().hashCode();
        result = 31 * result + getSvcId().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "SvcOption2RCPK{" +
                "opType=" + opType +
                ", rcType=" + rcType +
                ", reqDt=" + reqDt +
                ", option1='" + option1 + '\'' +
                ", option2='" + option2 + '\'' +
                ", svcId='" + svcId + '\'' +
                '}';
    }
}
