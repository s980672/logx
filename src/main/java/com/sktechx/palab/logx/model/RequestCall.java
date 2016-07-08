package com.sktechx.palab.logx.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="pv")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class RequestCall implements Serializable {

    public RequestCall() {
    }

    public RequestCall(enumRCType type, Date reqDt, Long count) {
        key = new RequestCallPK(type, reqDt);
        this.count = count;
    }


    @EmbeddedId
    RequestCallPK key;

    public RequestCallPK getKey() {
        return key;
    }

    private Long count;

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}