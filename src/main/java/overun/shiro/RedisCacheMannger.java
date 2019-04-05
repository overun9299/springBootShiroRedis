package overun.shiro;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by ZhangPY on 2019/4/5
 * Belong Organization OVERUN-9299
 * overun9299@163.com
 */
public class RedisCacheMannger implements CacheManager {


    /**
     * cache存活时间
     */
    private Long cacheLive;

    /**
     * cache前缀
     */
    private String cacheKeyPrefix;

    /**
     * redisTemplate
     */
    private RedisTemplate redisTemplate;

    private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<String, Cache>();


    @Override
    public <K, V> Cache<K, V> getCache(String s) throws CacheException {

        Cache cache = this.caches.get(s);
        if (cache == null) {
            //自定义cache
//            cache = ShiroResis
        }
        return null;
    }
}
