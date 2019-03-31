package overun.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import overun.mapper.UserMapper;
import overun.model.User;
import overun.model.UserExample;
import overun.service.UserService;

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


    /**
     * 认证信息.(身份验证) : Authentication 是用来验证用户身份
     * @param authcToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {

        System.out.println("身份认证方法：MyShiroRealm.doGetAuthenticationInfo()");
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        String username = token.getUsername();
        String password = String.valueOf(token.getPassword());
        User user = null;
        UserExample example = new UserExample();
        example.or().andNicknameEqualTo(username).andPswdEqualTo(password);
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
        return new SimpleAuthenticationInfo(user, user.getPswd(), getName());
    }

    /**
     * 授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        System.out.println("权限认证方法：MyShiroRealm.doGetAuthenticationInfo()");
        User token = (User) SecurityUtils.getSubject().getPrincipal();
        Long userId = token.getId();
        SimpleAuthorizationInfo info =  new SimpleAuthorizationInfo();

        //实际开发，当前登录用户的角色和权限信息是从数据库来获取的，我这里写死是为了方便测试
        Set<String> roleSet = new HashSet<String>();
        roleSet.add("100002");
        info.setRoles(roleSet);

        Set<String> permissionSet = new HashSet<String>();
        permissionSet.add("权限添加");
        info.setStringPermissions(permissionSet);

        return info;
    }
}
