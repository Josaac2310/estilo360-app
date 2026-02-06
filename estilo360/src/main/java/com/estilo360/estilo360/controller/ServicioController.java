package com.estilo360.estilo360.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.estilo360.estilo360.dto.ServicioDTO;
import com.estilo360.estilo360.services.PdfService;
import com.estilo360.estilo360.services.ServicioService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import jakarta.validation.Valid;


/**
 * Controlador REST para la gestión de servicios.
 * 
 * Proporciona endpoints para crear, listar, obtener, actualizar y eliminar
 * servicios, así como para la descarga de un documento PDF con la información
 * de los servicios disponibles.
 * 
 * @version 1.0
 */
@RestController
@RequestMapping("/servicios")
public class ServicioController {

	
    /** Servicio de negocio encargado de la gestión de servicios */
    private final ServicioService servicioService;
    
    /** Servicio encargado de la generación de documentos PDF */
    private final PdfService pdfService;
    
    
    /**
     * Constructor del controlador de servicios.
     * 
     * @param servicioService Servicio que contiene la lógica de negocio de servicios
     * @param pdfService Servicio encargado de generar documentos PDF
     */
    public ServicioController(ServicioService servicioService, PdfService pdfService) {
        this.servicioService = servicioService;
        this.pdfService = pdfService;
    }

    /**
     * Crea un nuevo servicio.
     * 
     * @param dto Objeto con la información del servicio a crear
     * @return Servicio creado
     */
    @PostMapping
    public ServicioDTO crear(@Valid @RequestBody ServicioDTO dto) {
        return servicioService.crearServicio(dto);
    }

    /**
     * Lista todos los servicios registrados.
     * 
     * @return Lista de servicios
     */
    @GetMapping
    public List<ServicioDTO> listar() {
        return servicioService.listarServicios();
    }
    
    /**
     * Obtiene un servicio por su identificador.
     * 
     * @param id Identificador único del servicio
     * @return Servicio correspondiente al identificador
     */
    @GetMapping("/{id}")
    public ServicioDTO obtener(@PathVariable Long id) {
        return servicioService.obtenerPorId(id);
    }

    /**
     * Actualiza la información de un servicio existente.
     * 
     * @param id Identificador único del servicio
     * @param dto Objeto con los nuevos datos del servicio
     * @return Servicio actualizado
     */
    @PutMapping("/{id}")
    public ServicioDTO actualizar(@PathVariable Long id, @Valid @RequestBody ServicioDTO dto) {
        return servicioService.actualizarServicio(id, dto);
    }

    
    /**
     * Elimina un servicio por su identificador.
     * 
     * @param id Identificador único del servicio a eliminar
     */
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        servicioService.eliminarServicio(id);
    }
    
    
    /**
     * Descarga un archivo PDF con el listado de servicios.
     * 
     * @return Respuesta HTTP con el archivo PDF adjunto
     */
    @GetMapping("/descargar-pdf")
    public ResponseEntity<byte[]> descargarPdfServicios() {
        try {
            byte[] pdfBytes = pdfService.generarPdfServicios();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "servicios-estilo360.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);

        } catch (Exception e) {
            System.err.println("Error al descargar PDF: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}