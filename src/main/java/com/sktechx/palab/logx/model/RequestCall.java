package com.sktechx.palab.logx.model;

import javax.persistence.*;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name="pv")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class RequestCall implements Serializable {

    public RequestCall() {
    }

    public RequestCall(enumRCType type, Date reqDt, long count) {
        key = new RequestCallPK(type, reqDt);
        this.count = count;
    }

    public RequestCall(enumRCType type, String reqDt, long count) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(reqDt);
        key = new RequestCallPK(type, date);
        this.count = count;

    }

    @EmbeddedId
    RequestCallPK key;

    public RequestCallPK getKey() {
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