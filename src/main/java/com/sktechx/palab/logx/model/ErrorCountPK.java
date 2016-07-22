package com.sktechx.palab.logx.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by 1002382 on 2016. 7. 7..
 */

@Embeddable
public class ErrorCountPK implements Serializable{

    public ErrorCountPK(){};

    public ErrorCountPK(enumRCType rcType, Date reqDt, String code) {
        this.rcType = rcType;
        this.reqDt = reqDt;
        this.code = code;
    }

    @Enumerated(EnumType.STRING)
    private enumRCType rcType;

    @Temporal(TemporalType.DATE)
    private Date reqDt;


    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
        if (!(o instanceof ErrorCountPK)) return false;

        ErrorCountPK that = (ErrorCountPK) o;

        if (getRcType() != that.getRcType()) return false;
        if (!getReqDt().equals(that.getReqDt())) return false;
        return getCode().equals(that.getCode());

    }

    @Override
    public int hashCode() {
        int result = getRcType().hashCode();
        result = 31 * result + getReqDt().hashCode();
        result = 31 * result + getCode().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ErrorCountPK{" +
                "rcType=" + rcType +
                ", reqDt=" + reqDt +
                ", code='" + code + '\'' +
                '}';
    }
}
