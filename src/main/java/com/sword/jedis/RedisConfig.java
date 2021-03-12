package com.sword.jedis;

import redis.clients.jedis.JedisPoolConfig;

/**
 * Redis config extends from JedisPoolConfig
 * @author max
 */
public class RedisConfig extends JedisPoolConfig {
    private String redisServer;
    private int redisPort = 6379;
    private String requirePass;
    private int timeout = 2000;

    public String getRedisServer() {
        return redisServer;
    }

    public void setRedisServer(String redisServer) {
        this.redisServer = redisServer;
    }

    public int getRedisPort() {
        return redisPort;
    }

    public void setRedisPort(int redisPort) {
        this.redisPort = redisPort;
    }

    public String getRequirePass() {
        return requirePass;
    }

    public void setRequirePass(String requirePass) {
        this.requirePass = requirePass;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
