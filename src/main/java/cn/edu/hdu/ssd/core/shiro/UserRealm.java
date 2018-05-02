package cn.edu.hdu.ssd.core.shiro;

import cn.edu.hdu.ssd.common.log.Logger;
import cn.edu.hdu.ssd.common.ret.RolesAndPermsReturner;
import cn.edu.hdu.ssd.model.ShiroToken;
import cn.edu.hdu.ssd.model.User;
import cn.edu.hdu.ssd.service.user.IUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;


/***
 *  【用户安全域】
 * */
public class UserRealm
        extends AuthorizingRealm//继承安全域
{
    public static Logger logger = Logger.getLogger(UserRealm.class);

    @Autowired
    IUserService userService;

    /***
     *  【角色信息】
     * */
    @SuppressWarnings("unused")
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authRoleToken)
    throws AuthenticationException
    {
        //转换成shrio的token
        ShiroToken token = (ShiroToken) authRoleToken;
        //获取登陆的用户名
        String loginUsername = token.getUsername();

        //从数据库中加载用户
        User user = loadUser(loginUsername);

        //检查用户
        checkUserAuth(token, user);

        // 登陆成功
        logger.info("{} login success.", loginUsername);
        //返回简单认证校色信息
        return new SimpleAuthenticationInfo(authRoleToken, user.getPassword(), loginUsername);
    }

    /***
     *  【用户权限】
     * */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalsColl)
    {
        ShiroToken token = (ShiroToken) SecurityUtils.getSubject().getPrincipal();
        String username = token.getUsername();

        //加载角色和权限
        RolesAndPermsReturner rolesAndPermsReturner = loadRolesAndPersms();

        //生成简单权限信息
        SimpleAuthorizationInfo sainfo = new SimpleAuthorizationInfo();
        sainfo.setRoles(rolesAndPermsReturner.getRolesSet());
        sainfo.setStringPermissions(rolesAndPermsReturner.getPermsSet());
        return sainfo;
    }

    /***
     *  从数据库中加载用户
     * */
    private User loadUser(String loginUsername)
    {
        // 根据username从数据库查找用户，得到密码
        // 假设找到的用户如下
        // User user = userService.findByUsername(username)
        User user = new User();
        user.setName(loginUsername);
        user.setPassword("21232f297a57a5a743894a0e4a801fc3"); // 数据库中的密码md5加密的
        return user;
    }

    /***
     *  检查用户
     * */
    private void checkUserAuth(ShiroToken token, User user)
    {
        if (null == user)
        {
            throw new AccountException("username is not exist");
        }

        if (!user.getPassword().equals(token.getPswd()))
        {
            throw new AccountException("password is not right");
        }
    }

    /***
     *  加载角色和权限
     * */
    private RolesAndPermsReturner loadRolesAndPersms()
    {
        Set<String> rolesSet = new HashSet<String>();
        rolesSet.add("admin");
        //roles.add("role1");
        Set<String> permissionsSet = new HashSet<String>();
        permissionsSet.add("add");
        permissionsSet.add("delete");

        return new RolesAndPermsReturner(rolesSet,permissionsSet);
    }
}
