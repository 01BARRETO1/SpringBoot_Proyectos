package com.krakedev.proyectos.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


//@Configuration->Le dice a spring que esta clase tiene configuraciones
//importantes del sistema... que las lea cuando incie la aplicación, aquí se define las reglas

@Configuration

// @EnableMethodSecurity->Activar la seguridad vazada en métodos, nos permite utilizar anotaciones...
// oficales de springSecurity, @PreAthorize sobre la ruta de nuestros controladores

@EnableMethodSecurity
public class SecurityConfig {
	// Clase 
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	// inyección
	public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter){
		super();
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	}

	// Método para: configuraciones, lee -> @Configuration -> e inyecta directamente (HttpSecurity http)

	@Bean 
	public SecurityFilterChain securityFilterChain(HttpSecurity http) {
  
  //retornamos las configuracios: (HttpSecurity http)
		//1.- Apagar las cockies --> http.csrf(csrf -> csrf.disable())
		//2.- Activar modo sin memoria;
		//	.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		//3.- Accedera las rutas, ¿cuáles? Públicas;
		// 	.authorizeHttpRequests(auth ->auth.requestMatchers("/auth/login", "/auth/registar")
		//4.- tipo de permiso -> .permitAll()
		//5.- le decimos que cualquier otra ruta del servidor por ejemplo perfil, requiere authentication
		//	.anyRequest().authenticated())
		//6.- Agregar nuestro filtro llamado, jwtAuthenticationFilter...
		// 	.addFilterBefore(jwtAuthenticationFilter,
		// filtor por defecto --------> ...
		//   UsernamePasswordAuthenticationFilter.class)
		//7.- para que se construya ----> .build()
		return http.csrf(csrf -> csrf.disable())
				.sessionManagement(session ->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth.requestMatchers("/auth/login","/auth/registar")
						.permitAll()
						.anyRequest().authenticated())
				.addFilterBefore(jwtAuthenticationFilter,UsernamePasswordAuthenticationFilter.class) .build();
  
  }
}
