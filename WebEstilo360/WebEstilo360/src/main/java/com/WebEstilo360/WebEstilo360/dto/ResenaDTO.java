package com.WebEstilo360.WebEstilo360.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResenaDTO {
    private Long id_resena;
    private Long usuarioId;
    private Long empleadoId;
    private Integer puntuacion;
    private String comentario;
    private String fecha;
    
    // Para mostrar en la vista (nombres en lugar de IDs)
    private String nombreUsuario;
    private String nombreEmpleado;
}
