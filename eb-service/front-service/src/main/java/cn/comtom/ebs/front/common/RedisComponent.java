package cn.comtom.ebs.front.common;

import cn.comtom.tools.exception.RRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisComponent {

    @Autowired
    private RedisTemplate redisTemplate;


    private RRException writeCacheFailedException(Exception e){
        return new RRException("","写入缓存失败",e);
    }

    /**
     * 写入缓存
     * @param key
     * @param value
     * @return
     */
    public void set(final String key, Object value) {
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
        } catch (Exception e) {
           throw writeCacheFailedException(e);
        }
    }
    /**
     * 写入缓存设置时效时间
     * @param key
     * @param value
     * @return
     */
    public void set(final String key, Object value, Long expireTime) {
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw writeCacheFailedException(e);
        }
    }

    /**
     * 删除对应的value
     * @param key
     */
    public void delete(final String key) {
       // if (exists(key)) {
            redisTemplate.delete(key);
       // }
    }
    /**
     * 判断缓存中是否有对应的key
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    public boolean expire(final String key ,long timeout,TimeUnit unit){
        return redisTemplate.expire(key,timeout,unit);
    }
    /**
     * 读取缓存
     * @param key
     * @return
     */
    public Object get(final String key) {
        Object result = null;
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        return result;
    }

    /**
     * list列表添加一个或多个数据
     * @param k
     * @param v
     */
    public void leftPush(String k,Object v){
        ListOperations<String, Object> list = redisTemplate.opsForList();
        list.leftPush(k,v);
    }

    /**
     * list列表获取
     * @param k
     * @return
     */
    public List<Object> leftPop(String k){
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.range(k,0,list.size(k));
    }

    /**
     * list 移除指定key中集合中指定的值
     * @param k
     * @param v
     */
    public void removeList(String k,Object v){
        ListOperations<String, Object> list = redisTemplate.opsForList();
        list.remove(k,0,v);
    }

    /**
     * list 集合写入缓存(覆盖原有的)
     * @param k
     * @param v
     * @return
     */
    public void setList(final String k, Object v) {
        try {
            ListOperations<String, Object> list = redisTemplate.opsForList();
            delete(k);
            leftPush(k,v);
        } catch (Exception e) {
            throw writeCacheFailedException(e);
        }
    }






    /**
     * 哈希 添加
     * @param key
     * @param hashKey
     * @param value
     */
    public void hmSet(String key, Object hashKey, Object value){
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        hash.put(key,hashKey,value);
    }

    /**
     * 哈希获取数据
     * @param key
     * @param hashKey
     * @return
     */
    public Object hmGet(String key, Object hashKey){
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        return hash.get(key,hashKey);
    }

    /**
     * 有序集合添加
     * @param key
     * @param value
     * @param scoure
     */
    public void zAdd(String key,Object value,double scoure){
        redisTemplate.opsForZSet().add(key,value,scoure);
    }

    /**
     * 有序集合获取
     * @param key
     * @param scoure
     * @param scoure1
     * @return
     */
    public Set<Object> rangeByScore(String key,double scoure,double scoure1){
        return redisTemplate.opsForZSet().rangeByScore(key, scoure, scoure1);
    }


    public void setSet(final String key,Set v){
        try {
            SetOperations so = redisTemplate.opsForSet();
            delete(key);
            if(!v.isEmpty()){
                so.add(key,v.toArray());
                redisTemplate.expire(key, 10, TimeUnit.MINUTES);
            }
        } catch (Exception e) {
            throw writeCacheFailedException(e);
        }
    }

    /**
     * 获取集合交集
     * @param key
     * @param key1
     * @return
     */
    public Set<Object> sIntersect(String key, String key1){
        return redisTemplate.opsForSet().intersect(key, key1);
    }

    public void sAdd(String key,Collection c){
        redisTemplate.opsForSet().add(key,c.toArray());
    }

    public void sAdd(String key,Object... v){
        redisTemplate.opsForSet().add(key,v);
    }

    /**
     * 集合获取
     * @param key
     * @return
     */
    public Set sMembers(String key){
        return redisTemplate.opsForSet().members(key);
    }

    public void sRemove(String key,Collection c){
        redisTemplate.opsForSet().remove(key,c);
    }

    public void sRemove(String key, Object... v){
        redisTemplate.opsForSet().remove(key,v);
    }
}