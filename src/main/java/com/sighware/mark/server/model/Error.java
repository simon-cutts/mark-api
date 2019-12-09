package com.sighware.mark.server.model;

public class Error {
    private String code;
    private String status;
    private String detail;

    public Error(String code, String status, String detail) {
        this.code = code;
        this.status = status;
        this.detail = detail;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
