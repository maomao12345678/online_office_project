package com.xxx.server.config.security.component;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 权限控制
 * 判断用户角色
 * 根据不用的用户角色进行不同的操作
 *
 * @author bing  @create 2021/1/16-下午12:47
 */
@Component
public class CustomUrlDecisionManager implements AccessDecisionManager {

    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        for (ConfigAttribute configAttribute : configAttributes) {
            //当前url所需要的角色
            String needRole = configAttribute.getAttribute();
            //如果是这个登录角色
            if ("ROLE_LOGIN".equals(needRole)){
                //看是否已经登陆了
                //看authentication是否属于AnonymousAuthenticationToken,属于这个就是没有登录
                if (authentication instanceof AnonymousAuthenticationToken){
                    throw new AccessDeniedException("尚未登录，请登录");
                }else {
                    return;
                }
            }
            //如果不是ROLE_LOGIN角色
            //判断用户角色是否为url所需角色
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                //判断是不是目标权限用户
                if (authority.getAuthority().equals(needRole)){
                    return;
                }
            }
        }
        //如果整体都不行就抛出异常
        throw new AccessDeniedException("权限不足，请联系管理员");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return false;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }
}