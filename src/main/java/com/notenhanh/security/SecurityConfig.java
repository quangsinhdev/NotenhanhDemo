package com.notenhanh.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig  {
	 private final OAuth2LoginAuthenticationSuccessHandler oAuth2Login;
	 private final OAuth2LoginAuthenticationFailureHandler oAuth2LoginAuthenticationFailureHandler;
	 private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
	 private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
	    public SecurityConfig(OAuth2LoginAuthenticationSuccessHandler oAuth2Login, OAuth2LoginAuthenticationFailureHandler oAuth2LoginAuthenticationFailureHandler, CustomAuthenticationFailureHandler customAuthenticationFailureHandler, CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) {
	        this.oAuth2Login = oAuth2Login;
	        this.oAuth2LoginAuthenticationFailureHandler = oAuth2LoginAuthenticationFailureHandler;
	        this.customAuthenticationFailureHandler = customAuthenticationFailureHandler;
	        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
	    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorizeRequests ->
            authorizeRequests

            	.requestMatchers("/","/login", "/register","/recovery","/updatepassword","/guest/**","/static/**","/images/**", "/css/**", "/js/**","/font/**","/sass/**","/policy","/terms","/error").permitAll()
                .requestMatchers("/login/authenticate","/login/registry","/send-email", "/request-recovery", "/change-newpassword", "/login/oauth2/code/google/**").permitAll()
                .requestMatchers("/login/oauth2/code/facebook").denyAll()
                .requestMatchers("/dashboard","/notestatistics","/feedback","/client/**","/logout").authenticated()
                .requestMatchers("/features/createtextnote","/features/createtasknote","/update/textnote/{id}","/update/tasknote/{id}","/features/textnote/delete/{noteId}","/features/tasknote/delete/{noteId}","/features/createfeedback","/changepasswordmyprofile").authenticated()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/admin/users/updaterole","/admin/users/delete","/admin/users/changeStatus","/admin/users/websettings").hasRole("ADMIN")
                .anyRequest().authenticated()
                )
            .csrf(csrf -> csrf.disable())
            .formLogin(form -> form
                    .loginPage("/login")
                    .loginProcessingUrl("/login/authenticate")
                    .successHandler(customAuthenticationSuccessHandler)
                    .failureHandler(customAuthenticationFailureHandler)
                    
                )
            
            .oauth2Login(oauth2 -> oauth2
                    .loginPage("/login")
                    .defaultSuccessUrl("/dashboard", true)
                    .failureHandler(oAuth2LoginAuthenticationFailureHandler)
                    .successHandler(oAuth2Login)
            )
            
                .logout(logout -> logout 
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout=true")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID", "XSRF-TOKEN")
                    .permitAll()
                )
                .requiresChannel(channel ->
                channel
                .anyRequest().requiresSecure()
        );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}