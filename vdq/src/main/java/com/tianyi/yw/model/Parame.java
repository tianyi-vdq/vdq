package com.tianyi.yw.model;

import com.tianyi.yw.common.utils.Page;

public class Parame  extends Page {
    private Integer id;

    private Integer vdqTime;

    private Integer vdqCount;

    private Integer vdqFrame;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVdqTime() {
        return vdqTime;
    }

    public void setVdqTime(Integer vdqTime) {
        this.vdqTime = vdqTime;
    }

    public Integer getVdqCount() {
        return vdqCount;
    }

    public void setVdqCount(Integer vdqCount) {
        this.vdqCount = vdqCount;
    }

    public Integer getVdqFrame() {
        return vdqFrame;
    }

    public void setVdqFrame(Integer vdqFrame) {
        this.vdqFrame = vdqFrame;
    }
}