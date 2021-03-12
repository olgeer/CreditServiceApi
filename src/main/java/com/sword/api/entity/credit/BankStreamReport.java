package com.sword.api.entity.credit;

import org.springframework.stereotype.Component;

@Component
/**
 * Bank stream report data entiy bean
 * @author max
 */
public class BankStreamReport {
    /**中文名*/
    private String cnName;
    /**英文名*/
    private String enName;
    /**身份证号*/
    private String idNo;
    /**性别*/
    private String sex;
    /**出生日*/
    private String birthday;
    /**年龄*/
    private int age;
    /**籍贯*/
    private String nativePlace;

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getNativePlace() {
        return nativePlace;
    }

    public void setNativePlace(String nativePlace) {
        this.nativePlace = nativePlace;
    }
}
