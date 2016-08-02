package com.sktechx.palab.logx.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by 1002382 on 2016. 7. 7..
 */

@Embeddable
public class ServiceRCPK implements Serializable{

    public ServiceRCPK(){};

    public ServiceRCPK(enumStatsType stsType, enumRCType rcType, Date reqDt, String svcId) {
    	this.stsType = stsType;
        this.rcType = rcType;
        this.reqDt = reqDt;
        this.svcId = svcId;

    }
    
    @Enumerated(EnumType.STRING)
    private enumStatsType stsType;

    @Enumerated(EnumType.STRING)
    private enumRCType rcType;

    @Temporal(TemporalType.DATE)
    private Date reqDt;


    private String svcId;

    public String getSvcId() {
        return svcId;
    }

    public void setSvcId(String svcId) {
        this.svcId = svcId;
    }
    

    public enumStatsType getStatsType() {
        return stsType;
    }

    public void setStatsType(enumStatsType stsType) {
        this.stsType = stsType;
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
        if (!(o instanceof ServiceRCPK)) return false;

        ServiceRCPK that = (ServiceRCPK) o;

        if (getRcType() != that.getRcType()) return false;
        if (getReqDt() != null ? !getReqDt().equals(that.getReqDt()) : that.getReqDt() != null) return false;
        return !(getSvcId() != null ? !getSvcId().equals(that.getSvcId()) : that.getSvcId() != null);

    }

    @Override
    public int hashCode() {
        int result = getRcType() != null ? getRcType().hashCode() : 0;
        result = 31 * result + (getReqDt() != null ? getReqDt().hashCode() : 0);
        result = 31 * result + (getSvcId() != null ? getSvcId().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ServiceRCPK{" +
                "stsType=" + stsType +
                "rcType=" + rcType +
                ", reqDt=" + reqDt +
                ", svcId='" + svcId + '\'' +
                '}';
    }
}
