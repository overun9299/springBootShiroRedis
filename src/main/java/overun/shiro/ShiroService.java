package overun.shiro;

import ch.qos.logback.core.encoder.EchoEncoder;
import javafx.application.Application;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.Session;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import overun.mapper.PermissionInitMapper;
import overun.model.PermissionInit;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName:
 * @Description:
 * @author: 薏米滴答-西安
 * @version: V1.0
 * @date: 2019/04/02 17:42
 * @Copyright: 2019 www.yimidida.com Inc. All rights reserved.
 */
@Service
public class ShiroService {

    @Autowired
    private PermissionInitMapper permissionInitMapper;

    @Autowired
    private ShiroFilterFactoryBean shiroFilterFactoryBean;

//    private static RedisSessionDAO redisSessionDAO = ApplicationContext

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 重新获取权限
     * @return
     */
    public Map<String,String> loadFilterChainDefinitions() {

        // 权限控制map.从数据库获取
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        List<PermissionInit> list = permissionInitMapper.selectByExample(null);

        for (PermissionInit permissionInit : list) {
            filterChainDefinitionMap.put(permissionInit.getUrl(),
                    permissionInit.getPermissionInit());
        }
        return filterChainDefinitionMap;
    }



    /**
     * 重新加载权限
     * 清除原有授权，重新加载权限认证方法：MyShiroRealm.doGetAuthorizationInfo()
     */
    public void updatePermission() {

        RealmSecurityManager securityManager = (RealmSecurityManager) shiroFilterFactoryBean.getSecurityManager();

        MyShiroRealm next = (MyShiroRealm) securityManager.getRealms().iterator().next();
        next.clearAuthorization();
    }
}
