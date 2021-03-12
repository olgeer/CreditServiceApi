package com.sword.api.entity.communicate;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
/**
 * Session key entiy bean
 * @author max
 */
public class SessionKey {
    private String sessionKey;

    @JsonFormat(pattern = "yyyy年MM月dd日hh时mm分ss秒")
    private Date createTime;

    private String account;

    private String action;

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
        this.createTime=new Date();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
