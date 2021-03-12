package com.sword.api.entity.credit;

import org.springframework.stereotype.Component;

@Component
/**
 * Bank stream command entiy bean
 * @author max
 */
public class BankStreamCmd {
    /**中文名*/
    private String cnName;
    /**身份证号*/
    private String idNo;
    /**手机*/
    private String mobile;
    /**银行*/
    private String bankName;
    /**卡号*/
    private String bankCardId;
    /**网银账号*/
    private String uniAccount;
    /**网银密码*/
    private String uniPassword;

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCardId() {
        return bankCardId;
    }

    public void setBankCardId(String bankCardId) {
        this.bankCardId = bankCardId;
    }

    public String getUniAccount() {
        return uniAccount;
    }

    public void setUniAccount(String uniAccount) {
        this.uniAccount = uniAccount;
    }

    public String getUniPassword() {
        return uniPassword;
    }

    public void setUniPassword(String uniPassword) {
        this.uniPassword = uniPassword;
    }
}
