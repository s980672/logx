package com.sktechx.palab.logx.secondary.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by 1002382 on 2016. 7. 20..
 */
@Table(name="category")
@Entity
public class SvcIdCall {

    @Id
    @Column(name="asset_id")
    String svcId;
    String category_key;
    String type;

    public SvcIdCall(){}

    public SvcIdCall(String svcId, String category_key){
        this.svcId = svcId;
        this.category_key = category_key;
    }

    public String getSvcId() {
        return svcId;
    }

    public void setSvcId(String svcId) {
        this.svcId = svcId;
    }

    public String getCategoryId() {
        return category_key;
    }

    public void setCategoryId(String category_key) {
        this.category_key = category_key;
    }

    public String gettype() {
        return type;
    }

    public void settype(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "SvcIdCall{" +
                "svcId=" + svcId +
                "category_key=" + category_key +
                ", type=" + type +
                '}';
    }

}
