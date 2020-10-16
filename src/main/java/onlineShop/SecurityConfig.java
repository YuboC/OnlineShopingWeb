package onlineShop;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.context.annotation.Bean;

// securityConfig class 定义了security framework 如何去DB里找到用户名和密码，是当前用户得到query权限

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired // 链接ApplicationConfig里定义的DataSource
    private DataSource dataSource;

    // Step 2: 登陆后判断业务逻辑权限
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .formLogin()
                .loginPage("/login");
        http
                .authorizeRequests()
                // .antMatchers 会去判断当前用户是否有权限访问对应api，若false则跳转到login page
                .antMatchers("/cart/**").hasAuthority("ROLE_USER")
                // "ROLE_USER", "ROLE_ADMIN" 两个用户都可以访问 get api
                .antMatchers("/get*/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                .antMatchers("/admin*/**").hasAuthority("ROLE_ADMIN")
                .anyRequest().permitAll();
        http
                .logout()
                .logoutUrl("/logout");

    }

    // step1: 登录时 Authentication
    // 两种验证方法：1. 内部使用的inMemoryAuthentication，存储在内存中，项目关闭后删除
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication().withUser("yubo@yubo.com").password("123").authorities("ROLE_ADMIN");

        auth
                .jdbcAuthentication()
                .dataSource(dataSource) // DB的位置，通过之前的autowire去DB里select
                // 拿到用户名和密码，emailId=? 从浏览器登录时得到的Id，然后比较DB中的数据
                .usersByUsernameQuery("SELECT emailId, password, enabled FROM users WHERE emailId=?")
                // 拿到用户权限，判断是否有权限访问
                .authoritiesByUsernameQuery("SELECT emailId, authorities FROM authorities WHERE emailId=?");

    }

    @SuppressWarnings("deprecation")
    @Bean
    public static NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
    }
}
