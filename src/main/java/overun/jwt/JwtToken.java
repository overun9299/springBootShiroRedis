package overun.jwt;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @ClassName: JwtToken
 * @Description:
 * @author: 薏米滴答-西安-zhangPY
 * @version: V1.0
 * @date: 2019/4/11 14:33
 * @Copyright: 2019 www.yimidida.com Inc. All rights reserved.
 */
public class JwtToken implements AuthenticationToken {

    private String token;

    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
