package cn.comtom.ebs.front.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.lang.reflect.Method;
import java.time.Duration;

/**
 * 类名：RedisCacheConfig<br>
 * 描述：Redis缓存配置对象<br>
 */
@Configuration
public class RedisCacheConfig extends CachingConfigurerSupport {

    /**
     * 生成key的策略
     *
     * @return
     */
    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName());
                sb.append(method.getName());
                for (Object obj : params) {
                    sb.append(obj.toString());
                }
                return sb.toString();
            }
        };
    }

    /**
     * 管理缓存
     *
     * @param redisConnectionFactory
     * @return RedisCacheManager
     */
    @SuppressWarnings("rawtypes")
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisSerializationContext.SerializationPair<String> stringSerializationPair = RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer());
        RedisSerializationContext.SerializationPair<Object> objectSerializationPair = RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer());


        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(10)).serializeKeysWith(stringSerializationPair).serializeValuesWith(objectSerializationPair);
        RedisCacheManager.RedisCacheManagerBuilder redisCacheManagerBuilder = RedisCacheManager.builder(redisConnectionFactory).cacheDefaults(redisCacheConfiguration);
//        Set<String> cacheNames = new HashSet<>();
//        cacheNames.add("UserInfo");
//        redisCacheManagerBuilder.initialCacheNames(cacheNames);
        return  redisCacheManagerBuilder.build();
    }


    @Bean
    @SuppressWarnings({"rawtypes", "unchecked"})
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate template = new RedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        template.setEnableTransactionSupport(true);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

}
