package com.sktechx.palab.logx.model;

import javax.persistence.*;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name="pv")
public class ReqCall implements Serializable {

    public ReqCall() {
    }

    public ReqCall(enumRCType type, Date reqDt, long count) {
        key = new ReqCallPK(type, reqDt);
        this.count = count;
    }

    public ReqCall(enumRCType type, String reqDt, long count) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(reqDt);
        key = new ReqCallPK(type, date);
        this.count = count;

    }

    @EmbeddedId
    ReqCallPK key;

    public ReqCallPK getKey() {
        return key;
    }

    private long count;

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}