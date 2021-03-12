package com.sword.api.entity.communicate;

import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
/**
 * Response package entiy bean
 * @author max
 */
public class ResponsePackage {
    private static Properties codeMap;
    private String respCode;

    private String respMessage;
    private String respContext;

    {
        codeMap = new Properties();
        codeMap.setProperty("9001","json解释失败或json格式错误");
        codeMap.setProperty("0000","调用成功");
        codeMap.setProperty("0001","用户认证失败");
        codeMap.setProperty("0002","用户注册成功");
        codeMap.setProperty("0003","用户注销成功");
        codeMap.setProperty("0004","用户存在");
        codeMap.setProperty("0007","用户不存在");
        codeMap.setProperty("0005","用户注册失败");
        codeMap.setProperty("0006","用户注销失败");
        codeMap.setProperty("0009","非法SessionKey或过期使用");
        codeMap.setProperty("0010","请输入验证码");
        codeMap.setProperty("0011","数据获取中，请等待");
        codeMap.setProperty("0019","数据获取失败");
        codeMap.setProperty("0020","银行流水获取成功");
        codeMap.setProperty("0024","银行流水获取失败");
        codeMap.setProperty("0030","收藏书籍列表获取成功");
        codeMap.setProperty("0031","收藏书籍列表保存成功");
        codeMap.setProperty("0034","收藏书籍列表获取失败");
        codeMap.setProperty("0035","收藏书籍列表保存失败");
    }

    public ResponsePackage() {
    }

    public ResponsePackage(String respCode, String respContext) {
        setRespCode(respCode);
        this.respContext = respContext;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
        this.respMessage=codeMap.getProperty(respCode);
    }

    public String getRespMessage() {
        return respMessage;
    }

    public void setRespMessage(String respMessage) {
        this.respMessage = respMessage;
    }

    public String getRespContext() {
        return respContext;
    }

    public void setRespContext(String respContext) {
        this.respContext = respContext;
    }
}
