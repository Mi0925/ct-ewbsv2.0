package cn.comtom.linkage.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.net.UnknownHostException;

@Configuration
public class RedisAutoConfig {

        @Bean
        public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
            /*RedisTemplate<Object, Object> template = new RedisTemplate();
            template.setConnectionFactory(redisConnectionFactory);
            template.setKeySerializer(new StringRedisSerializer());
            template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
            template.setHashKeySerializer(new StringRedisSerializer());
            template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
            template.afterPropertiesSet();
            return template;*/
            RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
            redisTemplate.setConnectionFactory(redisConnectionFactory);

            // 使用Jackson2JsonRedisSerialize 替换默认序列化
            Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
            objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

            jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

            // 设置value的序列化规则和 key的序列化规则
            redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
            redisTemplate.setKeySerializer(new StringRedisSerializer());

            redisTemplate.setHashKeySerializer(jackson2JsonRedisSerializer);
            redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

            redisTemplate.setDefaultSerializer(jackson2JsonRedisSerializer);
            redisTemplate.setEnableDefaultSerializer(true);
            redisTemplate.afterPropertiesSet();
            return redisTemplate;

        }

       @Bean
        public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
            StringRedisTemplate template = new StringRedisTemplate();
            template.setConnectionFactory(redisConnectionFactory);
            template.setKeySerializer(new StringRedisSerializer());
            template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
            template.setHashKeySerializer(new StringRedisSerializer());
            template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
            template.afterPropertiesSet();
            return template;
        }



   /*     @Bean(value="longRedisTemplate")
        public RedisTemplate<String, Long> longRedisTemplate(RedisConnectionFactory redisConnectionFactory ) {
            RedisTemplate<String,Long> template = new RedisTemplate<String,Long>();
            RedisSerializer<String> stringSerializer = new StringRedisSerializer();
            LongSerializer longSerializer = new LongSerializer();
            template.setKeySerializer(stringSerializer);
            template.setValueSerializer(longSerializer);
            template.setHashKeySerializer(stringSerializer);
            template.setHashValueSerializer(longSerializer);
            template.setConnectionFactory(redisConnectionFactory);
            template.afterPropertiesSet();
            return template;
        }*/

}
