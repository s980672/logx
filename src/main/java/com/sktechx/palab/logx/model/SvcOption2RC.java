package com.sktechx.palab.logx.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="svc_option2_view")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SvcOption2RC implements Serializable {


    @EmbeddedId
    SvcOption2RCPK id;

    @Transient
    String svcName;

    @Transient
    String appName;

    @Transient
    String appId;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSvcName() {
        return svcName;
    }

    public void setSvcName(String svcName) {
        this.svcName = svcName;
    }

    public SvcOption2RC() {
    }

    public void setId(SvcOption2RCPK id) {
        this.id = id;
    }

    public SvcOption2RC(SvcOption2RCPK id, long count) {
        setId(id);
        setCount(count);
    }

    public SvcOption2RC(enumStatsType stsType, enumRCType rcType, enumOptionType opType, Date reqDt, String svcId,String categoryId, String option1, String option2, long count) {

        id = new SvcOption2RCPK(stsType,rcType, opType, reqDt, svcId, categoryId, option1, option2);

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
                ", svcName='" + svcName + '\'' +
                ", appName='" + appName + '\'' +
                ", appId='" + appId + '\'' +
                ", count=" + count +
                '}';
    }
}