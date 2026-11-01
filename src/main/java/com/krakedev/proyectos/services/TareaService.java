package com.krakedev.proyectos.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.krakedev.proyectos.entidades.Empleado;
import com.krakedev.proyectos.entidades.Proyecto;
import com.krakedev.proyectos.entidades.Tarea;
import com.krakedev.proyectos.repositories.EmpleadoRepository;
import com.krakedev.proyectos.repositories.ProyectoRepository;
import com.krakedev.proyectos.repositories.TareaRepository;

@Service
public class TareaService {

	private final ProyectoRepository proyectoRepository;
	private final TareaRepository tareaRepository;
	private final EmpleadoRepository empleadoRepository;

	public TareaService(ProyectoRepository proyectoRepository, TareaRepository tareaRepository,
			EmpleadoRepository empleadoRepository) {
		super();
		this.proyectoRepository = proyectoRepository;
		this.tareaRepository = tareaRepository;
		this.empleadoRepository = empleadoRepository;
	}
	
	//Método guardar
	public Tarea guardarTarea(Tarea tarea) {
		//verificamos que exista el proyecto
		//Buscamos en la BD.
		Proyecto respuesta= proyectoRepository.findById(tarea.getProyecto().getId())
				.orElseThrow(()->
				new RuntimeException("Proyecto no existe"));
		//Guardamos en una lista los empleados obtenidos
		List<Empleado> empleadosBD= new ArrayList<Empleado>();
		//Creamos un forEach para recorrer los empleados
		for(Empleado empleado:tarea.getEmpleados()) {
			Empleado empleadoReal = empleadoRepository.findById(empleado.getId())
					.orElseThrow(()-> new RuntimeException("Empleado no existe"));
			//Agregamos el empleado encontrado
			empleadosBD.add(empleadoReal);
		}
		//Agreagmos el proyecto obtenido, y los empleados reales
		tarea.setProyecto(respuesta);
		tarea.setEmpleados(empleadosBD);
		//Retornamos guardadno la tarea
		return tareaRepository.save(tarea);
	}
	
	//Método listar
	public List<Tarea> listarTareas(){
		return tareaRepository.findAll();
	}
	
	//Método buscar
	public Tarea buscarTarea(int id) {
		
		Optional<Tarea> resultado=tareaRepository.findById(id);
		
		return resultado.orElse(null);
	}
	
	//Método actualizar
	public Tarea actualizarTarea(int id,Tarea datos) {
		Tarea tarea=buscarTarea(id);
		if(tarea == null) {
			return null;
		}
		tarea.setDescripcion(datos.getDescripcion());
		tarea.setFechaLimite(datos.getFechaLimite());
		tarea.setCostoEstimado(datos.getCostoEstimado());
		
		return tareaRepository.save(tarea);
	}
	
	//Método eliminar
	
	public boolean eliminarTarea(int id) {
		Tarea tarea=buscarTarea(id);
		if(tarea == null) {
			return false;
		}
		tareaRepository.deleteById(id);
		return true;
	}
	
	
}
