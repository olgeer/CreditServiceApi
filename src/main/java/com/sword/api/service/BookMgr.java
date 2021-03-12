package com.sword.api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookMgr {
    private static Map<String,String> faverBookInfos=new HashMap<>();

    public static boolean saveBookInfo(String account,String bookinfo){
        boolean saveSuccess=false;
        if(bookinfo!=null){
            faverBookInfos.put(account,bookinfo);
            saveSuccess=true;
        }
        return saveSuccess;
    }

    public static String getBookInfo(String account){
        return faverBookInfos.get(account);
    }

    public static List<String> getBookInfoList(int length){
        List<String> resultArray = new ArrayList<>();
        int processIds=0;
        for (String account : faverBookInfos.keySet()) {
            if (processIds >= length) break;
            resultArray.add(account);
        }
        return resultArray;
    }
}
