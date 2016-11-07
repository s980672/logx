package com.sktechx.palab.logx.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppStats implements Serializable {

    String svcName;

    String appName;

    String appId;

    String apiPath;

    List<CountAtDate> counts;

    public AppStats(){
        counts = Lists.newArrayList();
    };


    public AppStats(String svcName, String appName, String appId, String apiPath, List<CountAtDate> counts) {
        this.svcName = svcName;
        this.appName = appName;
        this.appId = appId;
        this.apiPath = apiPath;
        this.counts = counts;
    }

    public String getApiPath() {
        return apiPath;
    }

    public void setApiPath(String apiPath) {
        this.apiPath = apiPath;
    }

    public String getSvcName() {
        return svcName;
    }

    public void setSvcName(String svcName) {
        this.svcName = svcName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public List<CountAtDate> getCounts() {
        return counts;
    }

    public void addCount(Date date, Long count){
        CountAtDate cd = new CountAtDate(date, count);
        counts.add(cd);
    }

    public void removeAllCount() {
        counts.clear();
    }

    public void setCounts(List<CountAtDate> counts) {
        this.counts = counts;
    }

    @Override
    public String toString() {
        return "AppStats{" +
                "svcName='" + svcName + '\'' +
                ", appName='" + appName + '\'' +
                ", appId='" + appId + '\'' +
                ", apiPath='" + apiPath + '\'' +
                ", counts=" + counts.toString() +
                '}';
    }

    class CountAtDate{
        Date date;
        Long count;
        public CountAtDate(){};
        public CountAtDate(Date date, Long count) {
            this.date = date;
            this.count = count;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public Long getCount() {
            return count;
        }

        public void setCount(Long count) {
            this.count = count;
        }
    }
}