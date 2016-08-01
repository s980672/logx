package com.sktechx.palab.logx.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by 1002382 on 2016. 7. 7..
 */

@Embeddable
public class SvcUVRCPK implements Serializable{

    public SvcUVRCPK(){};

    public SvcUVRCPK(Date reqDt, String svcId ) {
        this.reqDt = reqDt;
        this.svcId = svcId;
    }    

    @Temporal(TemporalType.DATE)
    private Date reqDt;

    private String svcId;

    public String getSvcId() {
        return svcId;
    }

    public void setSvcId(String svcId) {
        this.svcId = svcId;
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
        if (!(o instanceof SvcUVRCPK)) return false;

        SvcUVRCPK that = (SvcUVRCPK) o;
        if (!getReqDt().equals(that.getReqDt())) return false;
        return getSvcId().equals(that.getSvcId());

    }

    @Override
    public int hashCode() {
        int result = getReqDt().hashCode();
        result = 31 * result + getSvcId().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "SvcOption2RCPK{" +
                ", reqDt=" + reqDt +
                ", svcId='" + svcId + '\'' +
                '}';
    }
}
