package cn.edu.hdu.ssd.core.shiro;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/***
 *  【角色和权限过滤器】
 * */
public class AnyRolesAuthorizationFilter
        extends AuthorizationFilter  //认证过滤器
{
    /***
     *  是否有权限
     * */
    @Override
    protected boolean isAccessAllowed(ServletRequest req, ServletResponse resp, Object rolesArr)
    throws Exception
    {
        //获取主题
        Subject subject = getSubject(req, resp);
        //该主题对应的角色
        String[] rolesArray = (String[]) rolesArr;

        //无需验证角色
        if (rolesArray == null || rolesArray.length == 0)
        {
            return true;
        }

        //判断是否含有指定的校色
        for (int i = 0; i < rolesArray.length; i++)
        {
            if (subject.hasRole(rolesArray[i]))
            {
                return true;
            }
        }

        return false;
    }
}
