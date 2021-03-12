package com.sword.api.entity.communicate;

import org.springframework.stereotype.Component;

@Component
/**
 * Common cmd entity bean
 * @author max
 */
public class SessionCmd {
    private String account;
    private String action;
    private String param;

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

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }
}