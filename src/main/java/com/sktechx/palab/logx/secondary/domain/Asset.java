package com.sktechx.palab.logx.secondary.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by 1002382 on 2016. 7. 20..
 */
@Entity
public class Asset {

    @Id
    Long id;

    String name;

    public Asset(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Asset)) return false;

        Asset asset = (Asset) o;

        return id.equals(asset.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Asset{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }
}
