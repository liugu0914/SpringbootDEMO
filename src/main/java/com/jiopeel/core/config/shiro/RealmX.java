package com.jiopeel.core.config.shiro;

import com.jiopeel.core.constant.Constant;
import com.jiopeel.core.constant.OauthConstant;
import com.jiopeel.core.logic.OauthLogic;
import com.jiopeel.core.util.BaseUtil;
import com.jiopeel.sys.bean.User;
import com.jiopeel.sys.bean.result.MenuResult;
import com.jiopeel.sys.bean.result.PermissionResult;
import com.jiopeel.sys.bean.result.RoleResult;
import com.jiopeel.sys.bean.result.UserResult;
import com.jiopeel.sys.logic.RoleLogic;
import com.jiopeel.sys.logic.UserLogic;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @Description :自定义Realm
 * @auhor:lyc
 * @Date: 2020/7/14 16:37
 */
@Slf4j
public class RealmX extends AuthorizingRealm {

    @Resource
    private UserLogic userLogic;

    @Resource
    private OauthLogic oauthLogic;

    @Resource
    private RoleLogic roleLogic;


    /**
     * 有值即可
     *
     * @param token
     * @return boolean
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token != null;
    }

    /**
     * @Description :登陆,用户名密码校验
     * @Return: AuthenticationInfo
     * @auhor :lyc
     * @Date: 2020/7/14 23:33
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String principal = String.valueOf(authenticationToken.getPrincipal());
        String credentials = String.valueOf(authenticationToken.getCredentials());
        //此处为权限访问
        if (authenticationToken instanceof PermissionToken && OauthConstant.ACCESS_TOKEN.equalsIgnoreCase(credentials)) {
            User user = oauthLogic.getUserByToken(principal);
            SimpleHash simpleHash = new SimpleHash(Sha256Hash.ALGORITHM_NAME, credentials, user.getSalt(), Constant.SALT_TIMES);
            return new SimpleAccount(new UserResult(user),
                    simpleHash.toBase64(),
                    ByteSource.Util.bytes(user.getSalt()),
                    getName());
        }
        //登陆认证 principal为传过来的用户账号
        UserResult user = userLogic.getByAccount(principal);
        if (user == null)
            throw new UnknownAccountException(); // 账号不存在
        if (!Constant.ENABLE_YES.equals(user.getEnable()))
            throw new LockedAccountException();// 账号被禁用/锁定
        String salt = user.getSalt();
        if (BaseUtil.empty(salt)) {
            log.error("账号[{}]盐值为空!", user.getAccount());
            throw new AuthenticationException();
        }
        return new SimpleAuthenticationInfo(user,
                user.getPassword(),
                ByteSource.Util.bytes(salt),
                getName());
    }


    /**
     * @Description :权限认证,定义如何获取用户信息的业务逻辑
     * @Return: AuthenticationInfo
     * @auhor :lyc
     * @Date: 2020/7/14 23:33
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Subject subject = SecurityUtils.getSubject();
        UserResult user = (UserResult) subject.getPrincipal();
//        handelRoleAndPes(user);
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(user.getRoles());
        authorizationInfo.setStringPermissions(user.getPermissions());
        return authorizationInfo;
    }


    /**
     * 获取角色 ，权限 ，菜单
     *
     * @Return: AuthenticationInfo
     * @auhor :lyc
     * @Date: 2020/7/14 23:33
     */
    private void handelRoleAndPes(UserResult user) {
        List<RoleResult> roleList = roleLogic.getRoles(user.getId());
        Set<String> roles = roleLogic.getRoles(roleList);
        List<PermissionResult> permissionList = roleLogic.getPermissionByRoles(roles);
        Set<String> permissions = roleLogic.getPermissions(permissionList);
        List<MenuResult> menus = roleLogic.getMenuByRoles(roles);
        user.setMenus(menus);
        user.setPermissionList(permissionList);
        user.setPermissions(permissions);
        user.setRoleList(roleList);
        user.setRoles(roles);
    }
}
