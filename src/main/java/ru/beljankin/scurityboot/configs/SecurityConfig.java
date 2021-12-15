package ru.beljankin.scurityboot.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ru.beljankin.scurityboot.configs.handler.LoginSuccessHandler;
import ru.beljankin.scurityboot.services.PersonService;


import javax.sql.DataSource;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private PersonService personService;
    private LoginSuccessHandler loginSuccessHandler;

    @Autowired
    public void setLoginSuccessHandler(LoginSuccessHandler loginSuccessHandler){
        this.loginSuccessHandler = loginSuccessHandler;
    }

    @Autowired
    public void setPersonService(PersonService personService){
        this.personService = personService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.formLogin() // форма логирования по умолчанию
                .successHandler(loginSuccessHandler) // раскидываем по страницам в зависимости от ролей
                .permitAll();

        http.logout()
                .permitAll()
                // URL логаута
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                // указываем URL при удачном логауте
                .logoutSuccessUrl("/logoutsucsess")
                //выклчаем кроссдоменную секьюрность (на этапе обучения неважна)
                .and().csrf().disable();

        http
                // делаем страницу регистрации недоступной для авторизированных пользователей
                .authorizeRequests()
                //страницы аутентификаци доступна всем
                .antMatchers("/login").anonymous()
                .antMatchers("/user/**").access("hasAnyRole('USER', 'ADMIN')")
                // защищенные URL
                .antMatchers("/admin/**").access("hasAnyRole('ADMIN')").anyRequest().authenticated();


/*
        http.authorizeRequests()
                .antMatchers("/authenticated/**").authenticated()
                .antMatchers("/only_for_admins/**").hasRole("ADMIN")
                .and()
                .formLogin()
//                .loginProcessingUrl("/hello_login") // можно указать по какому адресу будет обрабатываться пароль
                .and()
                .logout().logoutSuccessUrl("/");

 */
    }





    // InMemory *
//    @Bean
//    public UserDetailsService users(){
//        UserDetails user = User.builder()
//                .username("user")
//                .password("{bcrypt}$2a$10$LgDpXUu.37U2lhasLJ5EFu/IHge8wy/mP9YgJ46TZvzbVTbBT5y4C")
//                .roles("USER")
//                .build();
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password("{bcrypt}$2a$10$LgDpXUu.37U2lhasLJ5EFu/IHge8wy/mP9YgJ46TZvzbVTbBT5y4C")
//                .roles("ADMIN","USER")
//                .build();
//        return new InMemoryUserDetailsManager(user, admin);
//
//    }

    // JdbcAuthentication
//    @Bean
//    public JdbcUserDetailsManager user(DataSource dataSource){
//            UserDetails user = User.builder()
//                .username("user")
//                .password("{bcrypt}$2a$10$LgDpXUu.37U2lhasLJ5EFu/IHge8wy/mP9YgJ46TZvzbVTbBT5y4C")
//                .roles("USER")
//                .build();
//            UserDetails admin = User.builder()
//                .username("admin")
//                .password("{bcrypt}$2a$10$LgDpXUu.37U2lhasLJ5EFu/IHge8wy/mP9YgJ46TZvzbVTbBT5y4C")
//                .roles("ADMIN","USER")
//                .build();
//
//        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
//        if(jdbcUserDetailsManager.userExists(user.getUsername())){
//            jdbcUserDetailsManager.deleteUser(user.getUsername());
//        }
//        if(jdbcUserDetailsManager.userExists(admin.getUsername())){
//            jdbcUserDetailsManager.deleteUser(admin.getUsername());
//        }
//        jdbcUserDetailsManager.createUser(user);
//        jdbcUserDetailsManager.createUser(admin);
//        return jdbcUserDetailsManager;
//    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(personService);

        return authenticationProvider;
    }

}
