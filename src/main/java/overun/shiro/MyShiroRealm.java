package overun.shiro;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import overun.jwt.JwtToken;
import overun.mapper.PermissionMapper;
import overun.mapper.UserMapper;
import overun.model.Permission;
import overun.model.User;
import overun.model.UserExample;
import overun.service.UserService;
import overun.utils.JWTUtil;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ZhangPY on 2019/3/31
 * Belong Organization OVERUN-9299
 * overun9299@163.com
 */
public class MyShiroRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PermissionMapper permissionMapper;


    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }


    /**
     * 不使用jwt
     * 认证信息.(身份验证) : Authentication 是用来验证用户身份
     * @param authcToken
     * @return
     * @throws AuthenticationException
     */
    /**
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {

        System.out.println("身份认证方法：MyShiroRealm.doGetAuthenticationInfo()");
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        String username = token.getUsername();
        String password = String.valueOf(token.getPassword());
        User user = null;
        UserExample example = new UserExample();
        example.or().andNicknameEqualTo(username);
        List<User> users = userService.selectByExample(example);
        if (users.size() > 0) {
            user = users.get(0);
        }
        if (user == null) {
            throw new AccountException("帐号或密码不正确！");
        } else if (Long.valueOf(0).equals(user.getStatus())) {
            throw new DisabledAccountException("帐号已经禁止登录！");
        } else {
            //更新登录时间 last login time
            user.setLastLoginTime(new Date());
            userMapper.updateByPrimaryKey(user);
        }
        //获取盐值
        ByteSource byteSource = ByteSource.Util.bytes(username);
        return new SimpleAuthenticationInfo(user, user.getPswd(),byteSource, getName());
    }
    */


    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String token = (String) auth.getCredentials();
        // 解密获得username，用于和数据库进行对比
        String username = JWTUtil.getUsername(token);
        if (username == null) {
            throw new AuthenticationException("token无效");
        }

        UserExample userExample = new UserExample();
        userExample.or().andNicknameEqualTo(username);
        List<User> users = userService.selectByExample(userExample);
        if ( users.size() <= 0) {
            throw new AuthenticationException("用户不存在!");
        }

        if (!JWTUtil.verify(token, username, users.get(0).getPswd())) {
            throw new AuthenticationException("用户名或密码错误");
        }

        return new SimpleAuthenticationInfo(users.get(0), users.get(0), "my_realm");
    }

    /**
     * 授权:例如按钮类授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        System.out.println("权限认证方法：MyShiroRealm.doGetAuthorizationInfo()");
        /** 目前这种方式是不使用jwt时可以获取到user  ，使用了jwt后这块就会报错需要用JWTUtils通过token获取用户名在查询出user */
        User token = (User) SecurityUtils.getSubject().getPrincipal();
        Long userId = token.getId();
        SimpleAuthorizationInfo info =  new SimpleAuthorizationInfo();

        //实际开发，当前登录用户的角色和权限信息是从数据库来获取的，我这里写死是为了方便测试
//        Set<String> roleSet = new HashSet<String>();
//        roleSet.add("user:add");
//        info.setRoles(roleSet);

        //从数据库获取按钮权限
        List<Permission> permissions = permissionMapper.selectByExample(null);
        Set<String> permissionSet = new HashSet<String>();
        for (Permission p : permissions) {
            if (StringUtils.isNotEmpty(p.getUrl())) {
                permissionSet.add(p.getUrl());
            }
        }
        info.setStringPermissions(permissionSet);

        return info;
    }

    public void clearAuthorization() {
        this.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
    }
}
