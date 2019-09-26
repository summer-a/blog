package com.hjb.blog.service.common;

/**
 * Redis接口
 *
 * @author 胡江斌
 * @version 1.0
 * @title: RedisService
 * @projectName blog
 * @description: TODO
 * @date 2019/7/20 12:12
 */
public interface RedisService<T> {
    /**
     * 设置缓存
     *
     * @param key
     * @param value
     */
    void set(String key, T value);

    /**
     * 设置缓存
     *
     * @param key
     * @param value
     * @param seconds 缓存有效期
     */
    void set(String key, T value, int seconds);

    /**
     * 获取缓存
     *
     * @param key
     * @return
     */
    T get(String key);

    /**
     * 删除缓存
     *
     * @param key
     */
    void delete(String key);

    /**
     * hash get
     *
     * @param hkey
     * @param key
     * @return
     */
    T hget(String hkey, String key);

    /**
     * hash set
     *
     * @param hkey
     * @param key
     * @param value
     */
    void hset(String hkey, String key, T value);

}