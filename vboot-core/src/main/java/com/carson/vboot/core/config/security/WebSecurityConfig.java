package com.carson.vboot.core.config.security;


import com.carson.vboot.core.config.security.jwts.*;
import com.carson.vboot.core.config.security.permission.MyFilterSecurityInterceptor;
import com.carson.vboot.core.properties.IgnoredUrlsProperties;
import com.carson.vboot.core.properties.TokenProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

/**
 * Security 核心配置类
 * 开启注解控制权限至Controller
 *
 * @author Carson
 * 教程 https://www.cnblogs.com/wuyoucao/p/10863419.html
 * 开启注解模式 https://www.jianshu.com/p/41b7c3fb00e0
 * springBoot+springSecurity 数据库动态管理用户、角色、权限 https://blog.csdn.net/u012373815/article/details/54633046
 */
@Slf4j
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled=true)
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    @Autowired
    private IgnoredUrlsProperties ignoredUrlsProperties;


    @Autowired
    private MySuccessHandler mySuccessHandler;

    @Autowired
    private MyFailHandler myFailHandler;

    @Autowired
    private RestAccessDeniedHandler accessDeniedHandler;

    @Autowired
    private MyFilterSecurityInterceptor myFilterSecurityInterceptor;

    @Autowired
    private MyLogoutSuccessHandler myLogoutSuccessHandler;


    @Autowired
    private TokenProperties tokenProperties;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private SecurityUtil securityUtil;




    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider();
        // 自定义用户和密码
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder()); // 加密方法
    }



    @Override
    protected void configure(HttpSecurity http) throws Exception{

        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http.cors().and().csrf().disable()  // 允许跨域,csrf 关闭跨站请求
                .authorizeRequests();




        // 除配置文件忽略路径其它所有请求都需经过认证和授权
        for (String url : ignoredUrlsProperties.getUrls()) {
            registry.antMatchers(url).permitAll();
        }

        registry.and()
                // 表单登录方式
                .formLogin()
                // 如果没有登录，跳进这个方法，报错401，前端监听返回登录页面
                .loginPage("/security/login/page")
                .permitAll()
                // 登录接口api
                .loginProcessingUrl("/security/vboot/login")
                .permitAll() // 权限放开
                // 登录成功处理类
                .successHandler(mySuccessHandler)
                // 登录失败
                .failureHandler(myFailHandler)
                .and()
                // 允许网页iframe
                .headers().frameOptions().disable()
                .and()
                .logout()
                .logoutUrl("/logout")
                // 退出登录处理
                .logoutSuccessHandler(myLogoutSuccessHandler)
                .and()
                .authorizeRequests()
                // 任何请求
                .anyRequest()
                // 登录后可以访问
                .authenticated()
                .and()
                // 前后端分离采用JWT 不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 自定义权限拒绝处理类
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler)
                .and()
                // 添加自定义权限过滤器,用來配置按钮通过postman直接访问，如果没有登录直接拦截
                 .addFilterBefore(myFilterSecurityInterceptor, FilterSecurityInterceptor.class)
                // 添加JWT认证过滤器
                .addFilter(new JWTAuthenticationFilter(authenticationManager(), tokenProperties, redisTemplate, securityUtil));


       /* UsernamePasswordAuthenticationFilter类的attemptAuthentication方法，主要获取request传递的参数；
        UsernamePasswordAuthenticationToken类，主要用于增加认证所用到的额外参数；
        DaoAuthenticationProvider类的retrieveUser方法，主要把UsernamePasswordAuthenticationToken扩展类的额外参数传递给UserDetailsService；
        UserDetailsService实现类的loadUserByUsername方法。主用使用额外参数及账号、密码等多个参数进行验证。*/
    }


    public static void main(String[] args) {
        String encryptPass = new BCryptPasswordEncoder().encode("123456");
        System.out.println(encryptPass);
    }

}
