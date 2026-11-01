package com.krakedev.proyectos.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.krakedev.proyectos.entidades.Proyecto;

import com.krakedev.proyectos.repositories.ProyectoRepository;


@Service
public class ProyectoService {
	
	private final ProyectoRepository proyectoRepository;
	
	
	
	
	public ProyectoService(ProyectoRepository proyectoRepository) {
		super();
		this.proyectoRepository = proyectoRepository;
	}




	//----Guardar
	
	  public Proyecto guardarProyecto(Proyecto proyecto) {
		  return proyectoRepository.save(proyecto);
	  }
	  
	  //Listar
	  public List<Proyecto> listarProyectos(){
		  
		  return proyectoRepository.findAll();
	  }
	  
	  //Buscar
	  public Proyecto buscarProyecto(int id) {
		  Optional<Proyecto> respuesta=proyectoRepository.findById(id);
		  return respuesta.orElse(null);
	  }
	  
	  //Actualizar
	  public Proyecto actualizarProyecto(int id, Proyecto datos) {
		  Proyecto proyecto=buscarProyecto(id);
		  if(proyecto==null) {
			  return null;
		  }
		  
		  proyecto.setNombre(datos.getNombre());
		  proyecto.setDescripcion(datos.getDescripcion());
		  proyecto.setFechaInicio(datos.getFechaInicio());
		  
		  return proyectoRepository.save(proyecto);
	  }
	  
	  //Eliminar
	  public boolean eliminarProyecto(int id) {
		  Proyecto proyecto = buscarProyecto(id);
		  if(proyecto == null) {
			  return false;
		  }
		  proyectoRepository.deleteById(id);
		  return true;
	  }
	 
	

}
