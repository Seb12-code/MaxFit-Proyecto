package com.miempresa.proyectoFinal.Security;

import com.miempresa.proyectoFinal.Service.DetallesUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private DetallesUsuarioService userDetailsService;

    public WebSecurityConfig(DetallesUsuarioService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    // Beans de config de autentificacion

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Define un bean para el DaoAuthenticationProvider, configurando UserDetailsService y PasswordEncoder
    // Esto resuelve la advertencia de deprecación
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // Define un bean para AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // Configuracion de la Cadena de Filtros de Seguridad (Autorización y Login/Logout)

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http

                .cors().and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .authorizeHttpRequests(authz -> authz

                        // Rutas públicas (sin autenticación)
                        .requestMatchers("/auth/**", "/css/**", "/js/**", "/img/**", "/error").permitAll()

                        // Rutas para administradores
                        .requestMatchers("/admin/**", "/admin/usuarios/**", "/admin/gestionUsuario/nuevo").hasRole("ADMIN")

                        // Rutas para vendedores y administradores
                        .requestMatchers("/vendedor/**").hasAnyRole("ADMIN", "VENDEDOR")
                        .requestMatchers("/productos/**", "/ventas/**", "/reportes/**").hasAnyRole("ADMIN", "VENDEDOR")

                        // Página principal (dashboard) requiere autenticación
                        .requestMatchers("/").authenticated()

                        .anyRequest().authenticated()


                )
                //  Configuración del formulario de login
                .formLogin(form -> form
                        .loginPage("/auth/login")          // URL de la pagina de login personalizada
                        .loginProcessingUrl("/login")      // URL a la que el formulario envía las credenciales
                        .defaultSuccessUrl("/", true)      // Redirige a la raíz ("/") después de un login exitoso
                        .failureUrl("/auth/login?error=true") // Redirige a la página de login con un error en caso de fallo
                        .permitAll()
                )
                //  Configuración del cierre de sesion
                .logout(logout -> logout
                        .logoutUrl("/logout")              // URL para cerrar sesion
                        .logoutSuccessUrl("/auth/login?logout=true")
                        .permitAll()
                )

                //  Manejo de excepciones (ej. acceso denegado)
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/auth/acceso-denegado")
                );


        http.authenticationProvider(authenticationProvider());

        return http.build();
    }
}