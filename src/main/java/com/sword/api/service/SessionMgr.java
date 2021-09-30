package com.sword.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sword.api.entity.base.Account;
import com.sword.api.entity.communicate.SessionlessCmd;
import com.sword.api.entity.communicate.RequestPackage;
import com.sword.api.entity.communicate.SessionKey;
import com.sword.base.datasource.CommonDAO;
import com.sword.base.datasource.CommonExample;
import com.sword.base.datasource.DataSource;
import com.sword.common.Json;
import com.sword.common.Logger;
import com.sword.common.ShortKey;
//import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * SessionManager
 * @author max
 */
public class SessionMgr {
//    private static final String OPENSESSIONS = "OpenSessions";

    /**
     * Session 过期时间，单位：秒
     */
    private static final int SESSIONTIMEOUT = 120;

    private static Logger logger = Logger.newInstance(SessionMgr.class);

//    private static Jedis jedis;

    private static Map<String,String> accounts= new HashMap<String, String>();
    private static Map<String,String> sessions= new HashMap<String, String>();
    private static List<String> sessionQueue= new ArrayList<>();
    private static Connection connection;
    private static CommonDAO commonDAO;

    static {
//        jedis = RedisManager.newInstance("127.0.0.1", 6379, null).getJedis();
        accounts.put("olgeermax","{\"account\":\"olgeermax\",\"password\":\"test\",\"status\":true}");
        connection= DataSource.newInstance().getConn();
        commonDAO=new CommonDAO("user",connection);
    }

    public static List<String> getAccountList(int length){
        List<String> resultArray = new ArrayList<>();
        int processIds=0;
        for (String account : accounts.keySet()) {
            if (processIds >= length) break;
            resultArray.add(account);
        }
        return resultArray;
    }

    public static boolean login(String account,String password){
        Account tmpAccount=new Account(account,password);
        return tmpAccount.auth();
    }

    public static void preloadUser(){
        ResultSet rs=commonDAO.selectByExample();
        try {
            while (rs.next()){
                Account tmpAccount = new Account(rs.getString("user_name"), rs.getString("password"));
                accounts.put(tmpAccount.getAccount(),Json.mapper.writeValueAsString(tmpAccount));
            }
            rs.close();
            logger.info("Preload user to cache with "+accounts.size()+" record.");
        } catch (SQLException | JsonProcessingException throwables) {
            throwables.printStackTrace();
        }
    }

    public static boolean register(String account,String password) {
        boolean success=false;

        try {
            if(!isExist(account)) {
                CommonExample commonExample=new CommonExample();
                CommonExample.Criteria valueCriteria=commonExample.createValueCriteria();
                valueCriteria.addKeyValue("user_name",account);
                valueCriteria.addKeyValue("password",password);
                valueCriteria.addKeyValue("status",1);
                commonDAO.insertByExample(commonExample);

                Account tmpAccount = new Account(account, password);
                accounts.put(account, Json.mapper.writeValueAsString(tmpAccount));
                success = true;
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return success;
    }

    public static boolean unregister(String account) {
        boolean success=false;
        try {
            accounts.remove(account);
            success=true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    public static boolean isExist(String account) {
        boolean success=false;
        try {
            if (accounts.get(account)!=null)success=true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    public static boolean auth(String account, String password) {
        boolean pass = false;
        if (account != null && password != null) {

//            String accountInfo = jedis.get(account);
            String accountInfo = accounts.get(account);

            Account authAccount = null;
            try {
                authAccount = Json.mapper.readValue(accountInfo, Account.class);
                if (authAccount != null && authAccount.isStatus()) {
                    if (password.equals(authAccount.getPassword())) {
                        pass = true;
                    }
                }
            } catch (Exception e) {
                pass = false;
            }
        }
        return pass;
    }

    public static SessionKey newSession(SessionlessCmd sessionlessCmd) {
        SessionKey sessionKey = null;
        if (auth(sessionlessCmd.getAccount(), sessionlessCmd.getPassword())) {
            sessionKey = new SessionKey();
            sessionKey.setSessionKey(ShortKey.newKey());
            sessionKey.setAccount(sessionlessCmd.getAccount());
            sessionKey.setAction(sessionlessCmd.getAction());
            try {
//                jedis.set(sessionKey.getSessionKey(), Json.mapper.writeValueAsString(sessionKey));
//                jedis.lpush(OPENSESSIONS, sessionKey.getSessionKey());
                sessions.put(sessionKey.getSessionKey(), Json.mapper.writeValueAsString(sessionKey));
                sessionQueue.add(sessionKey.getSessionKey());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return sessionKey;
    }

    public static boolean sessionVaild(RequestPackage rp) {
        boolean isVaild = false;
        try {
//            String sessionJson = jedis.get(rp.getReqSessionKey());
            String sessionJson = sessions.get(rp.getReqSessionKey());
            SessionKey sessionKey = Json.mapper.readValue(sessionJson, SessionKey.class);
            if (rp.getAction().equals(sessionKey.getAction())
                    && rp.getReqAccount().equals(sessionKey.getAccount())
            ) {
                isVaild = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isVaild;
    }

    private static boolean isTimeout(Date sessionDate) {
        return (new Date()).getTime() - sessionDate.getTime() > SESSIONTIMEOUT * 1000;
    }

    private static void cleanOvertimeSession() {
//        String firstKey = null;
//        boolean endOfStack = false;
        SessionKey sessionKey = null;
//        do {
//            key = jedis.rpop(OPENSESSIONS);
//            try {
//                if (key != null && !key.equals(firstKey)) {
//                    sessionKey = Json.mapper.readValue(jedis.get(key), SessionKey.class);
//                    if (isTimeout(sessionKey.getCreateTime())) {
//                        jedis.del(key);
//                        logger.debug("Del overtime seesion like " + key);
//                    } else {
//                        jedis.lpush(key, OPENSESSIONS);
//                        if (firstKey == null) {
//                            firstKey = key;
//                        }
//                    }
//                } else {
//                    endOfStack = true;
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } while (!endOfStack);

        for(String key:sessionQueue){
            try{
                sessionKey = Json.mapper.readValue(sessions.get(key), SessionKey.class);
                if (isTimeout(sessionKey.getCreateTime())) {
                    sessionQueue.remove(key);
                    sessions.remove(key);
                    logger.debug("Del overtime seesion like " + key);
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }

        logger.debug("Clean overtime session job done !");
    }

    public static void main(String[] args) {
        SessionMgr.cleanOvertimeSession();
//        RedisManager.freeAllResource();
    }
}
