package methodsecuritynew.bookingapp.security;


import lombok.RequiredArgsConstructor;

import methodsecuritynew.bookingapp.security.error.CustomAccessDeniedHandler;
import methodsecuritynew.bookingapp.security.error.CustomAuthenticationEntryPoint;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true
)
@RequiredArgsConstructor
public class SecurityConfig {
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailService customUserDetailService;
    private final CustomFilter customFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(customUserDetailService);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {



        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.authorizeHttpRequests(authorization -> {
            authorization.anyRequest().permitAll();
                }
        );

        httpSecurity.logout(logout -> {
            logout.logoutSuccessUrl("/account/login");
            logout.permitAll();
        });

        // cấu hình xác thực
        httpSecurity.authenticationProvider(authenticationProvider());
        //
        httpSecurity.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);

        // xử lý exception
        httpSecurity.exceptionHandling(exception ->{
            exception.authenticationEntryPoint(customAuthenticationEntryPoint);
            exception.accessDeniedHandler(customAccessDeniedHandler);
        });
        return httpSecurity.build();


    }
}
