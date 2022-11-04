package com.xxx.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxx.server.mapper.AdminMapper;
import com.xxx.server.mapper.AdminRoleMapper;
import com.xxx.server.mapper.RoleMapper;
import com.xxx.server.pojo.Admin;
import com.xxx.server.pojo.AdminRole;
import com.xxx.server.pojo.Role;
import com.xxx.server.utils.AdminUtils;
import com.xxx.server.utils.RespBean;
import com.xxx.server.service.AdminService;
import com.xxx.server.config.security.component.JwtTokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 管理员表 服务实现类
 * </p>
 *
 * @author Bing
 * @since 2021-01-13
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @Autowired
    private UserDetailsService userDetailsService; // 权限框架的

    @Autowired
    private PasswordEncoder passwordEncoder; // 安全框架-密码加密解密

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    //获取头部信息
    @Value("${jwt.tokenHead}")
    private String tokenHead; // token 头部信息

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private AdminRoleMapper adminRoleMapper;

    /**
     * 登录之后返回 token
     *
     * @param username
     * @param password
     * @param code
     * @param request
     * @return
     */
    @Override
    public RespBean login(String username, String password, String code, HttpServletRequest request) {
        // 校验验证码
        String captcha = (String) request.getSession().getAttribute("captcah");
        if (StringUtils.isEmpty(code) || !captcha.equalsIgnoreCase(code)) {
            return RespBean.error("验证码输入错误，请重新输入！");
        }
        // 登录(主要使用userDetailsService的方法进行用户信息获取)
        UserDetails userDetails = userDetailsService.loadUserByUsername(username); // 调用权限框架方法获取用户名
        // 参数：第一个用户传过来的密码，第二个从 userDetails 中获取的
        if (null == userDetails || !passwordEncoder.matches(password, userDetails.getPassword())) {
            return RespBean.error("用户名或密码不正确");
        }
        if (!userDetails.isEnabled()) {
            return RespBean.error("账号被禁用，请联系管理员！");
        }
        // 自己补充其它判断：账号是否存在......

        // 更新 security 登录用户对象，设置到全局(之后可以用Principal获取值)
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails
                , null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // 登录成功，生成 token(获取令牌--用于登录)
        String token = jwtTokenUtil.generateToken(userDetails);
        Map<String, String> tokenMap = new HashMap<>();
        //把token放入head中,之后都会有带这个head
        tokenMap.put("tokenHead",tokenHead);
        tokenMap.put("token",token);
        //登录成功，把tokenMap返回给前端
        return RespBean.success("登录成功", tokenMap);
    }

    /**
     * 根据用户名获取用户
     *
     * @param username
     * @return
     */
    @Override
    public Admin getAdminByUserName(String username) {
        // 要作空判断，这里为了简单直接返回
        //因为用mybatis-plus,所以直接用QueryWrapper<范型为Admin>()然后去匹配用户名和是否授权登录即可
        return adminMapper.selectOne(new QueryWrapper<Admin>().eq("username", username).eq("enabled", true));
    }

    /**
     * 根据用户 id 查询对应角色列表
     *
     * @param adminId
     * @return
     */
    @Override
    public List<Role> getRoles(Integer adminId) {
        return roleMapper.getRoles(adminId);
    }

    /**
     * 获取所有操作员
     *
     * @param keywords
     * @return
     */
    @Override
    public List<Admin> getAllAdmins(String keywords) {
        return baseMapper.getAllAdmins(AdminUtils.getCurrentAdmin().getId(), keywords);
    }

    /**
     * 更新操作员角色
     *
     * @param adminId
     * @param rids
     * @return
     */
    @Override
    @Transactional // 开启事务(因为首先是要删除数据，然后后面才添加数据)
    public RespBean addAdminRole(Integer adminId, Integer[] rids) {
        // 先删除全部，后调用方法重新全部添加
        adminRoleMapper.delete(new QueryWrapper<AdminRole>().eq("admin_id", adminId));
        Integer result = adminRoleMapper.addAdminRole(adminId, rids);
        if (rids.length == result) {
            return RespBean.success("更新成功！");
        }
        return RespBean.error("更新失败！");
    }

    /**
     * 更新用户密码
     *
     * @param oldPass
     * @param pass
     * @param adminId
     * @return
     */
    @Override
    public RespBean updateAdminPassword(String oldPass, String pass, Integer adminId) {
        //根据id获取对象信息
        Admin admin = baseMapper.selectById(adminId);
        //明文(加密)
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        // 比对密码，判断旧密码是否正确
        if (encoder.matches(oldPass, admin.getPassword())) {
            // 设置密码，并加密
            admin.setPassword(encoder.encode(pass));
            //更新这个admin对象
            int result = baseMapper.updateById(admin);
            if (1 == result) {
                return RespBean.success("更新成功！");
            }
        }
        return RespBean.error("更新失败！");
    }


}
