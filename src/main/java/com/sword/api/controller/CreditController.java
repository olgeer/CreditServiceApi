package com.sword.api.controller;

import com.sword.api.service.BookMgr;
import com.sword.api.entity.communicate.*;
import com.sword.api.entity.credit.BankStreamCmd;
import com.sword.api.service.BankStream;
import com.sword.api.service.SessionMgr;
import com.sword.common.Encrypt;
import com.sword.common.Json;
import com.sword.common.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

@RestController
@RequestMapping("/api")
/**
 * credit Controller
 * RequestPackage json format:
 * {
 *      reqAccount,
 *      reqSessionKey,
 *      action,
 *      reqContext: CmdJson
 * }
 * 其中reqContext为action对应的Cmd的json，使用session key进行过加密
 *
 * CommonCmd json format:
 * {
 *     account,
 *     action,
 *     param
 * }
 * 其中param为action对应的参数json
 *
 * api's root path is /api/
 * slr为无session key的请求接口
 * sr为必须session key有效的请求接口
 *
 * 请求流程：
 *  除注册不需要有效session key外（使用默认session key加密数据），
 *  其它请求均需要先到slr申请对应session key，
 *  并在session key有效期内（默认为120秒）提交到sr
 *
 * @author max
 */
public class CreditController {
    @Autowired
    private Environment env;

    private final static Logger logger = Logger.newInstance(CreditController.class);

    @RequestMapping(value = "/alive", method = RequestMethod.GET)
    public String alive() {
        return "Service available ! Version 0.1 [" + env.getProperty("from") + "]";
    }

    @RequestMapping(value = "/showAccountDb", method = RequestMethod.GET)
    public String showAccountDb() {
        return SessionMgr.getAccountList(10).toString();
    }

    @RequestMapping(value = "/slr", method = RequestMethod.POST)
    public ResponsePackage sessionLessRequest(@RequestBody String requestBody) {
        ResponsePackage respP = new ResponsePackage();
        try {
            logger.debug(requestBody);
            RequestPackage reqP = Json.mapper.readValue(requestBody, RequestPackage.class);
            SessionlessCmd sessionlessCmd = Json.mapper.readValue(Encrypt.decode(reqP.getReqContext(), reqP.getReqSessionKey()), SessionlessCmd.class);

            switch (reqP.getAction()) {
                case "register":
                    if (SessionMgr.register(sessionlessCmd.getAccount(), sessionlessCmd.getPassword())) {
                        respP.setRespCode("0002");
                    } else {
                        respP.setRespCode("0005");
                    }
                    break;
                case "isExist":
                    if (SessionMgr.isExist(sessionlessCmd.getAccount())) {
                        respP.setRespCode("0004");
                    } else {
                        respP.setRespCode("0007");
                    }
                    break;
                case "getSessionKey":
                default:
                    SessionKey sessionKey = SessionMgr.newSession(sessionlessCmd);
                    if (sessionKey != null) {
                        respP.setRespCode("0000");
                        respP.setRespContext(Encrypt.encode(Json.mapper.writeValueAsString(sessionKey), reqP.getReqSessionKey()));
                    } else {
                        respP.setRespCode("0001");
                    }
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
            respP.setRespCode("9001");
            respP.setRespContext(e.getMessage());
        }
        return respP;
    }

    @RequestMapping(value = "/sr", method = RequestMethod.POST)
    public ResponsePackage sessionRequest(@RequestBody String requestBody) {
        ResponsePackage respP = new ResponsePackage();
        try {
            logger.debug(requestBody);
            RequestPackage reqP = Json.mapper.readValue(requestBody, RequestPackage.class);

            //校验sessionKey是否合法
            if (SessionMgr.sessionVaild(reqP)) {
                SessionCmd cmd = Json.mapper.readValue(Encrypt.decode(reqP.getReqContext(), reqP.getReqSessionKey()), SessionCmd.class);

                switch (cmd.getAction()){
                    case "unregister":
                        if (SessionMgr.unregister(cmd.getAccount())) {
                            respP.setRespCode("0003");
                        } else {
                            respP.setRespCode("0006");
                        }
                        break;
                    case "getBookInfoList":
                        String bookInfoJson = BookMgr.getBookInfo(cmd.getAccount());
                        if (bookInfoJson != null) {
                            respP.setRespContext(Encrypt.encode(bookInfoJson, reqP.getReqSessionKey()));
                            respP.setRespCode("0030");
                        } else {
                            respP.setRespCode("0034");
                        }
                        break;
                    case "saveBookInfoList":
                        if (BookMgr.saveBookInfo(cmd.getAccount(),cmd.getParam())) {
                            respP.setRespCode("0031");
                        } else {
                            respP.setRespCode("0035");
                        }
                        break;
                }
            } else {
                respP.setRespCode("0009");
            }
        } catch (IOException e) {
            e.printStackTrace();
            respP.setRespCode("9001");
            respP.setRespContext(e.getMessage());
        }
        return respP;
    }

    @RequestMapping(value = "/getBankStream", method = RequestMethod.POST)
    public ResponsePackage getBankStream(@RequestBody String requestBody) {
        ResponsePackage respP = new ResponsePackage();

        try {
            RequestPackage rp = Json.mapper.readValue(requestBody, RequestPackage.class);

            //校验sessionKey是否合法
            if (SessionMgr.sessionVaild(rp)) {
                BankStreamCmd bsCmd = Json.mapper.readValue(Encrypt.decode(rp.getReqContext(), rp.getReqSessionKey()), BankStreamCmd.class);
                String bsJson = BankStream.getBankStream(bsCmd);
                if (bsJson != null) {
                    respP.setRespContext(Encrypt.encode(bsJson, rp.getReqSessionKey()));
                    respP.setRespCode("0020");
                } else {
                    respP.setRespCode("0024");
                }
            } else {
                respP.setRespCode("0009");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respP;
    }

}
