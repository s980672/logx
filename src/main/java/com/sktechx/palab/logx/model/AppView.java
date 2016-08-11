package com.sktechx.palab.logx.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by 1002382 on 2016. 7. 20..
 */
@Entity
@Table(name="view_app")
public class AppView {

    @Id
    String appId;

    String name;

    String appKey;


    public AppView() {
    }

    public AppView(String appId, String name, String appKey) {
        this.appId = appId;
        this.name = name;
        this.appKey = appKey;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    @Override
    public String toString() {
        return "AppView{" +
                "appId='" + appId + '\'' +
                ", name='" + name + '\'' +
                ", appKey='" + appKey + '\'' +
                '}';
    }
}
