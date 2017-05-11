package com.tqmall.monkey.client.shiro;

import com.tqmall.monkey.client.user.UserService;
import com.tqmall.monkey.domain.entity.UserDO;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by ruibai on 15/3/31.
 * 可参考http://my.oschina.net/miger/blog/283526
 *
 */

@Component
@Slf4j
public class MonkeyJdbcRealm  extends JdbcRealm {
    @Resource
    private UserService userService;

    private final static String userKey = "currentUser";
    //登录认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
//        清除之前的缓存
//        AuthenticationInfo info = super.doGetAuthenticationInfo(token);
//        clearCache(info.getPrincipals());

        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        String username = String.valueOf(usernamePasswordToken.getUsername());
        String password = String.valueOf(usernamePasswordToken.getPassword());
        UserDO checkUser = new UserDO();
        checkUser.setUserName(username.trim());
        checkUser.setPassWord(password);
        usernamePasswordToken.setRememberMe(true);
        boolean isExist = userService.checkExistUser(checkUser);
        AuthenticationInfo authenticationInfo = null;
        if(isExist){
            UserDO user = userService.getUserDO(username);
            authenticationInfo = new SimpleAuthenticationInfo(username, password, this.getName());
            this.setSession(userKey, user);
            return authenticationInfo;
        }else{
            throw new IncorrectCredentialsException(); //如果密码错误
        }




    }


    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

//        String currentUsername = (String)super.getAvailablePrincipal(principals);
//         String username1 = (String) principals.getPrimaryPrincipal();
        String username = (String) principals.fromRealm(this.getName()).iterator().next();

        //实际中可能会像上面注释的那样从数据库取得
        if(null !=  username ){
            String key = username+"simpleAuthorInfo";
            SimpleAuthorizationInfo simpleAuthorInfo = (SimpleAuthorizationInfo)this.getSessionValue(key);
            if(null == simpleAuthorInfo) {
                simpleAuthorInfo = new SimpleAuthorizationInfo();
                simpleAuthorInfo.setRoles(userService.findRoles(username));
                simpleAuthorInfo.setStringPermissions(userService.findPermissions(username));

                this.setSession(key, simpleAuthorInfo);
            }
            return simpleAuthorInfo;
        }
        //若该方法什么都不做直接返回null的话,就会导致任何用户访问时都会自动跳转到unauthorizedUrl指定的地址
        //详见applicationContext.xml中的<bean id="shiroFilter">的配置
        return null;

    }

    /**
     * 将一些数据放到ShiroSession中,以便于其它地方使用
     *   比如Controller,使用时直接用HttpSession.getAttribute(key)就可以取到
     */
    public void setSession(Object key, Object value){
        Subject currentUser = SecurityUtils.getSubject();
        if(null != currentUser){
            Session session = currentUser.getSession();
//            log.debug("Set Session默认超时时间为[" + session.getTimeout() + "]毫秒");
            if(null != session){
                session.setAttribute(key, value);
            }
        }
    }

    /**
     * 根据key值获得值
     * @param key
     * @return
     */
    public Object getSessionValue(Object key){
        Subject currentUser = SecurityUtils.getSubject();
        if(null != currentUser){
            Session session = currentUser.getSession();
//            log.debug("Get Session默认超时时间为[" + session.getTimeout() + "]毫秒");
            if(null != session){
                return session.getAttribute(key);
            }
        }
        return null;
    }

    public  UserDO getCurrentUser(){
        UserDO userDO = (UserDO) this.getSessionValue(userKey);
        return userDO;
    }
}
