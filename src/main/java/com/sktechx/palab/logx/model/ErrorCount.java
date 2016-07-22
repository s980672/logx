package com.sktechx.palab.logx.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by 1002382 on 2016. 7. 21..
 */
@Entity
public class ErrorCount implements Serializable {


    public ErrorCount(){}

    @EmbeddedId
    ErrorCountPK key;


    long count;

    public ErrorCount(enumRCType type, Date reqDt, String code, long count){

        ErrorCountPK id = new ErrorCountPK(type, reqDt, code );

        this.key     = id;
        this.count = count;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public ErrorCountPK getKey() {
        return key;
    }

    public void setKey(ErrorCountPK key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ErrorCount)) return false;

        ErrorCount that = (ErrorCount) o;

        return getKey().equals(that.getKey());

    }

    @Override
    public int hashCode() {
        return getKey().hashCode();
    }

    @Override
    public String toString() {
        return "ErrorCount{" +
                "key=" + key +
                ", count=" + count +
                '}';
    }
}
