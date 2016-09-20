package com.sktechx.palab.logx.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name="service_view")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceRequestCall implements Serializable {


    @EmbeddedId
    ServiceRCPK id;


    @Transient
    String svcName;

    public String getSvcName() {
        return svcName;
    }

    public void setSvcName(String svcName) {
        this.svcName = svcName;
    }

    public ServiceRequestCall() {
        id = new ServiceRCPK();

    }

    public ServiceRequestCall(enumStatsType stsType, enumRCType rcType, Date reqDt, String svcId,String categoryId, long count) {

        id = new ServiceRCPK(stsType,rcType, reqDt, svcId,categoryId);
        this.count = count;
    }

    public ServiceRequestCall(enumStatsType stsType, enumRCType rcType, String reqDt, String svcId, String categoryId, long count) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(reqDt);
        id = new ServiceRCPK(stsType,rcType, date, svcId, categoryId);

        this.count = count;
    }

    private long count;

    public ServiceRCPK getId() {
        return id;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }


    @Override
    public String toString() {
        return "ServiceRequestCall{" +
                "id=" + id +
                ", count=" + count +
                '}';
    }
}