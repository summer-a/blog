package com.hjb.blog.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;


/**
 * Redis配置
 *
 * @author 胡江斌
 * @version 1.0
 * @title: RedisConfigurer
 * @projectName blog
 * @description: TODO
 * @date 2019/8/12 10:12
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // 使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
        Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(Object.class);

        MyObjectMapper mapper = new MyObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(mapper);
        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);

        // 使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {

        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);

        MyObjectMapper mapper = new MyObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        //mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(mapper);

        // 序列化配置
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))
                .disableCachingNullValues();

        RedisCacheManager redisCacheManager = RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .transactionAware()
                .build();

        return redisCacheManager;
    }

}
class MyObjectMapper extends ObjectMapper {
    private static final long serialVersionUID = 1L;

    public MyObjectMapper() {
        super();
        // 去掉各种@JsonSerialize注解的解析
        this.configure(MapperFeature.USE_ANNOTATIONS, false);
        // 只针对非空的值进行序列化
        this.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 将类型序列化到属性json字符串中
        //this.enableDefaultTyping(DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        // 对于找不到匹配属性的时候忽略报错
        this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 不包含任何属性的bean也不报错
        this.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

}
