package com.sktechx.palab.logx.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="svc_option1_view")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SvcOption1RC implements Serializable {


    @EmbeddedId
    SvcOption1RCPK id;

    public void setId(SvcOption1RCPK id) {
        this.id = id;
    }

    //for svc name
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

    public SvcOption1RC() {
    }

    public SvcOption1RC(enumStatsType stsType, enumRCType rcType, enumOptionType opType, Date reqDt, String svcId,String categoryId, String option1, long count) {

        id = new SvcOption1RCPK(stsType,rcType, opType, reqDt, svcId,categoryId, option1);

        this.count = count;
    }

    public SvcOption1RCPK getId() {
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
        if (!(o instanceof SvcOption1RC)) return false;

        SvcOption1RC that = (SvcOption1RC) o;

        return getId().equals(that.getId());

    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public String toString() {
        return "SvcOption1RC{" +
                "id=" + id +
                ", svcName='" + svcName + '\'' +
                ", appName='" + appName + '\'' +
                ", appId='" + appId + '\'' +
                ", count=" + count +
                '}';
    }
}