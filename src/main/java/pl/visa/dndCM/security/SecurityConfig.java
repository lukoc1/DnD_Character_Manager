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
        // Zasady autoryzacji:
        //  - /user/a.requestMatchers("/favicon.ico", "/css/**", "/images/**", "/js/**").permitAll()dd i /login są publiczne (permitAll) — umożliwia rejestrację i dostęp do strony logowania
        //  - każde inne żądanie wymaga uwierzytelnienia
        http.authorizeHttpRequests(a ->
                a.requestMatchers("/user/register", "/login").permitAll()
                        .requestMatchers("/favicon.ico").permitAll()
                        .anyRequest().authenticated()
        )
        // Konfiguracja logowania formularzowego:
        //  - niestandardowa strona logowania: /login
        //  - defaultSuccessUrl ustawione na /home BEZ wymuszenia — jeśli użytkownik wcześniej próbował
        //    wejść na chroniony URL, Spring Security przekieruje go z powrotem do zapisanego żądania
        //    (saved request) zamiast zawsze kierować na /home
        .formLogin(f -> f.loginPage("/login").defaultSuccessUrl("/home")
                .permitAll()
        )
        // Konfiguracja wylogowania:
        //  - endpoint wylogowania to /logout i jest dostępny dla wszystkich (wylogowanie nie wymaga uwierzytelnienia)
        .logout(l -> l.logoutUrl("/logout")
                .permitAll());

        return http.build();
    }

}
