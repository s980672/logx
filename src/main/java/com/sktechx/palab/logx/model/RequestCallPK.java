package com.sktechx.palab.logx.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by 1002382 on 2016. 7. 7..
 */

@Embeddable
public class RequestCallPK implements Serializable{

    public RequestCallPK(){};

    public RequestCallPK(enumRCType rcType, Date reqDt) {
        this.rcType = rcType;
        this.reqDt = reqDt;
    }

    @Enumerated(EnumType.STRING)
    private enumRCType rcType;

    @Temporal(TemporalType.DATE)
    private Date reqDt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RequestCallPK)) return false;

        RequestCallPK that = (RequestCallPK) o;

        if (getRcType() != that.getRcType()) return false;
        return !(getReqDt() != null ? !getReqDt().equals(that.getReqDt()) : that.getReqDt() != null);

    }

    @Override
    public int hashCode() {
        int result = getRcType() != null ? getRcType().hashCode() : 0;
        result = 31 * result + (getReqDt() != null ? getReqDt().hashCode() : 0);
        return result;
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
}
