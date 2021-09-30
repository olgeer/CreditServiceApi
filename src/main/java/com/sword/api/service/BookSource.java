package com.sword.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sword.api.entity.base.Account;
import com.sword.api.entity.bookreader.Source;
import com.sword.api.entity.communicate.SessionCmd;
import com.sword.base.datasource.CommonDAO;
import com.sword.base.datasource.CommonExample;
import com.sword.base.datasource.DataSource;
import com.sword.common.Json;
import com.sword.common.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookSource {
    private static Map<String,String> sources= new HashMap<String, String>();
    private static Logger logger = Logger.newInstance(BookSource.class);
    private static Connection connection;
    private static CommonDAO commonDAO;

    static {
        connection= DataSource.newInstance().getConn();
        commonDAO=new CommonDAO("sources",connection);
    }

    public static void preloadSource(){
        ResultSet rs=commonDAO.selectByExample();
        try {
            while (rs.next()){
                Source source = new Source(rs.getString("source_name"), rs.getString("source_script"),rs.getInt("state")==1);
                source.setLevel(rs.getInt("level"));
                source.setAuthor(rs.getString("author"));
                source.setAuthor_id(rs.getInt("author_id"));
                source.setFree(rs.getInt("free")==1);
                sources.put(source.getSource_name(), Json.mapper.writeValueAsString(source));
            }
            rs.close();
            logger.info("Preload source to cache with "+sources.size()+" record.");
        } catch (SQLException | JsonProcessingException throwables) {
            throwables.printStackTrace();
        }
    }

    public static List<String> getUserSources(int user_id){
        ArrayList<String> userSources=new ArrayList<>();
        ResultSet rs=commonDAO.selectBySql("select * from view_user_source where user_id="+user_id+" or user_id=0");
        try {
            while (rs.next()){
                userSources.add(rs.getString("source_script"));
            }
            rs.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return userSources;
    }

    public static List<String> getUserSources(SessionCmd sessionCmd){
        ArrayList<String> userSources=new ArrayList<>();
        ResultSet rs=commonDAO.selectBySql("select * from view_user_source where user_name='"+sessionCmd.getAccount()+"' or user_name='system'");
        try {
            while (rs.next()){
                userSources.add(rs.getString("source_script"));
            }
            rs.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return userSources;
    }

}
