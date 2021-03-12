package com.sword.api.entity.communicate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sword.common.Json;
import org.springframework.stereotype.Component;

@Component
/**
 * New session cmd entiy bean
 * @author max
 */
public class SessionlessCmd {
    private String account;
    private String password;
    private String action;

    public SessionlessCmd(){}

    public SessionlessCmd(String account,String password,String action){
        this.account=account;
        this.password=password;
        this.action=action;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        try {
            return Json.mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
