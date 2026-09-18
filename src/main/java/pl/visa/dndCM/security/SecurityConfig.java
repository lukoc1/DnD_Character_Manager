package pl.visa.dndCM.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(a ->
                a.requestMatchers("/user/register", "/login", "/access-denied").permitAll()
                        .requestMatchers("/favicon.ico").permitAll()
                        .requestMatchers("/api/**", "/swagger-ui/**", "/v3/api-docs/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
        )

                // ignorowanie certygfikatu dla -> /api/** wołane ręcznie, np przez swagger (admin)
                // w innych miejscach Thymeleaf sam dokleja token CSRF do <form>
        .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))

        .formLogin(f -> f.loginPage("/login").defaultSuccessUrl("/home")
                .permitAll()
        )

                // wylogowywuje (usuwa sesje)
                // dodaje parametr ?logout do login
        .logout(l -> l.logoutUrl("/logout")
                .permitAll())

                // przy próbie wejścia na stronę bez potrzebnej roli
        .exceptionHandling(e -> e.accessDeniedPage("/access-denied"));

        return http.build();
    }

}
