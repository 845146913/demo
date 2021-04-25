package com.example.demo.feign;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class SearchReq {
    private String wd;
    private int rsvSpt=1;

    public int getRsvSpt() {
        return rsvSpt;
    }

    public void setRsvSpt(int rsvSpt) {
        this.rsvSpt = rsvSpt;
    }

    public String getWd() {
        return wd;
    }

    public void setWd(String wd) {
        this.wd = wd;
    }
}
