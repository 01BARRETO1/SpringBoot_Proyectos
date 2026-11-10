package com.krakedev.proyectos.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.krakedev.proyectos.entidades.Empleado;
import com.krakedev.proyectos.services.EmpleadoService;

@RestController
@RequestMapping("/empleados")
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    // Guardar empleado
    @PostMapping
    public ResponseEntity<?> guardar(
            @RequestBody Empleado empleado) {

        try {

            Empleado nuevo =
                    empleadoService.guardarEmpleado(empleado);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(nuevo);

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    // Listar empleados
    @GetMapping
    // @PreAuthorize ----> autorización, accedemos porque ya lo creamos en la clase SecurityConfing --> @EnableMethodSecurity
  	@PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<Empleado>> listar() {

        return ResponseEntity.ok(
                empleadoService.listarEmpleados());
    }

    // Buscar empleado por id
    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(
            @PathVariable int id) {

        Empleado empleado =
                empleadoService.buscarEmpleado(id);

        if (empleado == null) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Empleado no encontrado");
        }

        return ResponseEntity.ok(empleado);
    }

    // Actualizar empleado
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable int id,
            @RequestBody Empleado datos) {

        try {

            Empleado actualizado =
                    empleadoService.actualizarEmpleado(
                            id,
                            datos);

            if (actualizado == null) {

                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Empleado no encontrado");
            }

            return ResponseEntity.ok(actualizado);

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    // Eliminar empleado
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(
            @PathVariable int id) {

        boolean eliminado =
                empleadoService.eliminarEmpleado(id);

        if (!eliminado) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Empleado no encontrado");
        }

        return ResponseEntity.ok(
                "Empleado eliminado correctamente");
    }
}