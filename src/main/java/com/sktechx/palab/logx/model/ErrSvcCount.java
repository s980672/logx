package com.sktechx.palab.logx.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by 1002382 on 2016. 7. 21..
 */
@Entity
@Table(name="error_svc_count")
public class ErrSvcCount implements Serializable {


    public ErrSvcCount(){}

    @EmbeddedId
    ErrSvcCntPK key;

    long count;

    public ErrSvcCount(enumRCType type, Date reqDt, String svcId, String code, long count){

        ErrSvcCntPK id = new ErrSvcCntPK(type, reqDt, code , svcId);

        this.key     = id;

        this.count = count;

    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

}
