package com.aias.dispatcher.server.dingtalk;

import java.io.Serializable;

/**
 * @author liuhy
 * @since 2021/8/1
 */
public class DingResult implements Serializable {
    private static final long serialVersionUID = -1080246157364626958L;
    private String errmsg;
    private Integer errcode;

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public Integer getErrcode() {
        return errcode;
    }

    public void setErrcode(Integer errcode) {
        this.errcode = errcode;
    }
}
