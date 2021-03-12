package com.sword.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sword.api.entity.credit.BankStreamCmd;
import com.sword.api.entity.credit.BankStreamReport;

/**
 * Bank stream list get
 * @author max
 */
public class BankStream {
    public static String getBankStream(BankStreamCmd bsCmd) throws Exception{
        BankStreamReport bsReport = new BankStreamReport();
        bsReport.setCnName("李明");
        bsReport.setIdNo("440107199802174209");
        bsReport.setAge(30);
        bsReport.setSex("男");
        bsReport.setBirthday("1998-02-17");
        bsReport.setNativePlace("广州");

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(bsReport);
    }
}
