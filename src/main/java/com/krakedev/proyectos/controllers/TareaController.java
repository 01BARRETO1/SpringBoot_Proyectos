package com.krakedev.proyectos.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.krakedev.proyectos.entidades.Tarea;
import com.krakedev.proyectos.services.TareaService;

@RestController
@RequestMapping("/tareas")
public class TareaController {

    private final TareaService tareaService;

    public TareaController(TareaService tareaService) {
        this.tareaService = tareaService;
    }

    // =====================================
    // CREAR TAREA
    // =====================================
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> guardar(
            @RequestBody Tarea tarea) {

        try {

            Tarea nueva =
                    tareaService.guardarTarea(tarea);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(nueva);

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    // =====================================
    // LISTAR TODAS LAS TAREAS
    // =====================================
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<List<Tarea>> listar() {

        return ResponseEntity.ok(
                tareaService.listarTareas());
    }

    // =====================================
    // BUSCAR POR ID
    // =====================================
    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(
            @PathVariable int id) {

        Tarea tarea =
                tareaService.buscarTarea(id);

        if (tarea == null) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Tarea no encontrada");
        }

        return ResponseEntity.ok(tarea);
    }

    // =====================================
    // ACTUALIZAR
    // =====================================
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable int id,
            @RequestBody Tarea datos) {

        try {

            Tarea actualizada =
                    tareaService.actualizarTarea(id, datos);

            if (actualizada == null) {

                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Tarea no encontrada");
            }

            return ResponseEntity.ok(actualizada);

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    // =====================================
    // ELIMINAR
    // =====================================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(
            @PathVariable int id) {

        boolean eliminado =
                tareaService.eliminarTarea(id);

        if (!eliminado) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Tarea no encontrada");
        }

        return ResponseEntity.ok(
                "Tarea eliminada correctamente");
    }
}