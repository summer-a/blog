package com.hjb.blog.service.common.impl;

import com.hjb.blog.service.common.RedisService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 胡江斌
 * @version 1.0
 * @title: RedisServiceImpl
 * @projectName blog
 * @description: TODO
 * @date 2019/7/20 12:13
 */
@Service
public class RedisServiceImpl<T> implements RedisService<T> {

    @Resource
    private RedisTemplate<String, T> redisTemplate;

    /**
     * 设置缓存
     *
     * @param key
     * @param value
     */
    @Override
    public void set(String key, T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置缓存
     *
     * @param key
     * @param value
     * @param seconds 缓存有效期
     */
    @Override
    public void set(String key, T value, int seconds) {
        redisTemplate.opsForValue().set(key, value, seconds);
    }

    /**
     * 获取缓存
     *
     * @param key
     * @return
     */
    @Override
    public T get(String key) {
        return redisTemplate.opsForValue().get(key);
    }


    /**
     * 删除缓存
     *
     * @param key
     */
    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
