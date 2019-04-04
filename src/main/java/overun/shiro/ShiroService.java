package overun.shiro;

import ch.qos.logback.core.encoder.EchoEncoder;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import overun.mapper.PermissionInitMapper;
import overun.model.PermissionInit;

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
     */
    public void updatePermission() {

        synchronized (shiroFilterFactoryBean) {

            AbstractShiroFilter shiroFilter = null;
            try {
                shiroFilter = (AbstractShiroFilter)shiroFilterFactoryBean.getObject();
            } catch (Exception e) {
                throw new RuntimeException( "get ShiroFilter from shiroFilterFactoryBean error!");
            }

            PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver) shiroFilter.getFilterChainResolver();

            DefaultFilterChainManager manager = (DefaultFilterChainManager) filterChainResolver.getFilterChainManager();

            //清空老的权限控制
            manager.getFilterChains().clear();

            shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();

            //重新填入权限
            shiroFilterFactoryBean.setFilterChainDefinitionMap(loadFilterChainDefinitions());

            // 重新构建生成
            Map<String, String> chains = shiroFilterFactoryBean.getFilterChainDefinitionMap();

            for (Map.Entry<String, String> entry : chains.entrySet()) {
                String url = entry.getKey();
                String chainDefinition = entry.getValue().trim()
                        .replace(" ", "");
                manager.createChain(url, chainDefinition);
            }

            System.out.println("更新权限成功！！");
        }
    }
}
