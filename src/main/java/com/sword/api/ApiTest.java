package com.sword.api;

import com.sword.api.entity.communicate.*;
import com.sword.api.entity.credit.BankStreamCmd;
import com.sword.api.entity.credit.BankStreamReport;
import com.sword.api.entity.base.Account;
import com.sword.common.Encrypt;
import com.sword.common.Json;
import com.sword.common.Logger;
import okhttp3.*;

import java.util.List;

/**
 * Credit service api test class
 * @author max
 */
public class ApiTest {
    public static void main(String[] avgs)throws Exception{
        String apiUrl="http://localhost:8088/api/";
        Account account=new Account("olgeer","olgeer");

//        SessionlessCmd sessionlessCmd = new SessionlessCmd("olgeer","olgeer","register");
//
//        RequestPackage reqP = new RequestPackage("olgeer","register",Json.mapper.writeValueAsString(sessionlessCmd));
//        reqP.encode();
////        reqP.setReqAccount("olgeer");
////        reqP.setReqSessionKey("unuse");
////        reqP.setAction("register");
////        reqP.setReqContext(Encrypt.encode(Json.mapper.writeValueAsString(sessionlessCmd),"unuse"));
//
//        OkHttpClient okHttpClient= new OkHttpClient();
////        MediaType json = MediaType.parse("application/json; charset=utf-8");
////        RequestBody body = RequestBody.create(json, Json.mapper.writeValueAsString(reqP));
//
//        Request request = new Request.Builder()
//                .url(apiUrl+"slr")
//                .addHeader("content-type","application/json")
//                .addHeader("accept","*/*")
//                .method("POST",reqP.toBody())
//                .build();
//        Call call = okHttpClient.newCall(request);
//
//        Response response = call.execute();
//        String respJson=response.body().string();
//        Logger.console("req="+reqP);
//        Logger.console("resp="+respJson);
        /*------------up-----register-----up--------------*/
        SessionlessCmd sessionlessCmd = new SessionlessCmd("olgeer","olgeer","getSessionKey");
        RequestPackage reqP = new RequestPackage("olgeer","getSessionKey",Json.mapper.writeValueAsString(sessionlessCmd));
        sessionlessCmd.setAction("getUserSource");
        reqP.setReqContext(Json.mapper.writeValueAsString(sessionlessCmd));
        reqP.encode();

        Request request = new Request.Builder()
                .url(apiUrl+"slr")
                .addHeader("content-type","application/json")
                .addHeader("accept","*/*")
                .method("POST",reqP.toBody())
                .build();
        OkHttpClient okHttpClient= new OkHttpClient();
        Call call = okHttpClient.newCall(request);

        Response response = call.execute();
        String respJson=response.body().string();
        Logger.console("req="+reqP);
        Logger.console("resp="+respJson);

        ResponsePackage respP=Json.mapper.readValue(respJson,ResponsePackage.class);
        if(respP.callSuccess()){
            SessionKey sessionKey = Json.mapper.readValue(Encrypt.decode(respP.getRespContext(),"unuse"),SessionKey.class);
            Logger.console("sessionKey="+Json.mapper.writeValueAsString(sessionKey));

            SessionCmd sessionCmd = new SessionCmd();
            sessionCmd.setAccount("olgeer");
            sessionCmd.setAction("getUserSource");
            sessionCmd.setParam("");
            reqP = new RequestPackage();
            reqP.setReqAccount("olgeer");
            reqP.setReqSessionKey(sessionKey.getSessionKey());
            reqP.setAction("getUserSource");
            reqP.setReqContext(Json.mapper.writeValueAsString(sessionCmd));
            reqP.encode();

            request = new Request.Builder()
                    .url(apiUrl + "getUserSource")
                    .addHeader("content-type", "application/json")
                    .addHeader("accept", "*/*")
                    .method("POST", reqP.toBody())
                    .build();
            call = okHttpClient.newCall(request);

            response = call.execute();

            assert response.body() != null;
            respJson = response.body().string();
            Logger.console("req=" + reqP);
            Logger.console("resp=" + respJson);

            respP = Json.mapper.readValue(respJson, ResponsePackage.class);
            String sourceJson = Encrypt.decode(respP.getRespContext(), sessionKey.getSessionKey());
            List<String> sourceList = Json.mapper.readValue(sourceJson,List.class);
            Logger.console(String.valueOf(sourceList.size()));
            Logger.console(sourceList.get(0));
        }
    }
}
