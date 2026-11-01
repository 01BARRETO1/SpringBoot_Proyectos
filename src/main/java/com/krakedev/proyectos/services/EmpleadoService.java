package com.krakedev.proyectos.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.krakedev.proyectos.entidades.Empleado;
import com.krakedev.proyectos.repositories.EmpleadoRepository;

@Service
public class EmpleadoService {
	
	private final EmpleadoRepository empleadoRepository;

	public EmpleadoService(EmpleadoRepository empleadoRepository) {
		super();
		this.empleadoRepository = empleadoRepository;
	}
	
	//Guardar
	public Empleado guardarEmpleado(Empleado empleado) {
		if(empleadoRepository.existsByNombreAndCargo(
				empleado.getNombre(),
				empleado.getCargo())) {

	        throw new RuntimeException(
	                "El empleado: "+empleado.getNombre()+", ya existe");
	    }
		return empleadoRepository.save(empleado);
	}
	
	//Listar Empleado
	public List<Empleado> listarEmpleados(){
		return empleadoRepository.findAll();
	}
	
	//Buscar
	
	public Empleado buscarEmpleado(int id) {
		Optional<Empleado> resultado= 
				empleadoRepository.findById(id);
		
		return resultado.orElse(null);
	}
	
	//Actualizar
	
	public Empleado actualizarEmpleado(int id, Empleado datos) {
		
		Empleado empleado = buscarEmpleado(id);
		if(empleado == null) {
			return null;
		}
		empleado.setNombre(datos.getNombre());
		empleado.setCargo(datos.getCargo());
		
		return empleadoRepository.save(empleado);
	}
	
	//Eliminar
	public boolean eliminarEmpleado(int id) {
		Empleado empleado = buscarEmpleado(id);
		if(empleado == null) {
			return false;
		}
		
		empleadoRepository.deleteById(id);
		return true;
	}
	

}
