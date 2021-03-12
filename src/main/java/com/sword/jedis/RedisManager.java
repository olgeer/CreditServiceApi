package com.sword.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Redis manager class
 * @author max
 */
public class RedisManager {
    /**
     * Redis服务器IP
     */
    private String addr = "127.0.0.1";

    /**Redis的端口号*/
    private int port = 6379;

    /**访问密码*/
    private String auth = "my_redis";

    /**
     * 可用连接实例的最大数目，默认值为8；
     * 如果赋值为-1，则表示不限制；
     * 如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
     */
    private static int MAX_ACTIVE = 256;

    /**控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。*/
    private static int MAX_IDLE = 20;

    /**等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；*/
    private static int MAX_WAIT = 2000;

    private static int TIMEOUT = 2000;

    /**在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；*/
    private static boolean TEST_ON_BORROW = true;

    private RedisConfig jedisPoolConfig;

    private static JedisPool jedisPool = null;

    public RedisManager(){

    }
    public RedisManager(RedisConfig redisConfig){
        this.jedisPoolConfig=redisConfig;
    }

    public RedisManager(String addr,int port,String auth) {
        this.addr=addr;
        this.port=port;
        this.auth=auth;
    }

    public RedisConfig getJedisPoolConfig() {
        return jedisPoolConfig;
    }

    public void setJedisPoolConfig(RedisConfig jedisPoolConfig) {
        this.jedisPoolConfig = jedisPoolConfig;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public static RedisManager newInstance(){
        return new RedisManager();
    }

    public static RedisManager newInstance(RedisConfig redisConfig){
        return new RedisManager(redisConfig);
    }

    public static RedisManager newInstance(String addr,int port,String auth){
        return new RedisManager(addr,port,auth);
    }

    /**
     * 初始化Redis连接池
     */
    private void connectRedis(){
        try {
            if(jedisPool==null) {
                if(jedisPoolConfig==null) {
                    jedisPoolConfig = new RedisConfig();
                    jedisPoolConfig.setMaxTotal(MAX_ACTIVE);
                    jedisPoolConfig.setMaxIdle(MAX_IDLE);
                    jedisPoolConfig.setMaxWaitMillis(MAX_WAIT);
                    jedisPoolConfig.setTestOnBorrow(TEST_ON_BORROW);
                    jedisPoolConfig.setRedisServer(addr);
                    jedisPoolConfig.setRedisPort(port);
                    jedisPoolConfig.setTimeout(TIMEOUT);
                    //jedisPoolConfig.setRequirePass(auth);
                }

                if(jedisPoolConfig.getRequirePass()==null) {
                    jedisPool = new JedisPool(jedisPoolConfig, jedisPoolConfig.getRedisServer(), jedisPoolConfig.getRedisPort());
                }else{
                    jedisPool = new JedisPool(jedisPoolConfig, jedisPoolConfig.getRedisServer(), jedisPoolConfig.getRedisPort()
                                                ,jedisPoolConfig.getTimeout(),jedisPoolConfig.getRequirePass());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取Jedis实例
     *
     * @return
     */
    public synchronized Jedis getJedis() {
        Jedis jedis=null;
        if(jedisPool==null){
            connectRedis();
        }

        try {
            if (jedisPool != null) {
                Jedis resource = jedisPool.getResource();
                jedis= resource;
            } else {
                jedis= null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            jedis=null;
        }
        return jedis;
    }

    /**
     * 释放jedis资源
     *
     * @param jedis
     */
    public static void returnResource(Jedis jedis) {
        if (jedis != null) {
            //jedisPool.returnResource(jedis);
            jedis.close();
        }
    }

    /**
     * Disconnect to redis
     */
    public static void freeAllResource() {
        if(jedisPool!=null) {
            jedisPool.close();
        }
    }

    /**
     * Main test function
     * @param args
     */
    public static void main(String[] args) {
        try {
            Jedis jedis = RedisManager.newInstance("127.0.0.1", 6379, null).getJedis();
            jedis.set("test", "98574763764");
            System.out.println(jedis.get("test"));
            RedisManager.returnResource(jedis);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            RedisManager.freeAllResource();
        }
    }
}
