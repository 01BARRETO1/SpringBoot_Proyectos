package com.krakedev.proyectos.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.krakedev.proyectos.entidades.Proyecto;
import com.krakedev.proyectos.services.ProyectoService;

@RestController
@RequestMapping("/proyectos")
public class ProyectoController {

    private final ProyectoService proyectoService;

    public ProyectoController(ProyectoService proyectoService) {
        this.proyectoService = proyectoService;
    }

    // ==========================
    // GUARDAR PROYECTO
    // ==========================
    @PostMapping
    // @PreAuthorize ----> autorización, accedemos porque ya lo creamos en la clase SecurityConfing --> @EnableMethodSecurity
 	@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> guardar(@RequestBody Proyecto proyecto) {

        try {

            Proyecto nuevo = proyectoService.guardarProyecto(proyecto);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(nuevo);

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    // ==========================
    // LISTAR PROYECTOS
    // ==========================
    @GetMapping
    // @PreAuthorize ----> autorización, accedemos porque ya lo creamos en la clase SecurityConfing --> @EnableMethodSecurity
 	@PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<Proyecto>> listar() {

        return ResponseEntity.ok(
                proyectoService.listarProyectos());
    }

    // ==========================
    // BUSCAR POR ID
    // ==========================
    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable int id) {

        Proyecto proyecto = proyectoService.buscarProyecto(id);

        if (proyecto == null) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Proyecto no encontrado");
        }

        return ResponseEntity.ok(proyecto);
    }

    // ==========================
    // ACTUALIZAR
    // ==========================
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable int id,
            @RequestBody Proyecto datos) {

        try {

            Proyecto actualizado =
                    proyectoService.actualizarProyecto(id, datos);

            if (actualizado == null) {

                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Proyecto no encontrado");
            }

            return ResponseEntity.ok(actualizado);

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    // ==========================
    // ELIMINAR
    // ==========================
    @DeleteMapping("/{id}")
    // @PreAuthorize ----> autorización, accedemos porque ya lo creamos en la clase SecurityConfing --> @EnableMethodSecurity
 	@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> eliminar(@PathVariable int id) {

        boolean eliminado =
                proyectoService.eliminarProyecto(id);

        if (!eliminado) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Proyecto no encontrado");
        }

        return ResponseEntity.ok(
                "Proyecto eliminado correctamente");
    }
}