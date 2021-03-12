package com.sword.api;

import com.sword.api.entity.credit.BankStreamCmd;
import com.sword.api.entity.credit.BankStreamReport;
import com.sword.api.entity.base.Account;
import com.sword.api.entity.communicate.SessionlessCmd;
import com.sword.api.entity.communicate.RequestPackage;
import com.sword.api.entity.communicate.ResponsePackage;
import com.sword.api.entity.communicate.SessionKey;
import com.sword.common.Encrypt;
import com.sword.common.Json;
import com.sword.common.Logger;
import okhttp3.*;

/**
 * Credit service api test class
 * @author max
 */
public class ApiTest {
    public static void main(String[] avgs)throws Exception{
        String apiUrl="http://localhost:8088/api/";
        Account account=new Account("olgeer","test");

        SessionlessCmd sessionlessCmd = new SessionlessCmd("olgeer","test","register");

        RequestPackage reqP = new RequestPackage("olgeer","register",Json.mapper.writeValueAsString(sessionlessCmd));
        reqP.encode();
//        reqP.setReqAccount("olgeer");
//        reqP.setReqSessionKey("unuse");
//        reqP.setAction("register");
//        reqP.setReqContext(Encrypt.encode(Json.mapper.writeValueAsString(sessionlessCmd),"unuse"));

        OkHttpClient okHttpClient= new OkHttpClient();
//        MediaType json = MediaType.parse("application/json; charset=utf-8");
//        RequestBody body = RequestBody.create(json, Json.mapper.writeValueAsString(reqP));

        Request request = new Request.Builder()
                .url(apiUrl+"slr")
                .addHeader("content-type","application/json")
                .addHeader("accept","*/*")
                .method("POST",reqP.toBody())
                .build();
        Call call = okHttpClient.newCall(request);

        Response response = call.execute();
        String respJson=response.body().string();
        Logger.console("req="+reqP);
        Logger.console("resp="+respJson);
        /*------------up-----register-----up--------------*/

        reqP.setAction("getSessionKey");
        sessionlessCmd.setAction("getBankStream");
        reqP.setReqContext(Json.mapper.writeValueAsString(sessionlessCmd));
        reqP.encode();

        request = new Request.Builder()
                .url(apiUrl+"slr")
                .addHeader("content-type","application/json")
                .addHeader("accept","*/*")
                .method("POST",reqP.toBody())
                .build();
        call = okHttpClient.newCall(request);

        response = call.execute();
        respJson=response.body().string();
        Logger.console("req="+reqP);
        Logger.console("resp="+respJson);

        ResponsePackage respP=Json.mapper.readValue(respJson,ResponsePackage.class);
        SessionKey sessionKey = Json.mapper.readValue(Encrypt.decode(respP.getRespContext(),"unuse"),SessionKey.class);
        Logger.console("sessionKey="+Json.mapper.writeValueAsString(sessionKey));
        /*-----------up------getSessionKey--------up-----------*/

        BankStreamCmd bsCmd = new BankStreamCmd();
        bsCmd.setCnName("李明");
        reqP = new RequestPackage();
        reqP.setReqAccount("olgeer");
        reqP.setReqSessionKey(sessionKey.getSessionKey());
        reqP.setAction("getBankStream");
        reqP.setReqContext(Json.mapper.writeValueAsString(bsCmd));
        reqP.encode();

        request = new Request.Builder()
                .url(apiUrl+"getBankStream")
                .addHeader("content-type","application/json")
                .addHeader("accept","*/*")
                .method("POST",reqP.toBody())
                .build();
        call = okHttpClient.newCall(request);

        response = call.execute();

        respJson=response.body().string();
        Logger.console("req="+reqP);
        Logger.console("resp="+respJson);

        respP=Json.mapper.readValue(respJson,ResponsePackage.class);
        BankStreamReport bsReport = Json.mapper.readValue(Encrypt.decode(respP.getRespContext(),sessionKey.getSessionKey()),BankStreamReport.class);
        Logger.console(Json.mapper.writeValueAsString(bsReport));
    }
}
