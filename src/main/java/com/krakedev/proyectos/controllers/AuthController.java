package com.krakedev.proyectos.controllers;




import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.krakedev.proyectos.entidades.Usuario;
import com.krakedev.proyectos.repositories.UsuarioRepository;
import com.krakedev.proyectos.security.JwtUtil;
import com.krakedev.proyectos.services.TokenBlackListService;
import com.krakedev.proyectos.services.UsuarioService;





@RestController
@RequestMapping("/auth")
public class AuthController {

	private final UsuarioService usuarioService;
	
	private final UsuarioRepository usuarioRepository;
	
	private final TokenBlackListService tokenBlackListService;

	public AuthController(UsuarioService usuarioService, UsuarioRepository usuarioRepository, TokenBlackListService tokenBlackListService ) {
		super();
		this.usuarioService = usuarioService;
		this.usuarioRepository=usuarioRepository;
		this.tokenBlackListService = tokenBlackListService;
	}
	
	@PostMapping("/registrar")//método registrar
	public ResponseEntity<?> registra(@RequestBody Usuario usuario){
		
		try {
			Usuario usuarioNuevo = usuarioService.guardar(usuario);
			//ResponseEntity de estado creado
			return ResponseEntity.status(HttpStatus.CREATED).body(usuarioNuevo);
			
		} catch (Exception e) {
			//INTERNAL_SERVER_ERROR
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error al registrar usuario: " + e.getMessage());
		}
		
	}
	
	//Método para login
	// clave:valor --> es un tipo de dato -> Map<String, String> -> y  se llama -> credenciales
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales){
		String username = credenciales.get("username");
		String password = credenciales.get("password");
		
		boolean autenticado = usuarioService.autenticar(username, password);
		
		if(autenticado) {
			//Buscamos a este usuario en la base de datos/ .get() para recuperarlo 
			Usuario usuario =usuarioRepository.findByUsername(username).get();
			
			String token = JwtUtil.generarToken(usuario.getUsername(), usuario.getRol());
			
			return ResponseEntity.ok(Map.of("token", token));
		}else {
			
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario o contraseña incorrecta");
		}
	}
	
	//Método para ingresar , perfil
		@GetMapping("/perfil")
		public ResponseEntity<?> verPerfil(){
			
			//Obtener los datos del token en la variable auth/ previamente obtenidos ...
			//en la clase JwtAutentication al final del metodo con--> ...
			// SecurityContextHolder.getContext().setAuthentication(authentication);
			//-----------°----------------
			//Obtenemos el contexto--> SecurityContextHolder.getContext() 
			//y los datos de autenticacion --> .getAuthentication()
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			
			//Dividimos los datos --- obtenemos el nombre
			String usuario = auth.getName();
			//---Obtenemos el rol
			String rol = auth.getAuthorities().iterator().next().getAuthority();
			
			//Respondemos con return
			return ResponseEntity.ok(Map.of("Mensaje", "Bienvenido al sistema protegido por Spring Security",
					"Usuario", usuario, "rol_detectado", rol, "Status", "Autenticado Exitosamente"));
			
		}
	
	//Método para hacer logout
		//Invalidar el token
		@PostMapping("/logout")
		public ResponseEntity<?> logout(
				@RequestHeader(
						value = "Authorization",
						required = false
						)String authHeader)
		{
			//---extarer el header
			if (authHeader != null && authHeader.startsWith("Bearer ")) {
				
				//Extraer el token
				//elimina-> substring(7)
				String token = authHeader.substring(7);
				
				//invalidar token
				tokenBlackListService.invalidarToken(token);
				
				//Return
				return ResponseEntity.ok(Map.of("Mensaje","Sesión cerrada Exitosamente. Token Invalido"));
				
			}else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token no proporcionado");
			}
			
		}
	
	
	
	
}

