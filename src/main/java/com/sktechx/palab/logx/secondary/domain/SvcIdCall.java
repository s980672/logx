package com.sktechx.palab.logx.secondary.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by 1002382 on 2016. 7. 20..
 */
@Table(name="category_aud")
@Entity
public class SvcIdCall {

    @Id
    @Column(name="asset_id")
    String svcId;
    String categoryKey;
    String type;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    Integer revtype;

    public Integer getRevtype() {
        return revtype;
    }

    public void setRevtype(Integer revtype) {
        this.revtype = revtype;
    }

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

    public String getCategoryKey() {
        return categoryKey;
    }

    public void setCategoryKey(String categoryKey) {
        this.categoryKey = categoryKey;
    }

    @Override
    public String toString() {
        return "SvcIdCall{" +
                "svcId='" + svcId + '\'' +
                ", categoryKey='" + categoryKey + '\'' +
                ", type='" + type + '\'' +
                ", revtype=" + revtype +
                '}';
    }
}
