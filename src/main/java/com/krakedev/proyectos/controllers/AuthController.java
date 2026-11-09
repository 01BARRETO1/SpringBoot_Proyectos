package com.krakedev.proyectos.controllers;




import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.krakedev.proyectos.entidades.Usuario;

import com.krakedev.proyectos.services.UsuarioService;



@RestController
@RequestMapping("/auth")
public class AuthController {

	private final UsuarioService usuarioService;
	

	public AuthController(UsuarioService usuarioService) {
		super();
		this.usuarioService = usuarioService;
		
		
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
			
			
			return ResponseEntity.ok(Map.of("Contraseña encriptada: ", password, "Usuario", username));
		}else {
			
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario o contraseña incorrecta");
		}
	}
	
	
	
	
	
}

