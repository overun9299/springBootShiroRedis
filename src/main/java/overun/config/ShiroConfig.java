package overun.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import overun.mapper.PermissionInitMapper;
import overun.model.PermissionInit;
import overun.shiro.MyShiroRealm;
import org.apache.shiro.mgt.SecurityManager;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZhangPY on 2019/3/31
 * Belong Organization OVERUN-9299
 * overun9299@163.com
 */

@Configuration
public class ShiroConfig {

    /**
     *  ShiroFilterFactoryBean 处理拦截资源文件问题。
     *  注意：单独一个ShiroFilterFactoryBean配置是或报错的，因为在初始化ShiroFilterFactoryBean的时候需要注入：SecurityManager
     *  Filter Chain定义说明 1、一个URL可以配置多个Filter，使用逗号分隔 2、当设置多个过滤器时，全部验证通过，才视为通过
     *  部分过滤器可指定参数，如perms，roles
     */

    @Autowired
    private PermissionInitMapper permissionInitMapper;

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${redis.expire}")
    private int expire;


    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {

        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 必须设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setLoginUrl("/login");
        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/index");
        // 未授权界面;
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");

        // 拦截器.
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
//        // 配置不会被拦截的链接 顺序判断
//        filterChainDefinitionMap.put("/static/**", "anon");
//        filterChainDefinitionMap.put("/ajaxLogin", "anon");
//
//        // 配置退出过滤器,其中的具体的退出代码Shiro已经替我们实现了
//        filterChainDefinitionMap.put("/logout", "logout");
//
//        filterChainDefinitionMap.put("/add", "perms[权限添加]");
//
//        // <!-- 过滤链定义，从上向下顺序执行，一般将 /**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
//        // <!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
//        filterChainDefinitionMap.put("/**", "authc");

        List<PermissionInit> permissionInits = permissionInitMapper.selectByExample(null);
        for (PermissionInit p : permissionInits ) {
            filterChainDefinitionMap.put(p.getUrl(),p.getPermissionInit());
        }

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        System.out.println("Shiro拦截器工厂类注入成功");
        return shiroFilterFactoryBean;
    }


    @Bean
    public SecurityManager securityManager() {

        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 设置realm.
        securityManager.setRealm(myShiroRealm());
        // 自定义缓存实现 使用redis
        securityManager.setCacheManager(cacheManager());
        // 自定义session管理 使用redis
        securityManager.setSessionManager(sessionManager());

        return securityManager;
    }




    /**
     * 身份认证realm; (这个需要自己写，账号密码校验；权限等)
     *
     * @return
     */
    @Bean
    public MyShiroRealm myShiroRealm() {

        MyShiroRealm myShiroRealm = new MyShiroRealm();
        return myShiroRealm;
    }

    /**
     * html页面支持shiro标签
     * @return
     */
    @Bean
    public ShiroDialect getShiroDialect() {

        return new ShiroDialect();
    }

    /**
     *  配置shiro redisManager
     * @return
     */
    @Bean
    public RedisManager redisManager() {

        RedisManager redisManager = new RedisManager();
        //  ip
//        redisManager.setHost(redisHost);
        redisManager.setHost("localhost");
        //  端口
//        redisManager.setPort(redisPort);
        redisManager.setPort(6379);
        //  过期时间
//        redisManager.setExpire(expire);
        //  超时时间
        // redisManager.setTimeout(timeout);
        //  密码
        // redisManager.setPassword(password);

        return redisManager;
    }

    /**
     * cacheManager 缓存 redis实现
     * @return
     */
    @Bean
    public RedisCacheManager cacheManager() {

        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }

    /**
     * RedisSessionDAO shiro sessionDao层的实现 通过redis
     * @return
     */
    @Bean
    public RedisSessionDAO redisSessionDAO() {

        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        return redisSessionDAO;
    }

    /**
     * shiro session的管理
     * @return
     */
    @Bean
    public DefaultWebSessionManager sessionManager() {

        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(redisSessionDAO());
        return sessionManager;
    }

}
