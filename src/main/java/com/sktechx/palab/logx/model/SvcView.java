package com.sktechx.palab.logx.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by 1002382 on 2016. 7. 20..
 */
@Entity
@Table(name="view_svc")
public class SvcView {

    @Id
    String svcId;

    String name;

    public SvcView(){}

    public SvcView(String svcId, String name){
        this.svcId = svcId;
        this.name = name;
    }

    public String getSvcId() {
        return svcId;
    }

    public void setSvcId(String svcId) {
        this.svcId = svcId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (!(o instanceof SvcView)){
            return false;
        }

        SvcView service = (SvcView) o;
        return getSvcId().equals(service.getSvcId());

    }

    @Override
    public int hashCode() {
        return getSvcId().hashCode();
    }

    @Override
    public String toString() {
        return "Service{" +
                "svcId='" + svcId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
