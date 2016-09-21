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
    String categoryKey;
    String type;

    public SvcIdCall(){}

    public SvcIdCall(String svcId, String categoryKey){
        this.svcId = svcId;
        this.categoryKey = categoryKey;
    }

    public String getSvcId() {
        return svcId;
    }

    public void setSvcId(String svcId) {
        this.svcId = svcId;
    }

    public String getCategoryId() {
        return categoryKey;
    }

    public void setCategoryId(String category_key) {
        this.categoryKey = category_key;
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
                "categoryKey=" + categoryKey +
                ", type=" + type +
                '}';
    }

}
