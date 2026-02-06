package com.WebEstilo360.WebEstilo360.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CitaDTO {
    private Long id_cita;
    private Long usuario_id;
    private Long empleado_id;
    private Long servicio_id;
    private String fecha; // dd/MM/yyyy
    private String hora; // HH:mm
    private String estado;
    private String observaciones;
    
    // Para mostrar en la vista (nombres en lugar de IDs)
    private String nombreUsuario;
    private String nombreEmpleado;
    private String nombreServicio;
}