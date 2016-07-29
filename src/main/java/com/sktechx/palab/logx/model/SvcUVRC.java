package com.sktechx.palab.logx.model;

import javax.persistence.*;

import org.springframework.beans.factory.annotation.Autowired;

import com.sktechx.palab.logx.service.ElasticsearchUVAnalysisService;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="service_uv")
public class SvcUVRC implements Serializable {

	@Autowired
	ElasticsearchUVAnalysisService elUVservice;
	
    @EmbeddedId
    SvcOption2RCPK id;


    public SvcUVRC() {
    }

    public SvcUVRC(enumRCType rcType, enumOptionType opType, Date reqDt, String svcId, String option1,String option2, long count) {

//        id = new elUVservice.generateSVUV(rcType, reqDt, svcId);        
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
    public String toString() {
        return "SvcOption2RC{" +
                "id=" + id +
                ", count=" + count +
                '}';
    }
}