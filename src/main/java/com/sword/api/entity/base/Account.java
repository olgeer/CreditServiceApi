package com.sword.api.entity.base;

import org.springframework.stereotype.Component;

@Component
/**
 * Account entiy bean
 * @author max
 *
 */
public class Account {
    private String account;
    private String password;

    //status , true 账号可用,false 账号不可用
    private boolean status;

    public Account(){}

    public Account(String account,String password){
        this.account=account;
        this.password=password;
        this.status=true;
    }

    public Account(String account,String password,boolean status){
        this.account=account;
        this.password=password;
        this.status=status;
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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean vaild(){
        boolean ret=false;
        if(account!=null && account.length()>0){
            if(password!=null && password.length()>0)ret=true;
        }
        return ret;
    }

    public boolean auth(){
        if(vaild() && status){

            return true;
        }
        return false;
    }
}
