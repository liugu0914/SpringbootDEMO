package com.jiopeel.core.logic;


import com.jiopeel.core.base.Base;
import com.jiopeel.core.bean.OauthToken;
import com.jiopeel.core.bean.Page;
import com.jiopeel.core.bean.UserGrant;
import com.jiopeel.core.config.exception.ServerException;
import com.jiopeel.core.constant.Constant;
import com.jiopeel.core.constant.OauthConstant;
import com.jiopeel.core.constant.UserConstant;
import com.jiopeel.core.util.BaseUtil;
import com.jiopeel.sys.bean.User;
import com.jiopeel.sys.bean.form.UserForm;
import com.jiopeel.sys.bean.result.MenuResult;
import com.jiopeel.sys.bean.result.PermissionResult;
import com.jiopeel.sys.bean.result.RoleResult;
import com.jiopeel.sys.bean.result.UserResult;
import com.jiopeel.sys.dao.UserDao;
import com.jiopeel.sys.logic.RoleLogic;
import com.jiopeel.sys.logic.UserLogic;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 登陆信息处理
 */
@Service
public class LoginLogic extends BaseLogic {

    @Resource
    private OauthLogic oauthLogic;
    @Resource
    private UserLogic userLogic;
    @Resource
    private RoleLogic roleLogic;

    /**
     * 注册操作
     *
     * @return
     */
    @Transactional
    public Base addregister(UserForm form) {
        if (BaseUtil.empty(form))
            throw new ServerException("用户对象不能为空");
        if (BaseUtil.empty(form.getAccount()))
            throw new ServerException("账号不能为空");
        if (BaseUtil.empty(form.getPassword()))
            throw new ServerException("密码不能为空");
        if (BaseUtil.empty(form.getEmail()))
            throw new ServerException("邮箱不能为空");
        User userDB = userLogic.getByAccount(form.getAccount());
        if (userDB != null)
            throw new ServerException("该账号已存在");
        User user = userLogic.save(form);
        Set<String> sets =new HashSet<String>();
        sets.add("9051411736301568");//默认为游客id
        userLogic.saveConfigRoles(sets,user.getId());
        return BaseUtil.empty(user) ? Base.suc("注册成功") : Base.fail("注册失败");
    }

    /**
     * 登陆操作
     *
     * @param tmpUser   页面登陆数据
     * @param request
     * @param response
     * @param client_id
     * @return code
     * @Author lyc
     * @Date:2019/12/12 23:42
     */
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public String dologin(User tmpUser, HttpServletRequest request, HttpServletResponse response,
                          String client_id) {
        if (!OauthConstant.local_client_id.equals(client_id))
            throw new ServerException("client_id无法识别");
        String password = tmpUser.getPassword();
        String account = tmpUser.getAccount();
        if (BaseUtil.empty(tmpUser))
            throw new ServerException("用户对象不能为空");
        if (BaseUtil.empty(tmpUser.getAccount()))
            throw new ServerException("账号不能为空");
        if (BaseUtil.empty(password))
            throw new ServerException("密码不能为空");
        Subject subject = SecurityUtils.getSubject();
        try {
            // 在认证提交前准备 token（令牌）
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(account, password);
            subject.login(usernamePasswordToken);
        } catch (UnknownAccountException uae) {
            throw new ServerException("该账户不存在");
        } catch (IncorrectCredentialsException ice) {
            throw new ServerException("密码不正确");
        } catch (LockedAccountException lae) {
            throw new ServerException("账户已被禁用");
        } catch (ExcessiveAttemptsException eae) {
            throw new ServerException("用户名或密码错误次数过多");
        } catch (AuthenticationException ae) {
            throw new ServerException("用户名或密码不正确!");
        }
        UserResult user = (UserResult) subject.getPrincipal();
        handelRoleAndPes(user);

        String charm = BaseUtil.uri2Charm(request.getRequestURI());
        boolean permitted = subject.isPermitted(charm);
        if (!permitted) {
            loginOut(request);
            throw new ServerException("用户没有登陆权限,请联系管理员获取权限!");
        }
        oauthLogic.BoxuserAgent(user.getId(), request);
        //code
        String code = BaseUtil.getUUID();
        OauthToken oauthToken = oauthLogic.RedisCode(user, code);
        oauthLogic.AddTokenCookie(response, oauthToken);
        return code;
    }

    /**
     * 获取角色 ，权限 ，菜单
     *
     * @Return: AuthenticationInfo
     * @auhor :lyc
     * @Date: 2020/7/14 23:33
     */
    public void handelRoleAndPes(UserResult user) {
        List<RoleResult> roleList = roleLogic.getRoles(user.getId());
        Set<String> roles = roleLogic.getRoles(roleList);
        List<PermissionResult> permissionList = roleLogic.getPermissionByRoles(roles);
        Set<String> permissions = roleLogic.getPermissions(permissionList);
        List<MenuResult> menus = roleLogic.getMenuByRoles(roles);
        for (MenuResult menu : menus) {
            if (!BaseUtil.empty(menu.getCharm()))
                permissions.add(menu.getCharm());
        }
        user.setMenus(menus);
        user.setPermissionList(permissionList);
        user.setPermissions(permissions);
        user.setRoleList(roleList);
        user.setRoles(roles);
    }

    /**
     * 退出登陆
     *
     * @param request
     * @Author lyc
     * @Date:2019/12/12 23:42
     */
    public void loginOut(HttpServletRequest request) {
        oauthLogic.loginOut(request);
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated())
            subject.logout();
    }
}
