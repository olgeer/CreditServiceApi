package com.sword.api.config;

import com.sword.api.service.SessionMgr;
import com.sword.base.common.Configure;
import com.sword.base.datasource.CommonDAO;
import com.sword.base.datasource.DataSource;
import com.sword.common.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

@Configuration
public class ApiConfig {
    private static Logger logger = Logger.newInstance(ApiConfig.class);
    @Autowired
    private Environment env;

    private void copyProperties(Environment f,Properties t,String k){
        t.setProperty(k,f.getProperty(k));
    }

    private Properties loadProperty(Environment e){
        Properties p= new Properties();
        String prefix=e.getProperty("db");
        copyProperties(e,p,"db");
        copyProperties(e,p,prefix+".driverClassName");
        copyProperties(e,p,prefix+".database");
        copyProperties(e,p,prefix+".url");
        copyProperties(e,p,prefix+".username");
        copyProperties(e,p,prefix+".password");
        copyProperties(e,p,prefix+".defaultAutoCommit");
        copyProperties(e,p,prefix+".initialSize");
        copyProperties(e,p,prefix+".maxActive");
        copyProperties(e,p,prefix+".minIdle");
        copyProperties(e,p,prefix+".maxIdle");
        copyProperties(e,p,prefix+".maxWait");
        copyProperties(e,p,prefix+".removeAbandoned");
        copyProperties(e,p,prefix+".removeAbandonedTimeout");
        copyProperties(e,p,prefix+".testWhileIdle");
        copyProperties(e,p,prefix+".testOnBorrow");
        copyProperties(e,p,prefix+".testOnReturn");
        copyProperties(e,p,prefix+".validationQuery");
        copyProperties(e,p,prefix+".timeBetweenEvictionRunsMillis");
        copyProperties(e,p,prefix+".numTestsPerEvictionRun");
        copyProperties(e,p,prefix+".filters");
        copyProperties(e,p,prefix+".logAbandoned");
        copyProperties(e,p,prefix+".minEvictableIdleTimeMillis");
        return p;
    }

    @PostConstruct
    public void postConstruct(){
        Properties jdbcProperties = loadProperty(env);
        Configure.loadConfig(jdbcProperties);
        SessionMgr.preloadUser();
        logger.info("ApiConfig run !");
    }
}
