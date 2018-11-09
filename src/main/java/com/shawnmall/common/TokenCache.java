package com.shawnmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @program: ShawnMall
 * @description: TokenCache using google guava
 * @author: Shawn Li
 * @create: 2018-08-15 15:02
 **/

public class TokenCache {
    private static Logger logger = LoggerFactory.getLogger(TokenCache.class);
    public static final String TOKEN_PREFIX = "token_";
    //LRU algorithm is used if the cache reaches the max capacity
    private static LoadingCache<String, String> localCache
            = CacheBuilder.newBuilder().initialCapacity(1000).maximumSize(10000).expireAfterAccess(12, TimeUnit.HOURS)
            .build(new CacheLoader<String, String>() {
                //default loading method, when call get method, if there is no key, call this method.
                @Override
                public String load(String s) {
                    return null;
                }
            });

    public static void setKey(String key, String value) {
        localCache.put(key, value);
    }
    public static String getKey(String key) {
        String value;
        try {
            value = localCache.get(key);
            if ("null".equals(value)) {
                return null;
            }
            return value;
        } catch (Exception e){
            logger.error("local cache get error", e);
        }
        return null;
    }
}
