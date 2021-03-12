package com.sword.api.entity.communicate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sword.common.Encrypt;
import com.sword.common.Json;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.springframework.stereotype.Component;

@Component
/**
 * Request package bean
 * @author max
 */
public class RequestPackage {
    private String reqAccount;
    private String reqSessionKey;
    private String action;
    private String reqContext;

    public RequestPackage() {
    }

    public RequestPackage(String reqAccount, String action, String reqContext) {
        this.reqAccount = reqAccount;
        this.reqSessionKey = "unuse";
        this.action = action;
        this.reqContext = reqContext;
    }

    public RequestPackage(String reqAccount, String reqSessionKey, String action, String reqContext) {
        this.reqAccount = reqAccount;
        this.reqSessionKey = reqSessionKey;
        this.action = action;
        this.reqContext = reqContext;
    }

    public String getReqAccount() {
        return reqAccount;
    }

    public void setReqAccount(String reqAccount) {
        this.reqAccount = reqAccount;
    }

    public String getReqSessionKey() {
        return reqSessionKey;
    }

    public void setReqSessionKey(String reqSessionKey) {
//        if (reqContext != null && reqSessionKey != null) {
//            this.reqContext = Encrypt.encode(reqContext, reqSessionKey);
//        }
        this.reqSessionKey = reqSessionKey;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getReqContext() {
        return reqContext;
    }

    public void setReqContext(String reqContext) {
//        if (reqSessionKey != null && reqContext != null) {
//            this.reqContext = Encrypt.encode(reqContext, reqSessionKey);
//        } else {
            this.reqContext = reqContext;
//        }
    }

    public void encode(){
        if (reqSessionKey != null && reqContext != null) {
            reqContext = Encrypt.encode(reqContext, reqSessionKey);
        }
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

    public RequestBody toBody() {
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), toString());
    }
}
