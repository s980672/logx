package com.sktechx.palab.logx.model;

import javax.persistence.*;

import org.springframework.beans.factory.annotation.Autowired;

import com.sktechx.palab.logx.service.ElasticsearchUVAnalysisService;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="service_uv")
public class SvcUVRC implements Serializable {

	
    @EmbeddedId
    SvcUVRCPK id;


    public SvcUVRC() {
    }

    public SvcUVRC(Date reqDt, String svcId, long count) {

        id = new SvcUVRCPK(reqDt, svcId);       
        this.count = count;
    }


    public SvcUVRCPK getId() {
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
    public String toString() {
        return "SvcOption2RC{" +
                "id=" + id +
                ", count=" + count +
                '}';
    }
}