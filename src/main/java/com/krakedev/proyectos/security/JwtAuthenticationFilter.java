package com.krakedev.proyectos.security;

import java.io.IOException;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.krakedev.proyectos.services.TokenBlackListService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//Componente especial

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final TokenBlackListService blackListService;

	public JwtAuthenticationFilter(TokenBlackListService blackListService) {
		super();
		this.blackListService = blackListService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException { // TODO Auto-generated method stub

		// Capturara el Header
		String authHeader = request.getHeader("Authorization");
		// Comprobar si;
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {

			// bloquear la petición
			filterChain.doFilter(request, response);

			// parar el código, para que no llegue a la siguiente linea...
			//evitar un nullPointer y que no pueda ingresar a otras rutas
			return;
		}

		// Capturamos el token, como un pokemon
		String token = authHeader.substring(7);

		// Verificamos si ya fue destruido por el logout
		if (blackListService.estaInvalidado(token)) {

			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

			response.getWriter().write("Acceso denegado: Sesión Cerrada");

			return;
		}

		// Validamos el token
		DecodedJWT datosToken = JwtUtil.validarToken(token);

		// si nos retorna el token
		//Respondemos con los datos
		if (datosToken != null) {

			// Recuperamos el SUBJECT o sea el name
			String username = datosToken.getSubject();

			// Recuperamos el rol
			String rolOriginal = datosToken.getClaim("rol").asString();

			// Spring security exige el Rol con prefijo--> PREFIJO_
			// Convertimos a como exige Spring Security
			String rolSpring = "ROLE_" + rolOriginal;

			// Convertimos a objeto que lo entiende Spring -> SimpleGrantedAuthority
			SimpleGrantedAuthority authority = new SimpleGrantedAuthority(rolSpring);

			// -UNIFICAR LOS PERMISOS
			// PARÁMETRO.- De quién es el permiso --> username
			// Credenciales o contraseña -> null
			// permisos que va a tener el usuario, authority ->
			// Collections.singleton(authority)
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null,
					Collections.singleton(authority));

			// -HACER VISBLE LA PETICION
			SecurityContextHolder.getContext().setAuthentication(authentication);

		}
		// -Continue porque sí paso el filtro //Respuesta del servidor
		filterChain.doFilter(request, response);

	}

}

