package com.sktechx.palab.logx.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="svc_option2_pv")
public class SvcUVRC implements Serializable {


    @EmbeddedId
    SvcOption2RCPK id;


    public SvcUVRC() {
    }

    public SvcUVRC(enumRCType rcType, enumOptionType opType, Date reqDt, String svcId, String option1,String option2, long count) {

        id = new SvcOption2RCPK(rcType, opType, reqDt, svcId, option1,option2);        
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