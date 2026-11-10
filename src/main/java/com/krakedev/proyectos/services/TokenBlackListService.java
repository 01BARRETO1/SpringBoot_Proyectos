package com.krakedev.proyectos.services;


import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

@Service
public class TokenBlackListService {
	
	
	//ConcurrentHashMap--> no causa errores en la memoria si es que varios logauts estan al mismo tiempo
	//Set<String> blacklist--> guardamos los tokens utilizados
	private final Set<String> blacklist = ConcurrentHashMap.newKeySet();
	
	
	//método
	public void invalidarToken(String token) {
		
		blacklist.add(token);
		
	}
	
	//Método para ver si ya el token existe en la lista de invalidado, blacklist
	public boolean estaInvalidado(String token) {
		
		//contains--> busca que si lo que le envíamos está en la lista
		//true si existe, false si no
		return blacklist.contains(token);
	}

}
