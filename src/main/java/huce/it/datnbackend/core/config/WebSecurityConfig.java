package huce.it.datnbackend.core.config;

import huce.it.datnbackend.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig  extends WebSecurityConfigurerAdapter{
    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        //leader
        http.authorizeRequests()
                .antMatchers("/", "/login","/logout", "homepage", "/admin.*", "/manager_brand.*", "/manager_customer.*").permitAll() // Các trang không yêu cầu login
                .regexMatchers("/leader.*","/leader.*","/save_task.*","/delete_task.*")
                .access("hasAnyRole('ROLE_LEADER')")
                .and().exceptionHandling().accessDeniedPage("/403");

        //manager
        http.authorizeRequests()
                .regexMatchers("/project_manager.*","/employee_manager.*","/add_employee.*","/save_employee_action.*","/update_employee.*","/delete_employee.*","/employees_of_department")
                .access("hasAnyRole('ROLE_MANAGER')")
                .and().exceptionHandling().accessDeniedPage("/403");

        http.authorizeRequests()
                .regexMatchers("/save_project.*","/delete_project.*","/department_manager.*")
                .access("hasAnyRole('ROLE_MANAGER')")
                .and().exceptionHandling().accessDeniedPage("/403");

        http.authorizeRequests()
                .regexMatchers("/getone_project.*")
                .access("hasAnyRole('ROLE_MANAGER','ROLE_LEADER')")
                .and().exceptionHandling().accessDeniedPage("/403");

        http.authorizeRequests().and().formLogin()//
                .loginProcessingUrl("/j_spring_security_check")
                .loginPage("/login")
                .defaultSuccessUrl("/loginSuccessfully")//đây Khi đăng nhập thành công
                .failureUrl("/login?error=Username or password is incorrect, please try again!")
                .usernameParameter("username")
                .passwordParameter("password")
                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/login").invalidateHttpSession(true);

        // Cấu hình Remember Me.  dùng cookie lưu lại trong 24h
        http.authorizeRequests().and() //
                .rememberMe().tokenRepository(this.persistentTokenRepository())
                .tokenValiditySeconds(1 * 24 * 60 * 60); // 24h
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        InMemoryTokenRepositoryImpl memory = new InMemoryTokenRepositoryImpl();
        return memory;
    }


}
