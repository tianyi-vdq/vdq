package com.tianyi.yw.model;

import java.util.Date;

import com.tianyi.yw.common.utils.Page;

public class Task extends Page {
    private Integer id;

    private Date startTime;

    private Integer runIntervals;

    private Integer runTimes;

    private Integer runCount;

    private Integer flag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Integer getRunIntervals() {
        return runIntervals;
    }

    public void setRunIntervals(Integer runIntervals) {
        this.runIntervals = runIntervals;
    }

    public Integer getRunTimes() {
        return runTimes;
    }

    public void setRunTimes(Integer runTimes) {
        this.runTimes = runTimes;
    }

    public Integer getRunCount() {
        return runCount;
    }

    public void setRunCount(Integer runCount) {
        this.runCount = runCount;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }
}