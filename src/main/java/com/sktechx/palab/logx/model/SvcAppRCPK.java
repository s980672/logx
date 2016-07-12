package com.sktechx.palab.logx.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by 1002382 on 2016. 7. 7..
 */

@Embeddable
public class SvcAppRCPK implements Serializable{

    public SvcAppRCPK(){};

    public SvcAppRCPK(enumRCType rcType, Date reqDt, String svcId, String appId) {
        this.rcType = rcType;
        this.reqDt = reqDt;
        this.appId = appId;
        this.svcId = svcId;
    }

    @Enumerated(EnumType.STRING)
    private enumRCType rcType;

    @Temporal(TemporalType.DATE)
    private Date reqDt;

    private String appId;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
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
        if (!(o instanceof SvcAppRCPK)) return false;

        SvcAppRCPK that = (SvcAppRCPK) o;

        if (getRcType() != that.getRcType()) return false;
        if (getReqDt() != null ? !getReqDt().equals(that.getReqDt()) : that.getReqDt() != null) return false;
        if (getAppId() != null ? !getAppId().equals(that.getAppId()) : that.getAppId() != null) return false;
        return !(getSvcId() != null ? !getSvcId().equals(that.getSvcId()) : that.getSvcId() != null);

    }

    @Override
    public int hashCode() {
        int result = getRcType() != null ? getRcType().hashCode() : 0;
        result = 31 * result + (getReqDt() != null ? getReqDt().hashCode() : 0);
        result = 31 * result + (getAppId() != null ? getAppId().hashCode() : 0);
        result = 31 * result + (getSvcId() != null ? getSvcId().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SvcAppRCPK{" +
                "rcType=" + rcType +
                ", reqDt=" + reqDt +
                ", appId='" + appId + '\'' +
                ", svcId='" + svcId + '\'' +
                '}';
    }
}
