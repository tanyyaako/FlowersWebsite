package org.example.flowerswebsite.Config;

import org.apache.catalina.User;
import org.example.flowerswebsite.Entities.Enums.UserRoleEnum;
import org.example.flowerswebsite.Repositories.UserRepository;
import org.example.flowerswebsite.services.Impl.AppUserDetailsService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

@Configuration
public class AppSecurityConfiguration {
    private UserRepository userRepository;
    public AppSecurityConfiguration(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, SecurityContextRepository securityContextRepository) throws Exception {
        http
                .authorizeHttpRequests(
                        authorizeHttpRequests ->
                                authorizeHttpRequests.
                                        requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                                        .permitAll()
                                        .requestMatchers("/favicon.ico").permitAll()
                                        .requestMatchers("/error").permitAll()
                                        .requestMatchers("/home", "/users/login", "/users/register", "/users/login-error")
                                        .permitAll().
                                        requestMatchers("/product/catalog").authenticated().
                                        requestMatchers("/product/create","/product/list", "/product//{id}/edit","product/{id}/delete","/category/create","/category/listCategory", "/product//{id}/edit").hasRole(UserRoleEnum.ADMIN.name()).
                                        anyRequest().authenticated()
                )
                .formLogin(
                        (formLogin) ->
                                formLogin.
                                        loginPage("/users/login").
                                        usernameParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY).
                                        passwordParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY).
                                        defaultSuccessUrl("/home").
                                        failureForwardUrl("/users/login-error").
                                        successHandler((request, response, authentication) -> {
                                            String redirectUrl = "/home";
                                            if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                                                redirectUrl = "/product/list";
                                            }
                                            response.sendRedirect(redirectUrl);
                                        })
                )
                .logout((logout) ->
                        logout.logoutUrl("/users/logout").
                                logoutSuccessUrl("/home").
                                invalidateHttpSession(true)
                ).securityContext(
                        securityContext -> securityContext.
                                securityContextRepository(securityContextRepository)
                )
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new DelegatingSecurityContextRepository(
                new RequestAttributeSecurityContextRepository(),
                new HttpSessionSecurityContextRepository()
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() { return new AppUserDetailsService(userRepository); }
}
