package com.hjb.blog.field;

/**
 * redis存储的对象名格式字段
 *
 * @author 胡江斌
 * @version 1.0
 * @title: RedisField
 * @projectName blog
 * @description: TODO
 * @date 2019/12/16 18:10
 */
public final class RedisFields {

    /**
     * 表对象名
     */
    public static final String TABLE = "table::%s::%s";

    private RedisFields() {

    }
}
