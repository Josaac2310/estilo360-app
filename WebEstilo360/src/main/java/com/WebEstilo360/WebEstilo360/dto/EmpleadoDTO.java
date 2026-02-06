package com.WebEstilo360.WebEstilo360.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpleadoDTO {
    private Long id_empleado;
    private String nombre_completo;
    private String especialidad;
    private String correo;
    private String movil;
}
