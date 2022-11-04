package com.xxx.server.config.security;

import com.xxx.server.config.security.component.*;
import com.xxx.server.pojo.Admin;
import com.xxx.server.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.text.FieldPosition;

/**
 * Security 配置类
 *
 * @author bing  @create 2021/1/13-下午9:47
 */
//安全登录配置
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //用户服务
    @Autowired
    private AdminService adminService;

    @Autowired
    private RestAuthorizationEntryPoint restAuthorizationEntryPoint; // 未登录 token 失效时自定义处理结果

    @Autowired
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler; // 无权访问时自定义处理结果

    //权限控制:根据请求 url 分析请求所需的角色
    @Autowired
    private CustomFilter customFilter;

    //根据不用的用户角色进行不同的操作
    @Autowired
    private CustomUrlDecisionManager customUrlDecisionManager;

    // 1、重写 UserDetailsService，用我们自己写的业务逻辑
    @Override
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            //输入username，然后获取用户身份
            Admin admin = adminService.getAdminByUserName(username);
            if (null != admin) {//如果该用户存在
                //进来设置用户的身份，返回用户信息
                admin.setRoles(adminService.getRoles(admin.getId()));
                return admin;
            }
            //没有用户名就抛出错误
            throw new UsernameNotFoundException("用户名或密码不正确");
        };
    }

    // 2、让 Security 走我们重写的 UserDetailsService ，通过 getAdminByUserName 获取用户名
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //把刚刚写的userDetailsService()放入,然后对用户的密码加密
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    // 3、密码加解密对象(加密)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 6、设置放行路径（不走拦截链）,下面有的都会忽略然后放行
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/websocket/**",
                "/**.html",
                "/login/**",
                "//hello/**",
                "/register/**",
                "/logout/**",
                "/css/**",
                "/js/**",
                "/img/**",
                "/fonts/**",
                "favicon.ico",
                "/doc.html",                    // 放行 swagger 资源
                "/webjars/**",                  // 放行 swagger 资源
                "/swagger-resources/**",        // 放行 swagger 资源
                "/v2/api-docs/**",              // 放行 swagger 资源
                "/captcha",      // 验证码接口
                "/ws/**"
        );
    }

    /**
     * 4、SpringSecurity 配置
     *  JWT是用于单点登陆的(分成三个部分组成)
     *  JWT也可以防止CSRF攻击
     *  头部（Header）
     *  负载（Payload）
     *  签名（Signature）
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 使用 JWT结合SpringSecurity , 不需要 csrf
        http.csrf()
                .disable()
                // 基于 token,不需要 session
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 所有请求都要求认证
                .anyRequest()
                .authenticated()
                // 动态权限配置
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                        //根据登录用户看有哪些角色
                        object.setAccessDecisionManager(customUrlDecisionManager);
                        //根据url请求角色
                        object.setSecurityMetadataSource(customFilter);
                        return object;
                    }
                })
                .and()
                // 禁用缓存
                .headers()
                .cacheControl();

        // 添加 jwt 登录授权过滤器(拦截器)
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        // 添加自定义未授权和未登录结果返回
        http.exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler)
                .authenticationEntryPoint(restAuthorizationEntryPoint);
    }

    // 5、JWT 登录授权过滤器
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }
}
