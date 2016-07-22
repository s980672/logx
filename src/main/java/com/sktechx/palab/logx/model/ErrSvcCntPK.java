package com.sktechx.palab.logx.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by 1002382 on 2016. 7. 7..
 */

@Embeddable
public class ErrSvcCntPK implements Serializable{

    private String svcId;

    public String getSvcId() {
        return svcId;
    }

    public void setSvcId(String svcId) {
        this.svcId = svcId;
    }

    public ErrSvcCntPK(){};

    public ErrSvcCntPK(enumRCType rcType, Date reqDt, String code, String svcId) {
        this.rcType = rcType;
        this.reqDt = reqDt;
        this.code = code;
        this.svcId = svcId;
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
        if (!(o instanceof ErrSvcCntPK)) return false;

        ErrSvcCntPK that = (ErrSvcCntPK) o;

        if (!getSvcId().equals(that.getSvcId())) return false;
        if (getRcType() != that.getRcType()) return false;
        if (!getReqDt().equals(that.getReqDt())) return false;
        return getCode().equals(that.getCode());

    }

    @Override
    public int hashCode() {
        int result = getSvcId().hashCode();
        result = 31 * result + getRcType().hashCode();
        result = 31 * result + getReqDt().hashCode();
        result = 31 * result + getCode().hashCode();
        return result;
    }


    @Override
    public String toString() {
        return "ErrSvcCntPK{" +
                "svcId='" + svcId + '\'' +
                ", rcType=" + rcType +
                ", reqDt=" + reqDt +
                ", code='" + code + '\'' +
                '}';
    }
}
